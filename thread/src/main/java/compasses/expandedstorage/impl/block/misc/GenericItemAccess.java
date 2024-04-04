package compasses.expandedstorage.impl.block.misc;

import compasses.expandedstorage.impl.block.entity.extendable.OpenableBlockEntity;
import compasses.expandedstorage.impl.block.strategies.ItemAccess;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GenericItemAccess implements ItemAccess<SlottedStorage<ItemVariant>> {
    private final OpenableBlockEntity entity;
    @SuppressWarnings("UnstableApiUsage")
    private InventoryStorage storage = null;

    public GenericItemAccess(OpenableBlockEntity entity) {
        this.entity = entity;
    }

    @Override
    public SlottedStorage<ItemVariant> get() {
        if (storage == null) {
            NonNullList<ItemStack> items = entity.getItems();
            Container wrapped = entity.getInventory();
            Container transferApiInventory = new Container() {
                @Override
                public int getContainerSize() {
                    return wrapped.getContainerSize();
                }

                @Override
                public boolean isEmpty() {
                    return wrapped.isEmpty();
                }

                @NotNull
                @Override
                public ItemStack getItem(int slot) {
                    return wrapped.getItem(slot);
                }

                @NotNull
                @Override
                public ItemStack removeItem(int slot, int amount) {
                    return ContainerHelper.removeItem(items, slot, amount);
                }

                @NotNull
                @Override
                public ItemStack removeItemNoUpdate(int slot) {
                    return wrapped.removeItemNoUpdate(slot);
                }

                @Override
                public void setItem(int slot, ItemStack stack) {
                    items.set(slot, stack);
                    if (stack.getCount() > this.getMaxStackSize()) {
                        stack.setCount(this.getMaxStackSize());
                    }
                }

                @Override
                public void setChanged() {
                    wrapped.setChanged();
                }

                @Override
                public boolean stillValid(Player player) {
                    return wrapped.stillValid(player);
                }

                @Override
                public void clearContent() {
                    wrapped.clearContent();
                }

                @Override
                public void startOpen(Player player) {
                    wrapped.startOpen(player);
                }

                @Override
                public void stopOpen(Player player) {
                    wrapped.stopOpen(player);
                }
            };
            //noinspection UnstableApiUsage
            storage = InventoryStorage.of(transferApiInventory, null);
        }
        return storage;
    }
}
