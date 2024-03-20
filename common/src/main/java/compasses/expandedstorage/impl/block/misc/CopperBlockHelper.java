package compasses.expandedstorage.impl.block.misc;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import compasses.expandedstorage.impl.registration.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class CopperBlockHelper {
    private static final BiMap<Block, Block> OXIDISATION_MAP =
            ImmutableBiMap.<Block, Block>builder()
                          .put(ModBlocks.COPPER_BARREL, ModBlocks.EXPOSED_COPPER_BARREL)
                          .put(ModBlocks.EXPOSED_COPPER_BARREL, ModBlocks.WEATHERED_COPPER_BARREL)
                          .put(ModBlocks.WEATHERED_COPPER_BARREL, ModBlocks.OXIDIZED_COPPER_BARREL)
                          .put(ModBlocks.COPPER_MINI_BARREL, ModBlocks.EXPOSED_COPPER_MINI_BARREL)
                          .put(ModBlocks.EXPOSED_COPPER_MINI_BARREL, ModBlocks.WEATHERED_COPPER_MINI_BARREL)
                          .put(ModBlocks.WEATHERED_COPPER_MINI_BARREL, ModBlocks.OXIDIZED_COPPER_MINI_BARREL)
                          .build();

    private static final BiMap<Block, Block> INVERSE_MAP = OXIDISATION_MAP.inverse();

    private static final BiMap<Block, Block> DEWAXED_MAP =
            ImmutableBiMap.<Block, Block>builder()
                          .put(ModBlocks.WAXED_COPPER_BARREL, ModBlocks.COPPER_BARREL)
                          .put(ModBlocks.WAXED_EXPOSED_COPPER_BARREL, ModBlocks.EXPOSED_COPPER_BARREL)
                          .put(ModBlocks.WAXED_WEATHERED_COPPER_BARREL, ModBlocks.WEATHERED_COPPER_BARREL)
                          .put(ModBlocks.WAXED_OXIDIZED_COPPER_BARREL, ModBlocks.OXIDIZED_COPPER_BARREL)
                          .put(ModBlocks.WAXED_COPPER_MINI_BARREL, ModBlocks.COPPER_MINI_BARREL)
                          .put(ModBlocks.WAXED_EXPOSED_COPPER_MINI_BARREL, ModBlocks.EXPOSED_COPPER_MINI_BARREL)
                          .put(ModBlocks.WAXED_WEATHERED_COPPER_MINI_BARREL, ModBlocks.WEATHERED_COPPER_MINI_BARREL)
                          .put(ModBlocks.WAXED_OXIDIZED_COPPER_MINI_BARREL, ModBlocks.OXIDIZED_COPPER_MINI_BARREL)
                          .build();

    public static Optional<BlockState> getNextOxidisedState(BlockState state) {
        return Optional.ofNullable(OXIDISATION_MAP.getOrDefault(state.getBlock(), null)).map(block -> block.withPropertiesOf(state));
    }

    public static Optional<BlockState> getPreviousOxidisedState(BlockState state) {
        return Optional.ofNullable(INVERSE_MAP.getOrDefault(state.getBlock(), null)).map(block -> block.withPropertiesOf(state));
    }

    public static BiMap<Block, Block> oxidisation() {
        return OXIDISATION_MAP;
    }

    public static BiMap<Block, Block> dewaxing() {
        return DEWAXED_MAP;
    }

    public static Optional<BlockState> getDewaxed(BlockState state) {
        return Optional.ofNullable(DEWAXED_MAP.getOrDefault(state.getBlock(), null)).map(block -> block.withPropertiesOf(state));
    }
}
