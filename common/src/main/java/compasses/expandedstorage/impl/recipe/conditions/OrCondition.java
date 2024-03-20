package compasses.expandedstorage.impl.recipe.conditions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class OrCondition implements RecipeCondition {
    public static final ResourceLocation NETWORK_ID = Utils.id("or");
    private final RecipeCondition[] conditions;

    public OrCondition(RecipeCondition... conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean isExactMatch() {
        return false;
    }

    @Override
    public boolean test(Object subject) {
        for (RecipeCondition condition : conditions) {
            if (condition.test(subject)) {
                return true;
            }
        }
        return false;
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

    public static OrCondition readFromBuffer(FriendlyByteBuf buffer) {
        RecipeCondition[] conditions = buffer.readCollection(ArrayList::new, RecipeCondition::readFromNetworkBuffer).toArray(RecipeCondition[]::new);
        return new OrCondition(conditions);
    }

    @Nullable
    @Override
    public JsonElement toJson(@Nullable JsonObject ignore) {
        if (ignore != null) {
            throw new IllegalStateException("JsonObject should not be passed.");
        }

        JsonArray array = new JsonArray();
        for (RecipeCondition condition : conditions) {
            JsonObject object = new JsonObject();
            condition.toJson(object);
            array.add(object);
        }
        return array;
    }
}
