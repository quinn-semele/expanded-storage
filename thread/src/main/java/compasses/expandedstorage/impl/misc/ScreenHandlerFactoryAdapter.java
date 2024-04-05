package compasses.expandedstorage.impl.misc;

import compasses.expandedstorage.impl.inventory.handler.AbstractHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

// Note: MenuProvider is important without it dependants will crash in development when opening inventories.
//       Yes java / mc modding is blessed.
public final class ScreenHandlerFactoryAdapter implements ExtendedScreenHandlerFactory, MenuProvider {
    private final ResourceLocation forcedScreenType;
    private final Component title;
    private final Container inventory;

    public ScreenHandlerFactoryAdapter(Component title, Container inventory, ResourceLocation forcedScreenType) {
        this.title = title;
        this.inventory = inventory;
        this.forcedScreenType = forcedScreenType;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buffer) {
        buffer.writeInt(inventory.getContainerSize());
        buffer.writeNullable(forcedScreenType, FriendlyByteBuf::writeResourceLocation);
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return title;
    }

    @NotNull
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new AbstractHandler(syncId, inventory, playerInventory, forcedScreenType);
    }
}
