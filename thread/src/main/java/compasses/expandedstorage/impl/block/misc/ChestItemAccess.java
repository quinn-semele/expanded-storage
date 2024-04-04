package compasses.expandedstorage.impl.block.misc;

import compasses.expandedstorage.impl.block.entity.extendable.OpenableBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedSlottedStorage;

import java.util.List;

public final class ChestItemAccess extends GenericItemAccess implements DoubleItemAccess<SlottedStorage<ItemVariant>> {
    private SlottedStorage<ItemVariant> cache;

    public ChestItemAccess(OpenableBlockEntity entity) {
        super(entity);
    }

    @Override
    public SlottedStorage<ItemVariant> get() {
        return this.hasCachedAccess() ? cache : this.getSingle();
    }

    @Override
    public SlottedStorage<ItemVariant> getSingle() {
        return super.get();
    }

    @Override
    public void setOther(DoubleItemAccess<SlottedStorage<ItemVariant>> other) {
        cache = other == null ? null : new CombinedSlottedStorage<>(List.of(this.getSingle(), other.getSingle()));
    }

    @Override
    public boolean hasCachedAccess() {
        return cache != null;
    }
}
