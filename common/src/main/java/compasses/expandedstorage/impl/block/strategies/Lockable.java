package compasses.expandedstorage.impl.block.strategies;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public interface Lockable {
    void writeLock(CompoundTag tag);

    void readLock(CompoundTag tag);

    boolean canPlayerOpenLock(ServerPlayer player);
}
