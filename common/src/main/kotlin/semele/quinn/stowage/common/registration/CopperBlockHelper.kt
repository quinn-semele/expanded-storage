package semele.quinn.stowage.common.registration

import com.google.common.collect.BiMap
import com.google.common.collect.ImmutableBiMap
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.Optional

object CopperBlockHelper {
    private val OXIDISATION_MAP = ImmutableBiMap.builder<Block, Block>()
        .put(ModBlocks.COPPER_BARREL, ModBlocks.EXPOSED_COPPER_BARREL)
        .put(ModBlocks.EXPOSED_COPPER_BARREL, ModBlocks.WEATHERED_COPPER_BARREL)
        .put(ModBlocks.WEATHERED_COPPER_BARREL, ModBlocks.OXIDIZED_COPPER_BARREL)
        .put(ModBlocks.OLD_COPPER_CHEST, ModBlocks.OLD_EXPOSED_COPPER_CHEST)
        .put(ModBlocks.OLD_EXPOSED_COPPER_CHEST, ModBlocks.OLD_WEATHERED_COPPER_CHEST)
        .put(ModBlocks.OLD_WEATHERED_COPPER_CHEST, ModBlocks.OLD_OXIDIZED_COPPER_CHEST)
        .build()

    private val INVERSE_MAP = OXIDISATION_MAP.inverse()

    private val DEWAXED_MAP = ImmutableBiMap.builder<Block, Block>()
        .put(ModBlocks.WAXED_COPPER_BARREL, ModBlocks.COPPER_BARREL)
        .put(ModBlocks.WAXED_EXPOSED_COPPER_BARREL, ModBlocks.EXPOSED_COPPER_BARREL)
        .put(ModBlocks.WAXED_WEATHERED_COPPER_BARREL, ModBlocks.WEATHERED_COPPER_BARREL)
        .put(ModBlocks.WAXED_OXIDIZED_COPPER_BARREL, ModBlocks.OXIDIZED_COPPER_BARREL)
        .put(ModBlocks.WAXED_OLD_COPPER_CHEST, ModBlocks.OLD_COPPER_CHEST)
        .put(ModBlocks.WAXED_OLD_EXPOSED_COPPER_CHEST, ModBlocks.OLD_EXPOSED_COPPER_CHEST)
        .put(ModBlocks.WAXED_OLD_WEATHERED_COPPER_CHEST, ModBlocks.OLD_WEATHERED_COPPER_CHEST)
        .put(ModBlocks.WAXED_OLD_OXIDIZED_COPPER_CHEST, ModBlocks.OLD_OXIDIZED_COPPER_CHEST)
    .build()

    fun getNextState(state: BlockState): Optional<BlockState> {
        return Optional.ofNullable(OXIDISATION_MAP.getOrDefault(state.block, null)).map {
            it.withPropertiesOf(state)
        }
    }

    fun getPreviousState(state: BlockState): Optional<BlockState> {
        return Optional.ofNullable(INVERSE_MAP.getOrDefault(state.block, null)).map {
            it.withPropertiesOf(state)
        }
    }

    fun getDewaxed(state: BlockState): Optional<BlockState> {
        return Optional.ofNullable(DEWAXED_MAP.getOrDefault(state.block, null)).map {
            it.withPropertiesOf(state)
        }
    }

    fun oxidisationMap(): Map<Block, Block> = OXIDISATION_MAP

    fun dewaxingMap(): BiMap<Block, Block> = DEWAXED_MAP
}