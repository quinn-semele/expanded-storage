package compasses.expandedstorage.impl.misc;

import compasses.expandedstorage.impl.recipe.BlockConversionRecipe;
import compasses.expandedstorage.impl.recipe.ConversionRecipeManager;
import compasses.expandedstorage.impl.recipe.EntityConversionRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.ArrayList;
import java.util.List;

public class ClientboundUpdateRecipesMessage implements CustomPacketPayload {
    public static final ResourceLocation ID = Utils.id("update_conversion_recipes");
    private final List<BlockConversionRecipe<?>> blockRecipes;
    private final List<EntityConversionRecipe<?>> entityRecipes;

    public ClientboundUpdateRecipesMessage(List<BlockConversionRecipe<?>> blockRecipes, List<EntityConversionRecipe<?>> entityRecipes) {
        this.blockRecipes = blockRecipes;
        this.entityRecipes = entityRecipes;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeCollection(blockRecipes, (b, recipe) -> recipe.writeToBuffer(b));
        buffer.writeCollection(entityRecipes, (b, recipe) -> recipe.writeToBuffer(b));
    }

    public static ClientboundUpdateRecipesMessage decode(FriendlyByteBuf buffer) {
        List<BlockConversionRecipe<?>> blockRecipes = new ArrayList<>(buffer.readCollection(ArrayList::new, BlockConversionRecipe::readFromBuffer));
        List<EntityConversionRecipe<?>> entityRecipes = new ArrayList<>(buffer.readCollection(ArrayList::new, EntityConversionRecipe::readFromBuffer));
        return new ClientboundUpdateRecipesMessage(blockRecipes, entityRecipes);
    }

    public void handle(PlayPayloadContext context) {
        context.workHandler().execute(() -> ConversionRecipeManager.INSTANCE.replaceAllRecipes(blockRecipes, entityRecipes));
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
