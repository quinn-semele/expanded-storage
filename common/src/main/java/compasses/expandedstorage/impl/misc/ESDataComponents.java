package compasses.expandedstorage.impl.misc;

import compasses.expandedstorage.impl.item.MutationMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ESDataComponents {
    public static final DataComponentType<MutationMode> MUTATOR_MODE = DataComponentType
            .<MutationMode>builder()
            .persistent(MutationMode.CODEC)
            .networkSynchronized(MutationMode.STREAM_CODEC)
            .build();

    public static final DataComponentType<BlockPos> STORED_LOCATION = DataComponentType
            .<BlockPos>builder()
            .persistent(BlockPos.CODEC)
            .networkSynchronized(BlockPos.STREAM_CODEC)
            .build();

    public static void register() {
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Utils.id("mutator_tool_mode"), MUTATOR_MODE);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Utils.id("stored_location"), STORED_LOCATION);
    }
}
