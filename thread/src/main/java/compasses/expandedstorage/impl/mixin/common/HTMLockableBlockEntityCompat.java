package compasses.expandedstorage.impl.mixin.common;

import com.github.fabricservertools.htm.HTMContainerLock;
import com.github.fabricservertools.htm.api.LockableObject;
import compasses.expandedstorage.impl.block.entity.extendable.OpenableBlockEntity;
import compasses.expandedstorage.impl.compat.htm.HTMLockable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("unused")
@Mixin(OpenableBlockEntity.class)
public abstract class HTMLockableBlockEntityCompat extends BlockEntity implements LockableObject {
    protected HTMLockableBlockEntityCompat(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public HTMContainerLock getLock() {
        return ((HTMLockable) expandedstorage$self().getLockable()).getLock();
    }

    @Override
    public void setLock(HTMContainerLock lock) {
        ((HTMLockable) expandedstorage$self().getLockable()).setLock(lock);
    }

    @Unique
    private OpenableBlockEntity expandedstorage$self() {
        //noinspection ConstantConditions
        return (OpenableBlockEntity) (Object) this;
    }
}
