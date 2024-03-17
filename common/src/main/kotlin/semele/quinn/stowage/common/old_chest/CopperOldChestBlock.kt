/*
 * Copyright 2024 Quinn Semele
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    override fun checkForCopperChanges(chest: BlockState, otherChest: BlockState): BlockState {
        val moreOxidizedState = CopperBlockHelper.moreOxidized(chest).orElse(chest)

        if (moreOxidizedState.isBlock(otherChest.block)) {
            return moreOxidizedState.block.withPropertiesOf(chest)
        }

        val lessOxidizedState = CopperBlockHelper.lessOxidized(chest).orElse(chest)

        if (lessOxidizedState.isBlock(otherChest.block)) {
            return lessOxidizedState.block.withPropertiesOf(chest)
        }

        val maybeWaxed = CopperBlockHelper.waxed(chest)

        if (maybeWaxed.isPresent) {
            val waxedState = maybeWaxed.get()

            if (waxedState.isBlock(otherChest.block)) {
                return waxedState.block.withPropertiesOf(chest)
            }
        }

        val maybeUnwaxed = CopperBlockHelper.unwaxed(chest)

        if (maybeUnwaxed.isPresent) {
            val unwaxedState = maybeUnwaxed.get()

            if (unwaxedState.isBlock(otherChest.block)) {
                return unwaxedState.block.withPropertiesOf(chest)
            }
        }

        return chest
    }

    override fun isRandomlyTicking(state: BlockState): Boolean = getNext(state).isPresent

    @Suppress("OVERRIDE_DEPRECATION")
    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        changeOverTime(state, level, pos, random)
    }

    override fun getNext(state: BlockState): Optional<BlockState> {
        return CopperBlockHelper.moreOxidized(state)
    }

    override fun getAge(): WeatherState {
        return state
    }
}