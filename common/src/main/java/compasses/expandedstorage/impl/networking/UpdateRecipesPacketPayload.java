package compasses.expandedstorage.impl.networking;

import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.recipe.BlockConversionRecipe;
import compasses.expandedstorage.impl.recipe.EntityConversionRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record UpdateRecipesPacketPayload(List<BlockConversionRecipe<?>> blockRecipes, List<EntityConversionRecipe<?>> entityRecipes) implements CustomPacketPayload {
    public static final Type<UpdateRecipesPacketPayload> TYPE = new Type<>(Utils.id("update_conversion_recipes"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateRecipesPacketPayload> CODEC = StreamCodec.of(UpdateRecipesPacketPayload::encode, UpdateRecipesPacketPayload::decode);

    private static UpdateRecipesPacketPayload decode(RegistryFriendlyByteBuf buffer) {
        List<BlockConversionRecipe<?>> blockRecipes = new ArrayList<>(buffer.readCollection(ArrayList::new, BlockConversionRecipe::readFromBuffer));
        List<EntityConversionRecipe<?>> entityRecipes = new ArrayList<>(buffer.readCollection(ArrayList::new, EntityConversionRecipe::readFromBuffer));

        return new UpdateRecipesPacketPayload(blockRecipes, entityRecipes);
    }

    private static void encode(RegistryFriendlyByteBuf buffer, UpdateRecipesPacketPayload payload) {
        buffer.writeCollection(payload.blockRecipes, (b, recipe) -> recipe.writeToBuffer(b));
        buffer.writeCollection(payload.entityRecipes, (b, recipe) -> recipe.writeToBuffer(b));
    }

    @NotNull
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
