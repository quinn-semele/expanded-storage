package compasses.expandedstorage.impl.block.misc;

import compasses.expandedstorage.impl.block.entity.extendable.OpenableBlockEntity;
import compasses.expandedstorage.impl.block.strategies.ItemAccess;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class GenericItemAccess implements ItemAccess<IItemHandlerModifiable> {
    private final OpenableBlockEntity entity;
    private IItemHandlerModifiable handler = null;

    public GenericItemAccess(OpenableBlockEntity entity) {
        this.entity = entity;
    }

    @Override
    public IItemHandlerModifiable get() {
        if (handler == null) {
            handler = new InvWrapper(entity.getInventory());
        }

        return handler;
    }
}
