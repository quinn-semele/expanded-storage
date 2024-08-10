package compasses.expandedstorage.impl.block.misc;

import compasses.expandedstorage.impl.block.strategies.Lockable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.LockCode;

public class BasicLockable implements Lockable {
    LockCode lock = LockCode.NO_LOCK;

    @Override
    public void writeLock(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        lock.addToTag(tag);
    }

    @Override
    public void readLock(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        lock = LockCode.fromTag(tag);
    }

    @Override
    public boolean canPlayerOpenLock(ServerPlayer player) {
        return lock == LockCode.NO_LOCK || !player.isSpectator() && lock.unlocksWith(player.getMainHandItem());
    }
}
