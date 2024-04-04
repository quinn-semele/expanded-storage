package compasses.expandedstorage.impl.block.misc;

import compasses.expandedstorage.impl.block.strategies.ItemAccess;

public interface DoubleItemAccess<T> extends ItemAccess<T> {
    T getSingle();

    void setOther(DoubleItemAccess<T> other);

    boolean hasCachedAccess();
}
