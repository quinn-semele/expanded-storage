package compasses.expandedstorage.impl.block.misc;

import compasses.expandedstorage.impl.block.entity.extendable.OpenableBlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public final class ChestItemAccess extends GenericItemAccess implements DoubleItemAccess<IItemHandlerModifiable> {
    private IItemHandlerModifiable cache;

    public ChestItemAccess(OpenableBlockEntity entity) {
        super(entity);
    }

    @Override
    public IItemHandlerModifiable get() {
        return this.hasCachedAccess() ? cache : this.getSingle();
    }

    @Override
    public IItemHandlerModifiable getSingle() {
        return super.get();
    }

    @Override
    public void setOther(DoubleItemAccess<IItemHandlerModifiable> other) {
        cache = other == null ? null : new CombinedInvWrapper(this.getSingle(), other.getSingle());
    }

    @Override
    public boolean hasCachedAccess() {
        return cache != null;
    }
}
