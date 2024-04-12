package compasses.expandedstorage.impl.misc;

import compasses.expandedstorage.impl.item.MutatorData;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ESDataComponents {
    public static final DataComponentType<MutatorData> MUTATOR_DATA = DataComponentType
            .<MutatorData>builder()
            .persistent(MutatorData.CODEC.codec())
            .networkSynchronized(MutatorData.STREAM_CODEC)
            .build();

    public static void register() {
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Utils.id("mutator_data"), MUTATOR_DATA);
    }
}
