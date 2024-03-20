package compasses.expandedstorage.impl.inventory.handler;

public interface InventorySlotFunction<T, U> {
    U apply(T inventory, int slot);
}
