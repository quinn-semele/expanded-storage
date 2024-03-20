package compasses.expandedstorage.impl.recipe.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class IsInstanceOfCondition implements RecipeCondition {
    public static final ResourceLocation NETWORK_ID = Utils.id("is_instance");
    private final Class<?> clazz;

    public IsInstanceOfCondition(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean isExactMatch() {
        return false;
    }

    @Override
    public boolean test(Object subject) {
        return clazz.isAssignableFrom(RecipeCondition.unwrap(subject).getClass());
    }

    @Override
    public ResourceLocation getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        if (this == RecipeCondition.IS_WOODEN_BARREL) {
            buffer.writeResourceLocation(RecipeCondition.IS_WOODEN_BARREL_ID);
        } else if (this == RecipeCondition.IS_WOODEN_CHEST) {
            buffer.writeResourceLocation(RecipeCondition.IS_WOODEN_CHEST_ID);
        } else {
            throw new IllegalStateException("trying to send unknown is instance condition.");
        }
    }

    public static IsInstanceOfCondition readFromBuffer(FriendlyByteBuf buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        if (RecipeCondition.IS_WOODEN_BARREL_ID.equals(id)) {
            return RecipeCondition.IS_WOODEN_BARREL;
        } else if (RecipeCondition.IS_WOODEN_CHEST_ID.equals(id)) {
            return RecipeCondition.IS_WOODEN_CHEST;
        }
        throw new IllegalStateException("unknown is instance condition sent.");
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
        String conditionName = null;
        if (this == RecipeCondition.IS_WOODEN_BARREL) {
            conditionName = RecipeCondition.IS_WOODEN_BARREL_ID.toString();
        } else if (this == RecipeCondition.IS_WOODEN_CHEST) {
            conditionName = RecipeCondition.IS_WOODEN_CHEST_ID.toString();
        }

        if (conditionName != null) {
            object.addProperty("condition", conditionName);
        } else {
            throw new IllegalStateException("Cannot serialize this instance of to json");
        }
    }
}
