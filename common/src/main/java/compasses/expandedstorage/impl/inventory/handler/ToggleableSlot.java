package compasses.expandedstorage.impl.inventory.handler;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class ToggleableSlot extends Slot {
    private boolean active;

    public ToggleableSlot(Container container, int slot, int x, int y, boolean active) {
        super(container, slot, x, y);
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void toggleActive() {
        active = !active;
    }
}
