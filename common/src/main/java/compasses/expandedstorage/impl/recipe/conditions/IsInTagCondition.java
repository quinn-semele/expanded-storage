package compasses.expandedstorage.impl.recipe.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

public class IsInTagCondition implements RecipeCondition {
    public static final ResourceLocation NETWORK_ID = Utils.id("in_tag");
    private final TagKey<?> tagKey;
    private Set<Object> values;

    public IsInTagCondition(TagKey<?> tagKey) {
        this.tagKey = tagKey;
    }

    @Override
    public boolean isExactMatch() {
        return false;
    }

    @Override
    public boolean test(Object subject) {
        if (values == null) {
            //noinspection unchecked
            values = ((HolderSet.Named<Object>) BuiltInRegistries.REGISTRY.get(tagKey.registry().location()).getTag((TagKey) tagKey).orElseThrow()).stream().map(Holder::value).collect(Collectors.toUnmodifiableSet());
        }
        return values.contains(RecipeCondition.unwrap(subject));
    }

    @Override
    public ResourceLocation getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(tagKey.registry().location());
        buffer.writeResourceLocation(tagKey.location());
    }

    public static IsInTagCondition readFromBuffer(FriendlyByteBuf buffer) {
        ResourceLocation registryId = buffer.readResourceLocation();
        ResourceLocation tag = buffer.readResourceLocation();
        Registry<?> registry = BuiltInRegistries.REGISTRY.get(registryId);
        if (registry == null) {
            throw new NullPointerException("Unknown registry: " + registryId);
        }
        return new IsInTagCondition(TagKey.create(registry.key(), tag));
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

    private void writeToJsonObject(JsonObject object) {
        object.addProperty("tag", tagKey.location().toString());
    }
}
