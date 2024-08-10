package compasses.expandedstorage.impl.block.strategies;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public interface Lockable {
    void writeLock(CompoundTag tag, HolderLookup.Provider lookupProvider);

    void readLock(CompoundTag tag, HolderLookup.Provider lookupProvider);

    boolean canPlayerOpenLock(ServerPlayer player);
}
