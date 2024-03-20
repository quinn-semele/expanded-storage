package compasses.expandedstorage.impl.inventory;

import compasses.expandedstorage.impl.inventory.context.BaseContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public interface OpenableInventoryProvider<T extends BaseContext> {
    /**
     * Return the openable inventory, {@link OpenableInventories} can be used to supply more than one inventory.
     */
    OpenableInventory getOpenableInventory(T context);

    /**
     * Call back for running code when an inventory is initially opened, can be used to award opening stats.
     * Note: more context can be provided if needed, namely ServerWorld.
     */
    default void onInitialOpen(ServerPlayer player) {

    }

    /**
     * @return The screen type that should be used, null if the player can decide.
     */
    default ResourceLocation getForcedScreenType() {
        return null;
    }
}
