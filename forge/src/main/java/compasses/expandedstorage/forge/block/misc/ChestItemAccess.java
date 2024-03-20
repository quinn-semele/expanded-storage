package compasses.expandedstorage.forge.block.misc;

import compasses.expandedstorage.impl.block.entity.extendable.OpenableBlockEntity;
import compasses.expandedstorage.impl.block.misc.DoubleItemAccess;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public final class ChestItemAccess extends GenericItemAccess implements DoubleItemAccess {
    private IItemHandlerModifiable cache;

    public ChestItemAccess(OpenableBlockEntity entity) {
        super(entity);
    }

    @Override
    public Object get() {
        return this.hasCachedAccess() ? cache : this.getSingle();
    }

    @Override
    public Object getSingle() {
        return super.get();
    }

    @Override
    public void setOther(DoubleItemAccess other) {
        cache = other == null ? null : new CombinedInvWrapper((IItemHandlerModifiable) this.getSingle(), (IItemHandlerModifiable) other.getSingle());
    }

    @Override
    public boolean hasCachedAccess() {
        return cache != null;
    }
}
