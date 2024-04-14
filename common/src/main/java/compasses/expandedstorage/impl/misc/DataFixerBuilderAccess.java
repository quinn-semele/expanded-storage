package compasses.expandedstorage.impl.misc;

import com.mojang.datafixers.schemas.Schema;

public interface DataFixerBuilderAccess {
    Schema expandedstorage$getSchema(int version, int subversion);
}
