package compasses.expandedstorage.impl.block.entity;

import compasses.expandedstorage.impl.block.OpenableBlock;
import compasses.expandedstorage.impl.block.entity.extendable.InventoryBlockEntity;
import compasses.expandedstorage.impl.block.entity.extendable.OpenableBlockEntity;
import compasses.expandedstorage.impl.block.misc.DoubleItemAccess;
import compasses.expandedstorage.impl.block.strategies.ItemAccess;
import compasses.expandedstorage.impl.block.strategies.Lockable;
import compasses.expandedstorage.impl.inventory.VariableSidedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;
import java.util.function.Supplier;

public class OldChestBlockEntity extends InventoryBlockEntity {
    WorldlyContainer cachedDoubleInventory = null;

    public OldChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, ResourceLocation blockId,
                               Function<OpenableBlockEntity, ItemAccess> access, Supplier<Lockable> lockable) {
        super(type, pos, state, blockId, ((OpenableBlock) state.getBlock()).getInventoryTitle(), ((OpenableBlock) state.getBlock()).getSlotCount());
        this.setItemAccess(access.apply(this));
        this.setLockable(lockable.get());
    }

    public void invalidateDoubleBlockCache() {
        cachedDoubleInventory = null;
        this.getItemAccess().setOther(null);
    }

    public WorldlyContainer getCachedDoubleInventory() {
        return cachedDoubleInventory;
    }

    public void setCachedDoubleInventory(OldChestBlockEntity other) {
        this.cachedDoubleInventory = VariableSidedInventory.of(this.getInventory(), other.getInventory());
    }

    @Override
    public DoubleItemAccess getItemAccess() {
        return (DoubleItemAccess) super.getItemAccess();
    }

}
