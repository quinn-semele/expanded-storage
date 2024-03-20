package compasses.expandedstorage.impl.recipe.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class AndCondition implements RecipeCondition {
    public static final ResourceLocation NETWORK_ID = Utils.id("and");
    private final RecipeCondition[] conditions;

    public AndCondition(RecipeCondition... conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean isExactMatch() {
        return Arrays.stream(conditions).anyMatch(RecipeCondition::isExactMatch);
    }

    @Override
    public boolean test(Object subject) {
        for (RecipeCondition condition : conditions) {
            if (!condition.test(subject)) {
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
        buffer.writeCollection(Arrays.asList(conditions), (b, condition) -> {
            buffer.writeResourceLocation(condition.getNetworkId());
            condition.writeToBuffer(b);
        });
    }

    public static AndCondition readFromBuffer(FriendlyByteBuf buffer) {
        RecipeCondition[] conditions = buffer.readCollection(ArrayList::new, RecipeCondition::readFromNetworkBuffer).toArray(RecipeCondition[]::new);
        return new AndCondition(conditions);
    }

    @Nullable
    @Override
    public JsonElement toJson(@Nullable JsonObject object) {
        if (object != null) {
            writeToJsonObject(object);
            return null;
        }
        JsonObject jsonObject = new JsonObject();
        writeToJsonObject(jsonObject);
        return jsonObject;
    }

    private void writeToJsonObject(JsonObject object) {
        for (RecipeCondition condition : conditions) {
            condition.toJson(object);
        }
    }
}
