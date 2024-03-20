package compasses.expandedstorage.impl.block.entity;

import compasses.expandedstorage.impl.block.OpenableBlock;
import compasses.expandedstorage.impl.block.entity.extendable.ExposedInventoryBlockEntity;
import compasses.expandedstorage.impl.block.entity.extendable.OpenableBlockEntity;
import compasses.expandedstorage.impl.block.strategies.ItemAccess;
import compasses.expandedstorage.impl.block.strategies.Lockable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;
import java.util.function.Supplier;

public final class MiniStorageBlockEntity extends ExposedInventoryBlockEntity {
    public MiniStorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, ResourceLocation blockId,
                                  Function<OpenableBlockEntity, ItemAccess> access, Supplier<Lockable> lockable) {
        super(type, pos, state, blockId, ((OpenableBlock) state.getBlock()).getInventoryTitle(), 1);
        this.setItemAccess(access.apply(this));
        this.setLockable(lockable.get());
    }
}
