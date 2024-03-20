package compasses.expandedstorage.impl.recipe.conditions;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class HasPropertyCondition implements RecipeCondition {
    public static final ResourceLocation NETWORK_ID = Utils.id("has_property");
    private final ResourceLocation blockId;
    private final Map<Property<?>, Object> properties;
    private final boolean optional;

    public HasPropertyCondition(ResourceLocation blockId, Map<Property<?>, Object> properties, boolean optional) {
        this.blockId = blockId;
        this.properties = properties;
        this.optional = optional;
    }

    @Override
    public boolean isExactMatch() {
        return false;
    }

    @Override
    public boolean test(Object subject) {
        BlockState state = (BlockState) subject;
        for (Map.Entry<Property<?>, Object> entry : properties.entrySet()) {
            if (state.hasProperty(entry.getKey())) {
                if (!state.getValue(entry.getKey()).equals(entry.getValue())) {
                    return false;
                }
            } else if (!optional) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ResourceLocation getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(blockId);
        buffer.writeBoolean(optional);
        buffer.writeInt(properties.size());
        for (Map.Entry<Property<?>, ?> property : properties.entrySet()) {
            buffer.writeUtf(property.getKey().getName());
            buffer.writeUtf(property.getValue().toString());
        }
    }

    public static HasPropertyCondition readFromBuffer(FriendlyByteBuf buffer) {
        ResourceLocation blockId = buffer.readResourceLocation();
        if (!BuiltInRegistries.BLOCK.containsKey(blockId)) {
            throw new IllegalStateException("Received an unknown block: " + blockId);
        }
        Block block = BuiltInRegistries.BLOCK.get(blockId);
        boolean optional = buffer.readBoolean();
        int numProperties = buffer.readInt();

        Map<Property<?>, Object> properties = Maps.newHashMapWithExpectedSize(numProperties);
        for (int i = 0; i < numProperties; i++) {
            Property<?> key = block.getStateDefinition().getProperty(buffer.readUtf());
            // todo: we should be more safe when it comes to networking, server could send bad data intentionally
            Object value = key.getValue(buffer.readUtf()).orElseThrow();
            properties.put(key, value);
        }

        return new HasPropertyCondition(blockId, properties, optional);
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
        JsonObject state = new JsonObject();
        for (Map.Entry<Property<?>, Object> entry : properties.entrySet()) {
            state.addProperty(entry.getKey().getName(), entry.getValue().toString());
        }
        object.add("state", state);
    }
}
