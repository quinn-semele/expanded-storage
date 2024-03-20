package compasses.expandedstorage.impl.client.helpers;

import compasses.expandedstorage.impl.client.function.ScreenSize;
import compasses.expandedstorage.impl.client.gui.AbstractScreen;
import compasses.expandedstorage.impl.inventory.handler.AbstractHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public interface ScreenConstructor<T extends AbstractScreen> {
    @NotNull
    T createScreen(AbstractHandler handler, Inventory playerInventory, Component title, ScreenSize screenSize);
}
