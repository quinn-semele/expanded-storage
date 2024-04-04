package compasses.expandedstorage.impl.block.misc;

import compasses.expandedstorage.impl.block.entity.extendable.OpenableBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;

import java.util.List;

public final class ChestItemAccess extends GenericItemAccess implements DoubleItemAccess<Storage<ItemVariant>> {
    private Storage<ItemVariant> cache;

    public ChestItemAccess(OpenableBlockEntity entity) {
        super(entity);
    }

    @Override
    public Storage<ItemVariant> get() {
        return this.hasCachedAccess() ? cache : this.getSingle();
    }

    @Override
    public Storage<ItemVariant> getSingle() {
        return super.get();
    }

    @Override
    public void setOther(DoubleItemAccess<Storage<ItemVariant>> other) {
        cache = other == null ? null : new CombinedStorage<>(List.of(this.getSingle(), other.getSingle()));
    }

    @Override
    public boolean hasCachedAccess() {
        return cache != null;
    }
}
