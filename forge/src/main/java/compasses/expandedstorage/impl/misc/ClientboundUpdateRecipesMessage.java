package compasses.expandedstorage.impl.misc;

import compasses.expandedstorage.impl.recipe.BlockConversionRecipe;
import compasses.expandedstorage.impl.recipe.ConversionRecipeManager;
import compasses.expandedstorage.impl.recipe.EntityConversionRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.ArrayList;
import java.util.List;

public class ClientboundUpdateRecipesMessage implements CustomPacketPayload {
    public static final Type<ClientboundUpdateRecipesMessage> TYPE = new Type<>(Utils.id("update_conversion_recipes"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateRecipesMessage> CODEC = StreamCodec.of(ClientboundUpdateRecipesMessage::encode, ClientboundUpdateRecipesMessage::decode);
    private final List<BlockConversionRecipe<?>> blockRecipes;
    private final List<EntityConversionRecipe<?>> entityRecipes;

    public ClientboundUpdateRecipesMessage(List<BlockConversionRecipe<?>> blockRecipes, List<EntityConversionRecipe<?>> entityRecipes) {
        this.blockRecipes = blockRecipes;
        this.entityRecipes = entityRecipes;
    }

    public static void encode(FriendlyByteBuf buffer, ClientboundUpdateRecipesMessage message) {
        buffer.writeCollection(message.blockRecipes, (b, recipe) -> recipe.writeToBuffer(b));
        buffer.writeCollection(message.entityRecipes, (b, recipe) -> recipe.writeToBuffer(b));
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
    public Type<ClientboundUpdateRecipesMessage> type() {
        return TYPE;
    }
}
