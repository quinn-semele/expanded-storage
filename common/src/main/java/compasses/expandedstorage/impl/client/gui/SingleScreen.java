package compasses.expandedstorage.impl.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.client.function.ScreenSize;
import compasses.expandedstorage.impl.inventory.handler.AbstractHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.HashSet;
import java.util.Set;

public final class SingleScreen extends AbstractScreen {
    private final Set<TexturedRect> blankArea = new HashSet<>();
    private final int blankSlots;

    public SingleScreen(AbstractHandler handler, Inventory playerInventory, Component title, ScreenSize screenSize) {
        super(handler, playerInventory, title, screenSize);

        this.initializeSlots(playerInventory);

        blankSlots = (inventoryWidth * inventoryHeight) - totalSlots;

        imageWidth = Utils.CONTAINER_PADDING_LDR + Utils.SLOT_SIZE * inventoryWidth + Utils.CONTAINER_PADDING_LDR;
        imageHeight = Utils.CONTAINER_HEADER_HEIGHT + Utils.SLOT_SIZE * inventoryHeight + 14 + Utils.SLOT_SIZE * 3 + 4 + Utils.SLOT_SIZE + Utils.CONTAINER_PADDING_LDR;
    }

    public static ScreenSize retrieveScreenSize(int slots, int scaledWidth, int scaledHeight) {
        int width;

        if (slots <= 81) {
            width = 9;
        } else if (slots <= 108) {
            width = 12;
        } else if (slots <= 135) {
            width = 15;
        } else if (slots <= 270) {
            width = 18;
        } else {
            return null;
        }

        int height;

        if (slots <= 27) {
            height = 3;
        } else if (slots <= 45) {
            height = 5;
        } else if (slots <= 54) {
            height = 6;
        } else if (slots <= 162) {
            height = 9;
        } else if (slots <= 216) {
            height = 12;
        } else /* if (slots <= 270) */ {
            height = 15;
        } // slots is guaranteed to be 270 or below when getting width.

        return new ScreenSize(width, height);
    }

    @Override
    protected void init() {
        super.init();
        if (blankSlots > 0) {
            blankArea.clear();
            int rows = Math.floorDiv(blankSlots, inventoryWidth);
            int remainder = (blankSlots - inventoryWidth * rows);
            int yTop = topPos + Utils.CONTAINER_HEADER_HEIGHT + (inventoryHeight - 1) * Utils.SLOT_SIZE;
            int xLeft = leftPos + Utils.CONTAINER_PADDING_LDR;
            for (int i = 0; i < rows; i++) {
                blankArea.add(new TexturedRect(textureLocation, xLeft, yTop, inventoryWidth * Utils.SLOT_SIZE, Utils.SLOT_SIZE,
                        Utils.CONTAINER_PADDING_LDR, imageHeight, textureWidth, textureHeight));
                yTop -= Utils.SLOT_SIZE;
            }
            if (remainder > 0) {
                int xRight = leftPos + Utils.CONTAINER_PADDING_LDR + inventoryWidth * Utils.SLOT_SIZE;
                int width = remainder * Utils.SLOT_SIZE;
                blankArea.add(new TexturedRect(textureLocation, xRight - width, yTop, width, Utils.SLOT_SIZE,
                        Utils.CONTAINER_PADDING_LDR, imageHeight, textureWidth, textureHeight));
            }
        }
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
        if (inventoryWidth > 9 && mouseY >= top + Utils.CONTAINER_HEADER_HEIGHT + inventoryHeight * Utils.SLOT_SIZE + Utils.CONTAINER_HEADER_HEIGHT) {
            int outsideRegion = (imageWidth - (Utils.CONTAINER_PADDING_LDR + 9 * Utils.SLOT_SIZE + Utils.CONTAINER_PADDING_LDR)) / 2;
            if (mouseX < left + outsideRegion || mouseX > left + imageWidth - outsideRegion) {
                return true;
            }
        }
        return super.hasClickedOutside(mouseX, mouseY, left, top, button);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(textureLocation, leftPos, topPos, 0, 0, imageWidth, imageHeight, textureWidth, textureHeight);
        blankArea.forEach(image -> image.render(graphics));
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, 8, 6, 0x404040, false);
        graphics.drawString(font, playerInventoryTitle, 8, imageHeight - 96 + 2, 0x404040, false);
    }

    private void initializeSlots(Inventory playerInventory) {
        for (int i = 0; i < menu.getInventory().getContainerSize(); i++) {
            int x = i % inventoryWidth;
            int y = (i - x) / inventoryWidth;
            menu.addClientSlot(new Slot(menu.getInventory(), i, x * Utils.SLOT_SIZE + 8, y * Utils.SLOT_SIZE + Utils.SLOT_SIZE));
        }
        int left = (inventoryWidth * Utils.SLOT_SIZE + 14) / 2 - 80;
        int top = Utils.SLOT_SIZE + 14 + (inventoryHeight * Utils.SLOT_SIZE);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                menu.addClientSlot(new Slot(playerInventory, y * 9 + x + 9, left + Utils.SLOT_SIZE * x, top + y * Utils.SLOT_SIZE));
            }
        }
        for (int x = 0; x < 9; x++) {
            menu.addClientSlot(new Slot(playerInventory, x, left + Utils.SLOT_SIZE * x, top + 58));
        }
    }
}
