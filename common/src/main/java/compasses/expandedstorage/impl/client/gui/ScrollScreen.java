package compasses.expandedstorage.impl.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import compasses.expandedstorage.impl.CommonClient;
import compasses.expandedstorage.impl.inventory.handler.ToggleableSlot;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.client.function.ScreenSize;
import compasses.expandedstorage.impl.inventory.handler.AbstractHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ScrollScreen extends AbstractScreen {
    private static final int THUMB_WIDTH = 12, THUMB_HEIGHT = 15;
    private final int totalRows;
    private final boolean scrollingUnrestricted;
    private boolean isDragging, blankAreaVisible;
    private int topRow, scrollYOffset, thumbY, blankSlots;
    private @Nullable TexturedRect blankArea;

    public ScrollScreen(AbstractHandler handler, Inventory playerInventory, Component title, ScreenSize screenSize) {
        super(handler, playerInventory, title, screenSize);

        this.initializeSlots(playerInventory);

        totalRows = Mth.ceil(((double) totalSlots) / inventoryWidth);
        imageWidth = Utils.CONTAINER_PADDING_LDR + Utils.SLOT_SIZE * inventoryWidth + Utils.CONTAINER_PADDING_LDR; // 22 - 4 is scrollbar width - overlap
        imageHeight = Utils.CONTAINER_HEADER_HEIGHT + Utils.SLOT_SIZE * inventoryHeight + 14 + Utils.SLOT_SIZE * 3 + 4 + Utils.SLOT_SIZE + Utils.CONTAINER_PADDING_LDR;
        scrollingUnrestricted = CommonClient.platformHelper().configWrapper().isScrollingUnrestricted();
    }

    public static ScreenSize retrieveScreenSize(int slots, int scaledWidth, int scaledHeight) {
        if (CommonClient.platformHelper().configWrapper().fitVanillaConstraints()) {
            return new ScreenSize(9, 6);
        }

        ArrayList<ScreenSize> options = new ArrayList<>();
        options.add(new ScreenSize(9, 6));
        if (slots > 90) {
            options.add(new ScreenSize(15, 6));
        }
        if (scaledHeight >= 276) {
            if (slots > 54) {
                options.add(new ScreenSize(9, 9));
            }
            if (scaledWidth >= 248 && slots > 81) {
                options.add(new ScreenSize(12, 9));
            }
            if (scaledWidth >= 302 && slots > 108) {
                options.add(new ScreenSize(15, 9));
            }
            if (scaledWidth >= 356 && slots > 135) {
                options.add(new ScreenSize(18, 9));
            }
        }
        if (scaledHeight >= 330 && scaledWidth >= 356 && slots > 162) {
            options.add(new ScreenSize(18, 12));
        }
        if (scaledHeight >= 384 && scaledWidth >= 356 && slots > 216) {
            options.add(new ScreenSize(18, 15));
        }

        return options.get(options.size() - 1);
    }

    private void initializeSlots(Inventory playerInventory) {
        for (int i = 0; i < totalSlots; i++) {
            int slotXPos = i % inventoryWidth;
            int slotYPos = Mth.ceil((((double) (i - slotXPos)) / inventoryWidth));
            int realYPos = slotYPos >= inventoryHeight ? -2000 : slotYPos * Utils.SLOT_SIZE + Utils.SLOT_SIZE;
            menu.addClientSlot(new ToggleableSlot(menu.getInventory(), i, slotXPos * Utils.SLOT_SIZE + 8, realYPos, realYPos != -2000));
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

    @Override
    protected void init() {
        super.init();
        leftPos = (width - (imageWidth + 22 - 4)) / 2;
        isDragging = false;

        int remainderSlots = (totalSlots % inventoryWidth);
        if (remainderSlots > 0) {
            blankSlots = inventoryWidth - remainderSlots;
            int xRight = leftPos + Utils.CONTAINER_PADDING_LDR + inventoryWidth * Utils.SLOT_SIZE;
            int yTop = topPos + Utils.CONTAINER_HEADER_HEIGHT + (inventoryHeight - 1) * Utils.SLOT_SIZE;
            int width = blankSlots * Utils.SLOT_SIZE;
            blankArea = new TexturedRect(textureLocation, xRight - width, yTop, width, Utils.SLOT_SIZE, Utils.CONTAINER_PADDING_LDR, imageHeight, textureWidth, textureHeight);
            blankAreaVisible = topRow == (totalRows - inventoryHeight);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(textureLocation, leftPos, topPos, 0, 0, imageWidth, imageHeight, textureWidth, textureHeight);

        int containerSlotsHeight = inventoryHeight * 18;
        int scrollbarHeight = containerSlotsHeight + (inventoryWidth > 9 ? 34 : 24);
        graphics.blit(textureLocation, leftPos + imageWidth - 4, topPos, imageWidth, 0, 22, scrollbarHeight, textureWidth, textureHeight);

        graphics.blit(textureLocation, leftPos + imageWidth - 2, topPos + Utils.CONTAINER_HEADER_HEIGHT + 1 + thumbY, imageWidth, scrollbarHeight, ScrollScreen.THUMB_WIDTH, ScrollScreen.THUMB_HEIGHT, textureWidth, textureHeight);

        if (blankArea != null && blankAreaVisible) {
            blankArea.render(graphics);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, 8, 6, 0x404040, false);
        graphics.drawString(font, playerInventoryTitle, 8, imageHeight - 96 + 2, 0x404040, false);
    }

    private boolean isMouseOverTrack(double mouseX, double mouseY) {
        boolean xCheck = leftPos + imageWidth - 2 <= mouseX && mouseX <= leftPos + imageWidth - 2 + ScrollScreen.THUMB_WIDTH;
        int scrollbarStart = topPos + Utils.CONTAINER_HEADER_HEIGHT + 1;
        return xCheck && scrollbarStart <= mouseY && mouseY <= scrollbarStart + inventoryHeight * 18 - 2;
    }

    private boolean isMouseOverThumb(double mouseX, double mouseY) {
        boolean xCheck = leftPos + imageWidth - 2 <= mouseX && mouseX <= leftPos + imageWidth - 2 + ScrollScreen.THUMB_WIDTH;
        double correctedThumbY = topPos + Utils.CONTAINER_HEADER_HEIGHT + 1 + thumbY;
        return xCheck && correctedThumbY <= mouseY && mouseY <= correctedThumbY + ScrollScreen.THUMB_HEIGHT;
    }

    private boolean isMouseOverScrollbar(double mouseX, double mouseY, int left, int top) {
        return mouseX > left + imageWidth - 4 && mouseX <= left + imageWidth + 22 &&
                mouseY >= top && mouseY < top + Utils.CONTAINER_HEADER_HEIGHT + inventoryHeight * 18 + (inventoryWidth > 9 ? 10 : 0) + Utils.CONTAINER_PADDING_LDR;
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
        if (inventoryWidth > 9 && mouseY >= top + Utils.CONTAINER_HEADER_HEIGHT + inventoryHeight * Utils.SLOT_SIZE + Utils.CONTAINER_HEADER_HEIGHT) {
            int outsideRegion = (imageWidth - (Utils.CONTAINER_PADDING_LDR + 9 * Utils.SLOT_SIZE + Utils.CONTAINER_PADDING_LDR)) / 2;
            if (mouseX < left + outsideRegion || mouseX > left + imageWidth - outsideRegion) {
                return true;
            }
        }
        return super.hasClickedOutside(mouseX, mouseY, left, top, button) && !this.isMouseOverScrollbar(mouseX, mouseY, leftPos, topPos);
    }

    @Override
    protected boolean handleKeyPress(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
            if (topRow != totalRows - inventoryHeight) {
                if (hasShiftDown()) {
                    this.setTopRowAndMoveThumb(topRow, Math.min(topRow + inventoryHeight, totalRows - inventoryHeight));
                } else {
                    this.setTopRowAndMoveThumb(topRow, topRow + 1);
                }
            }
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_PAGE_UP) {
            if (topRow != 0) {
                if (hasShiftDown()) {
                    this.setTopRowAndMoveThumb(topRow, Math.max(topRow - inventoryHeight, 0));
                } else {
                    this.setTopRowAndMoveThumb(topRow, topRow - 1);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isMouseOverThumb(mouseX, mouseY) && button == 0) {
            scrollYOffset = (int) mouseY - thumbY;
            isDragging = true;
        } else if (this.isMouseOverTrack(mouseX, mouseY) && button == 0) {
            this.updateThumbPosition(mouseY - Utils.CONTAINER_HEADER_HEIGHT - 1 - topPos);
            this.snapThumbToGradation();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging) {
            this.updateThumbPosition(mouseY - scrollYOffset);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isDragging) {
            isDragging = false;
            this.snapThumbToGradation();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void snapThumbToGradation() {
        thumbY = (int) (((double) topRow / (totalRows - inventoryHeight)) * (inventoryHeight * Utils.SLOT_SIZE - 2 - ScrollScreen.THUMB_HEIGHT));
    }

    private void updateThumbPosition(double adjustedMouseY) {
        thumbY = (int) Math.min(Math.max(adjustedMouseY, 0), inventoryHeight * Utils.SLOT_SIZE - 2 - ScrollScreen.THUMB_HEIGHT);
        int row = (int) Math.round(((double) thumbY) / (inventoryHeight * Utils.SLOT_SIZE - 2 - ScrollScreen.THUMB_HEIGHT) * (totalRows - inventoryHeight));
        this.setTopRow(topRow, row);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaY) {
        if (super.mouseScrolled(mouseX, mouseY, deltaY)) {
            return true;
        }
        if (scrollingUnrestricted || this.isMouseOverTrack(mouseX, mouseY)) {
            int newTop;
            if (deltaY < 0) {
                newTop = Math.min(topRow + (hasShiftDown() ? inventoryHeight : 1), totalRows - inventoryHeight);
            } else {
                newTop = Math.max(topRow - (hasShiftDown() ? inventoryHeight : 1), 0);
            }
            this.setTopRowAndMoveThumb(topRow, newTop);
            return true;
        }
        return false;
    }

    private void setTopRowAndMoveThumb(int oldTopRow, int newTopRow) {
        this.setTopRow(oldTopRow, newTopRow);
        this.snapThumbToGradation();
    }

    private void setTopRow(int oldTopRow, int newTopRow) {
        if (oldTopRow == newTopRow) {
            return;
        }
        topRow = newTopRow;
        blankAreaVisible = topRow == (totalRows - inventoryHeight);
        int delta = newTopRow - oldTopRow;
        int rows = Math.abs(delta);
        if (rows < inventoryHeight) {
            int setAmount = rows * inventoryWidth;
            int movableAmount = (inventoryHeight - rows) * inventoryWidth;
            if (delta > 0) {
                int setOutBegin = oldTopRow * inventoryWidth;
                int movableBegin = newTopRow * inventoryWidth;
                int setInBegin = movableBegin + movableAmount;
                menu.setSlotRange(setOutBegin, setOutBegin + setAmount, index -> -2000);
                menu.moveSlotRange(movableBegin, setInBegin, -18 * rows);
                menu.setSlotRange(setInBegin, Math.min(setInBegin + setAmount, totalSlots),
                        index -> 18 * Math.floorDiv(index - movableBegin + inventoryWidth, inventoryWidth));
            } else {
                int setInBegin = newTopRow * inventoryWidth;
                int movableBegin = oldTopRow * inventoryWidth;
                int setOutBegin = movableBegin + movableAmount;
                menu.setSlotRange(setInBegin, setInBegin + setAmount,
                        index -> 18 * Math.floorDiv(index - setInBegin + inventoryWidth, inventoryWidth));
                menu.moveSlotRange(movableBegin, setOutBegin, 18 * rows);
                menu.setSlotRange(setOutBegin, Math.min(setOutBegin + setAmount, totalSlots), index -> -2000);
            }
        } else {
            int oldMin = oldTopRow * inventoryWidth;
            menu.setSlotRange(oldMin, Math.min(oldMin + inventoryWidth * inventoryHeight, totalSlots), index -> -2000);
            int newMin = newTopRow * inventoryWidth;
            menu.setSlotRange(newMin, newMin + inventoryWidth * inventoryHeight - (blankAreaVisible ? blankSlots : 0),
                    index -> 18 + 18 * Math.floorDiv(index - newMin, inventoryWidth));
        }
    }

    @NotNull
    @Override
    public List<Rect2i> getExclusionZones() {
        int height = Utils.CONTAINER_HEADER_HEIGHT + inventoryHeight * Utils.SLOT_SIZE + (inventoryWidth > 9 ? 10 : 0) + Utils.CONTAINER_PADDING_LDR;
        return Collections.singletonList(new Rect2i(leftPos + imageWidth, topPos, 22 - 4, height)); // 22 - 4 is scrollbar width minus overlap
    }
}
