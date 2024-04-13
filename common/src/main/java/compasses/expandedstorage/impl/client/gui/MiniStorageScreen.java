package compasses.expandedstorage.impl.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import compasses.expandedstorage.impl.client.function.ScreenSize;
import compasses.expandedstorage.impl.inventory.handler.AbstractHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public final class MiniStorageScreen extends AbstractScreen {
    public MiniStorageScreen(AbstractHandler handler, Inventory playerInventory, Component title, ScreenSize screenSize) {
        super(handler, playerInventory, title, screenSize);
        this.initializeSlots(playerInventory);
    }

    private void initializeSlots(Inventory playerInventory) {
        menu.addClientSlot(new Slot(menu.getInventory(), 0, 80, 35));
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                menu.addClientSlot(new Slot(playerInventory, 9 + x + y * 9, 8 + x * 18, 84 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            menu.addClientSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        graphics.blit(textureLocation, leftPos, topPos, 0, 0, imageWidth, imageHeight, textureWidth, textureHeight);
    }
}
