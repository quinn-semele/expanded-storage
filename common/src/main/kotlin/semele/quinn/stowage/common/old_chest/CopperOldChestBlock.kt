package semele.quinn.stowage.common.old_chest

import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.WeatheringCopper.WeatherState
import net.minecraft.world.level.block.state.BlockState
import semele.quinn.stowage.common.Utils.isBlock
import semele.quinn.stowage.common.registration.CopperBlockHelper
import java.util.*

class CopperOldChestBlock(
    properties: Properties,
    openingStat: ResourceLocation,
    slots: Int,
    private val state: WeatherState
) : OldChestBlock(properties, openingStat, slots), WeatheringCopper {

    override fun checkForOxidisation(chest: BlockState, otherChest: BlockState): BlockState {
         val nextState = this.getNext(chest).orElse(chest)

        if (nextState.isBlock(otherChest.block)) {
            return nextState.block.withPropertiesOf(chest)
        }

        return chest
    }

    override fun isRandomlyTicking(state: BlockState): Boolean = getNext(state).isPresent

    @Suppress("OVERRIDE_DEPRECATION")
    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        changeOverTime(state, level, pos, random)
    }

    override fun getNext(state: BlockState): Optional<BlockState> {
        return CopperBlockHelper.getNextState(state)
    }

    override fun getAge(): WeatherState {
        return state
    }
}