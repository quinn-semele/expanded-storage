package compasses.expandedstorage.impl;

import compasses.expandedstorage.impl.misc.CommonPlatformHelper;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.recipe.BlockConversionRecipe;
import compasses.expandedstorage.impl.recipe.EntityConversionRecipe;
import compasses.expandedstorage.impl.misc.ScreenHandlerFactoryAdapter;
import compasses.expandedstorage.impl.inventory.handler.AbstractHandler;
import compasses.expandedstorage.impl.registration.ModBlocks;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThreadCommonHelper implements CommonPlatformHelper {
    private final ExtendedScreenHandlerType<AbstractHandler> menuType;
    private final TagKey<Block> woodenChestTag = TagKey.create(Registry.BLOCK.key(), new ResourceLocation("c", "wooden_chests"));
    private MinecraftServer minecraftServer;

    {
        menuType = Registry.register(Registry.MENU, Utils.HANDLER_TYPE_ID, new ExtendedScreenHandlerType<>(AbstractHandler::createClientMenu));
    }

    @Override
    public MenuType<AbstractHandler> getScreenHandlerType() {
        return menuType;
    }

    @Override
    public void openScreenHandler(ServerPlayer player, Container inventory, Component title, ResourceLocation forcedScreenType) {
        player.openMenu(new ScreenHandlerFactoryAdapter(title, inventory, forcedScreenType));
    }

    @Override
    public void sendConversionRecipesToClient(@Nullable ServerPlayer target, List<BlockConversionRecipe<?>> blockRecipes, List<EntityConversionRecipe<?>> entityRecipes) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeCollection(blockRecipes, (b, recipe) -> recipe.writeToBuffer(b));
        buffer.writeCollection(entityRecipes, (b, recipe) -> recipe.writeToBuffer(b));
        if (target == null) {
            for (ServerPlayer player : minecraftServer.getPlayerList().getPlayers()) {
                ServerPlayNetworking.send(player, ThreadMain.UPDATE_RECIPES_ID, buffer);
            }
        } else {
            ServerPlayNetworking.send(target, ThreadMain.UPDATE_RECIPES_ID, buffer);
        }
    }

    @Override
    public boolean canDestroyBamboo(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    @Override
    public boolean isWoodenChest(BlockState state) {
        return state.is(ModBlocks.OLD_WOOD_CHEST) || state.is(woodenChestTag);
    }

    public void setServerInstance(MinecraftServer server) {
        this.minecraftServer = server;
    }
}
