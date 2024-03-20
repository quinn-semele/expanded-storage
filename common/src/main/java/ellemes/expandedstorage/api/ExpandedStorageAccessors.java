package ellemes.expandedstorage.api;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * @deprecated Use replacements under the compasses.expandedstorage.api package; for removal in minecraft 1.21.
 */
@SuppressWarnings({"unused", "removal"})
@Deprecated(forRemoval = true)
public final class ExpandedStorageAccessors {
    private ExpandedStorageAccessors() {
        throw new IllegalStateException("Should not be instantiated.");
    }

    /**
     * @return The chest type or empty if the state passed is not a chest block.
     */
    public static Optional<EsChestType> getChestType(BlockState state) {
        return compasses.expandedstorage.api.ExpandedStorageAccessors.getChestType(state).map(EsChestType::fromNewType);
    }

    /**
     * @return The direction to attached chest block or empty if the state passed is not a chest block or is a single chest.
     */
    public static Optional<Direction> getAttachedChestDirection(BlockState state) {
        return compasses.expandedstorage.api.ExpandedStorageAccessors.getAttachedChestDirection(state);
    }

    /**
     * @return A chest block of the specified type or empty if the passed in state is not a chest block.
     */
    public static Optional<BlockState> chestWithType(BlockState original, EsChestType type) {
        return compasses.expandedstorage.api.ExpandedStorageAccessors.chestWithType(original, type.toNewType());
    }
}
