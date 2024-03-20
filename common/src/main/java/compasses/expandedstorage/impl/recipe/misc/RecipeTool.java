package compasses.expandedstorage.impl.recipe.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import compasses.expandedstorage.impl.item.StorageConversionKit;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract sealed class RecipeTool permits RecipeTool.UpgradeTool, RecipeTool.MutatorTool {
    private final ResourceLocation toolId;

    private RecipeTool(ResourceLocation toolId) {
        this.toolId = toolId;
    }

    public boolean isMatchFor(ItemStack tool) {
        //noinspection deprecation
        return tool.getItem().builtInRegistryHolder().is(toolId);
    }

    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(toolId);
    }

    public JsonObject toJson() {
        JsonObject tool = new JsonObject();
        tool.addProperty("id", toolId.toString());
        return tool;
    }

    public static final class UpgradeTool extends RecipeTool {
        public UpgradeTool(ResourceLocation toolId) {
            super(toolId);
        }

        public UpgradeTool(Item item) {
            //noinspection deprecation
            super(item.builtInRegistryHolder().key().location());
        }
    }

    public static final class MutatorTool extends RecipeTool {
        private final String requiredName;

        public MutatorTool(String requiredName) {
            super(Utils.id("storage_mutator"));
            this.requiredName = requiredName;
        }

        @Override
        public boolean isMatchFor(ItemStack tool) {
            boolean isNameMatch = true;
            if (requiredName != null) {
                isNameMatch = tool.getHoverName().getString().equalsIgnoreCase(requiredName);
            }
            return isNameMatch && super.isMatchFor(tool);
        }

        @Override
        public void writeToBuffer(FriendlyByteBuf buffer) {
            super.writeToBuffer(buffer);
            buffer.writeNullable(requiredName, FriendlyByteBuf::writeUtf);
        }

        @Override
        public JsonObject toJson() {
            JsonObject tool = super.toJson();
            if (requiredName != null) {
                tool.addProperty("name", requiredName);
            }
            return tool;
        }

        public String getRequiredName() {
            return requiredName;
        }
    }

    public static RecipeTool fromJsonObject(JsonObject object) {
        ResourceLocation toolId = JsonHelper.getJsonResourceLocation(object, "id");
        if (toolId.toString().equals("expandedstorage:storage_mutator")) {
            if (object.has("name")) {
                JsonElement name = object.get("name");
                if (name.isJsonPrimitive() && name.getAsJsonPrimitive().isString()) {
                    return new MutatorTool(name.getAsString());
                } else {
                    throw new JsonSyntaxException("Tool's name entry must be a string");
                }
            } else {
                return new MutatorTool(null);
            }
        } else if (BuiltInRegistries.ITEM.get(toolId) instanceof StorageConversionKit) {
            return new UpgradeTool(toolId);
        } else {
            throw new IllegalArgumentException("Tool id supplied is not a conversion kit or the storage mutator.");
        }
    }

    public static RecipeTool fromNetworkBuffer(FriendlyByteBuf buffer) {
        ResourceLocation toolId = buffer.readResourceLocation();
        if (toolId.toString().equals("expandedstorage:storage_mutator")) {
            String name = buffer.readNullable(FriendlyByteBuf::readUtf);
            return new MutatorTool(name);
        } else if (BuiltInRegistries.ITEM.get(toolId) instanceof StorageConversionKit) {
            return new UpgradeTool(toolId);
        } else {
            throw new IllegalArgumentException("Invalid tool id sent by the server.");
        }
    }
}
