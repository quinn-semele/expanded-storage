package compasses.expandedstorage.impl.recipe.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class IsRegistryObject implements RecipeCondition {
    public static final ResourceLocation NETWORK_ID = Utils.id("is_registry_object");
    private final Object value;
    private final ResourceLocation registry;
    private final ResourceLocation objectId;

    public IsRegistryObject(Registry<?> registry, ResourceLocation id) {
        this.value = registry.get(id);
        this.registry = registry.key().location();
        this.objectId = id;
    }

    @Override
    public boolean isExactMatch() {
        return true;
    }

    @Override
    public boolean test(Object subject) {
        return RecipeCondition.unwrap(subject) == value;
    }

    @Override
    public ResourceLocation getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(registry);
        buffer.writeResourceLocation(objectId);
    }

    public Object getValue() {
        return value;
    }

    public static IsRegistryObject readFromBuffer(FriendlyByteBuf buffer) {
        ResourceLocation registryId = buffer.readResourceLocation();
        ResourceLocation objectId = buffer.readResourceLocation();
        Registry<?> registry = BuiltInRegistries.REGISTRY.get(registryId);
        if (registry == null) {
            throw new NullPointerException("Unknown registry: " + registryId);
        }
        return new IsRegistryObject(registry, objectId);
    }

    @Nullable
    @Override
    public JsonElement toJson(@Nullable JsonObject object) {
        if (object != null) {
            writeToJsonObject(object);
            return null;
        } else {
            JsonObject jsonObject = new JsonObject();
            writeToJsonObject(jsonObject);
            return jsonObject;
        }
    }

    public void writeToJsonObject(JsonObject object) {
        object.addProperty("id", objectId.toString());
    }
}
