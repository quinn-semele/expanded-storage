package compasses.expandedstorage.impl.block.misc;

import compasses.expandedstorage.impl.block.strategies.ItemAccess;

public interface DoubleItemAccess extends ItemAccess {
    Object getSingle();

    void setOther(DoubleItemAccess other);

    boolean hasCachedAccess();
}
