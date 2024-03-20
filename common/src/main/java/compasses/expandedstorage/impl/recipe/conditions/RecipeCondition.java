package compasses.expandedstorage.impl.recipe.conditions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.recipe.misc.JsonHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This uhhhhh system is uhhhhhhhhh very uhhhhhhhhh yes...
 * Is it over-engineer, probably not going to be used and eventually be removed? Yes!
 */
public interface RecipeCondition {
    Map<ResourceLocation, Function<FriendlyByteBuf, RecipeCondition>> RECIPE_DESERIALIZERS = Map.of(
            AndCondition.NETWORK_ID, AndCondition::readFromBuffer,
            HasPropertyCondition.NETWORK_ID, HasPropertyCondition::readFromBuffer,
            IsInstanceOfCondition.NETWORK_ID, IsInstanceOfCondition::readFromBuffer,
            IsInTagCondition.NETWORK_ID, IsInTagCondition::readFromBuffer,
            IsRegistryObject.NETWORK_ID, IsRegistryObject::readFromBuffer,
            OrCondition.NETWORK_ID, OrCondition::readFromBuffer
    );
    ResourceLocation IS_WOODEN_CHEST_ID = Utils.id("is_wooden_chest");
    ResourceLocation IS_WOODEN_BARREL_ID = Utils.id("is_wooden_barrel");
    IsInstanceOfCondition IS_WOODEN_CHEST = new IsInstanceOfCondition(ChestBlock.class);
    IsInstanceOfCondition IS_WOODEN_BARREL = new IsInstanceOfCondition(BarrelBlock.class);

    private static <T> RecipeCondition tryReadGenericCondition(JsonElement condition, Registry<T> registry) {
        if (condition.isJsonObject()) {
            JsonObject object = condition.getAsJsonObject();
            if (object.has("tag")) {
                TagKey<T> tag = TagKey.create(registry.key(), JsonHelper.getJsonResourceLocation(object, "tag"));
                return new IsInTagCondition(tag);
            } else if (object.has("id")) {
                return new IsRegistryObject(registry, JsonHelper.getJsonResourceLocation(object, "id"));
            }
            return null;
        } else if (condition.isJsonArray()) {
            JsonArray conditions = condition.getAsJsonArray();
            RecipeCondition[] recipeConditions = new RecipeCondition[conditions.size()];
            Function<JsonElement, RecipeCondition> function = registry == BuiltInRegistries.BLOCK ? RecipeCondition::readBlockCondition : RecipeCondition::readEntityCondition;
            for (int i = 0; i < conditions.size(); i++) {
                recipeConditions[i] = function.apply(conditions.get(i));
            }
            return new OrCondition(recipeConditions);
        } else {
            throw new JsonSyntaxException("condition must be an Object or an Array.");
        }
    }

    static RecipeCondition readBlockCondition(JsonElement condition) {
        RecipeCondition generic = tryReadGenericCondition(condition, BuiltInRegistries.BLOCK);
        if (generic != null) {
            if (generic instanceof IsInTagCondition) {
                JsonObject objCondition = (JsonObject) condition;
                if (objCondition.has("state")) {
                    throw new IllegalStateException("Cannot combine in tag and has property checks.");
                }
            } else if (generic instanceof IsRegistryObject isRegistryObject) {
                JsonObject objCondition = (JsonObject) condition;
                if (objCondition.has("state")) {
                    Block block = (Block) isRegistryObject.getValue();
                    Map<String, Property<?>> propertyLookup = block.defaultBlockState().getProperties().stream()
                                                                   .map(it -> Map.entry(it.getName(), it))
                                                                   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    boolean optional = objCondition.has("optional") && JsonHelper.getJsonBoolean(objCondition, "optional");
                    JsonObject properties = JsonHelper.getJsonObject(objCondition, "state");
                    //noinspection rawtypes
                    Map.Entry[] stateProperties = new Map.Entry[properties.size()];
                    int index = 0;
                    for (Map.Entry<String, JsonElement> propertyEntry : properties.entrySet()) {
                        if (!propertyLookup.containsKey(propertyEntry.getKey())) {
                            throw new IllegalArgumentException("Block does not contain property with name: " + propertyEntry.getKey());
                        }
                        Property<?> property = propertyLookup.get(propertyEntry.getKey());
                        String propertyValue = JsonHelper.toString(property.getName(), propertyEntry.getValue());
                        Optional<?> value = property.getValue(propertyValue);
                        if (value.isEmpty()) {
                            throw new IllegalStateException("Property " + property.getName() + " doesn't contain value " + propertyValue);
                        }
                        stateProperties[index] = Map.entry(property, value.get());
                        index++;
                    }
                    //noinspection deprecation
                    return new AndCondition(generic, new HasPropertyCondition(block.builtInRegistryHolder().key().location(), Map.ofEntries(stateProperties), optional));
                }
            }
            return generic;
        }
        if (condition.isJsonObject()) {
            JsonObject recipeCondition = condition.getAsJsonObject();
            if (recipeCondition.has("condition")) {
                ResourceLocation conditionId = JsonHelper.getJsonResourceLocation(recipeCondition, "condition");
                if (RecipeCondition.IS_WOODEN_CHEST_ID.equals(conditionId)) {
                    return RecipeCondition.IS_WOODEN_CHEST;
                } else if (RecipeCondition.IS_WOODEN_BARREL_ID.equals(conditionId)) {
                    return RecipeCondition.IS_WOODEN_BARREL;
                } else {
                    throw new IllegalArgumentException("condition with id " + conditionId + " doesn't exist.");
                }
            }
        }
        throw new JsonSyntaxException("Unknown recipe condition");
    }

    static RecipeCondition readEntityCondition(JsonElement condition) {
        RecipeCondition generic = tryReadGenericCondition(condition, BuiltInRegistries.ENTITY_TYPE);
        if (generic != null) {
            return generic;
        }
        throw new JsonSyntaxException("Unknown recipe condition");
    }

    // Honestly not sure if we like this solution
    static Object unwrap(Object subject) {
        if (subject instanceof BlockState state) {
            return state.getBlock();
        } else if (subject instanceof Entity entity) {
            return entity.getType();
        }
        return subject;
    }

    boolean isExactMatch();

    boolean test(Object subject);

    ResourceLocation getNetworkId();

    void writeToBuffer(FriendlyByteBuf buffer);

    static RecipeCondition readFromNetworkBuffer(FriendlyByteBuf buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        if (RECIPE_DESERIALIZERS.containsKey(id)) {
            return RECIPE_DESERIALIZERS.get(id).apply(buffer);
        } else {
            throw new IllegalStateException("Cannot find recipe condition with id: \"" + id + "\"");
        }
    }

    /**
     * If object passed should write into that object otherwise return a new element.
     */
    @Nullable
    JsonElement toJson(@Nullable JsonObject object);
}

