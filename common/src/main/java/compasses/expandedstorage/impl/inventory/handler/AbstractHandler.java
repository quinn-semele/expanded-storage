package compasses.expandedstorage.impl.inventory.handler;

import compasses.expandedstorage.impl.CommonMain;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntUnaryOperator;

public final class AbstractHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ResourceLocation forcedScreenType;

    public AbstractHandler(int syncId, Container inventory, Inventory playerInventory, ResourceLocation forcedScreenType) {
        super(CommonMain.platformHelper().getScreenHandlerType(), syncId);
        this.inventory = inventory;
        this.forcedScreenType = forcedScreenType;
        inventory.startOpen(playerInventory.player);
        if (playerInventory.player instanceof ServerPlayer) {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                this.addSlot(new Slot(inventory, i, i * Utils.SLOT_SIZE, 0));
            }
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 9; x++) {
                    this.addSlot(new Slot(playerInventory, y * 9 + x + 9, Utils.SLOT_SIZE * x, y * Utils.SLOT_SIZE));
                }
            }
            for (int i = 0; i < 9; i++) {
                this.addSlot(new Slot(playerInventory, i, i * Utils.SLOT_SIZE, 2 * Utils.SLOT_SIZE));
            }
        }
    }

    // Client only
    public static AbstractHandler createClientMenu(int syncId, Inventory playerInventory, int slots, ResourceLocation forcedScreenType) {
        return new AbstractHandler(syncId, new SimpleContainer(slots), playerInventory, forcedScreenType);
    }

    @Override
    public boolean stillValid(Player player) {
        return inventory.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        inventory.stopOpen(player);
    }

    // Public API, required for mods to check if blocks should be considered open
    public Container getInventory() {
        return inventory;
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack newStack = slot.getItem();
            originalStack = newStack.copy();
            if (index < inventory.getContainerSize()) {
                if (!this.moveItemStackTo(newStack, inventory.getContainerSize(), inventory.getContainerSize() + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(newStack, 0, inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (newStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return originalStack;
    }

    // Below are client only methods
    public void resetSlotPositions(boolean createSlots, int menuWidth, int menuHeight) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            int slotXPos = i % menuWidth;
            int slotYPos = Mth.ceil((((double) (i - slotXPos)) / menuWidth));
            int realYPos = slotYPos >= menuHeight ? (Utils.SLOT_SIZE * (slotYPos % menuHeight)) - 2000 : slotYPos * Utils.SLOT_SIZE;
            if (createSlots) {
                this.addSlot(new ToggleableSlot(inventory, i, slotXPos * Utils.SLOT_SIZE + 8, realYPos + Utils.SLOT_SIZE, slotYPos < menuHeight));
            } else {
                slots.get(i).y = realYPos + Utils.SLOT_SIZE;
            }
        }
    }

    public void moveSlotRange(int minSlotIndex, int maxSlotIndex, int yDifference) {
        for (int i = minSlotIndex; i < maxSlotIndex; i++) {
            ToggleableSlot slot = (ToggleableSlot) slots.get(i);
            slot.y += yDifference;
            if (yDifference == -2000 || yDifference == 2000) {
                slot.toggleActive();
            }
        }
    }

    public void setSlotRange(int minSlotIndex, int maxSlotIndex, IntUnaryOperator yMutator) {
        for (int i = minSlotIndex; i < maxSlotIndex; i++) {
            ToggleableSlot slot = (ToggleableSlot) slots.get(i);
            int newY = yMutator.applyAsInt(i);
            if (newY - slot.y > 1000 || slot.y - newY > 1000) {
                ((ToggleableSlot) slots.get(i)).toggleActive();
            }
            slot.y = newY;
        }
    }

    public void clearSlots() {
        this.slots.clear();
        this.remoteSlots.clear();
        this.lastSlots.clear();
    }

    public void addClientSlot(Slot slot) {
        this.addSlot(slot);
    }

    public ResourceLocation getForcedScreenType() {
        return forcedScreenType;
    }
}
