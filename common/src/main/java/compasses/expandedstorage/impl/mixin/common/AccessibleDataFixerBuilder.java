package compasses.expandedstorage.impl.mixin.common;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import compasses.expandedstorage.impl.misc.DataFixerBuilderAccess;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = DataFixerBuilder.class, remap = false)
public class AccessibleDataFixerBuilder implements DataFixerBuilderAccess {
    @Final
    @Shadow(remap = false)
    private Int2ObjectSortedMap<Schema> schemas;

    @Override
    public Schema expandedstorage$getSchema(int version, int subVersion) {
        return schemas.get(version * 10 + subVersion);
    }
}
