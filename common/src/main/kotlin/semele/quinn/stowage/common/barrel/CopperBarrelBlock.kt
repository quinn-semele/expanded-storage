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

package semele.quinn.stowage.common.barrel

import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.WeatheringCopper.WeatherState
import net.minecraft.world.level.block.state.BlockState
import semele.quinn.stowage.common.registration.CopperBlockHelper
import java.util.*

class CopperBarrelBlock(
    properties: Properties,
    stat: ResourceLocation,
    slots: Int,
    private val weatherState: WeatherState
) : BarrelBlock(properties, stat, slots), WeatheringCopper {

    override fun isRandomlyTicking(state: BlockState): Boolean = getNext(state).isPresent

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        changeOverTime(state, level, pos, random)
    }

    override fun getNext(state: BlockState): Optional<BlockState> {
        return CopperBlockHelper.moreOxidized(state)
    }

    override fun getAge(): WeatherState = weatherState
}