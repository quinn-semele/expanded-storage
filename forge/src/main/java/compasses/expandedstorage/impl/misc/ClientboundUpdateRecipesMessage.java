package compasses.expandedstorage.impl.misc;

import compasses.expandedstorage.impl.recipe.BlockConversionRecipe;
import compasses.expandedstorage.impl.recipe.ConversionRecipeManager;
import compasses.expandedstorage.impl.recipe.EntityConversionRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ClientboundUpdateRecipesMessage {
    private final List<BlockConversionRecipe<?>> blockRecipes;
    private final List<EntityConversionRecipe<?>> entityRecipes;

    public ClientboundUpdateRecipesMessage(List<BlockConversionRecipe<?>> blockRecipes, List<EntityConversionRecipe<?>> entityRecipes) {
        this.blockRecipes = blockRecipes;
        this.entityRecipes = entityRecipes;
    }

    public static void encode(ClientboundUpdateRecipesMessage msg, FriendlyByteBuf buffer) {
        buffer.writeCollection(msg.blockRecipes, (b, recipe) -> recipe.writeToBuffer(b));
        buffer.writeCollection(msg.entityRecipes, (b, recipe) -> recipe.writeToBuffer(b));
    }

    public static ClientboundUpdateRecipesMessage decode(FriendlyByteBuf buffer) {
        List<BlockConversionRecipe<?>> blockRecipes = new ArrayList<>(buffer.readCollection(ArrayList::new, BlockConversionRecipe::readFromBuffer));
        List<EntityConversionRecipe<?>> entityRecipes = new ArrayList<>(buffer.readCollection(ArrayList::new, EntityConversionRecipe::readFromBuffer));
        return new ClientboundUpdateRecipesMessage(blockRecipes, entityRecipes);
    }

    public static void handle(ClientboundUpdateRecipesMessage msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ConversionRecipeManager.INSTANCE.replaceAllRecipes(msg.blockRecipes, msg.entityRecipes);
        });
    }
}
