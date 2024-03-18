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

package semele.quinn.stowage.common.registration

import com.google.common.collect.BiMap
import com.google.common.collect.ImmutableBiMap
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.Optional

object CopperBlockHelper {
    private var oxidizingMap: BiMap<Block, Block>? = null
    private var oxidizingInverseMap: BiMap<Block, Block>? = null
    private var waxingMap: BiMap<Block, Block>? = null
    private var waxingInverseMap: BiMap<Block, Block>? = null

    fun setOxidizingMap(map: Map<Block, Block>) {
        if (oxidizingMap == null) {
            val immutableMap = ImmutableBiMap.builder<Block, Block>()

            immutableMap.putAll(map)

            oxidizingMap = immutableMap.build()
            oxidizingInverseMap = oxidizingMap!!.inverse()
        }
    }

    fun setWaxingMap(map: Map<Block, Block>) {
        if (waxingMap == null) {
            val immutableMap = ImmutableBiMap.builder<Block, Block>()

            immutableMap.putAll(map)

            waxingMap = immutableMap.build()
            waxingInverseMap = waxingMap!!.inverse()
        }
    }

    fun moreOxidized(state: BlockState): Optional<BlockState> {
        return Optional.ofNullable(oxidizingMap!!.getOrDefault(state.block, null)).map {
            it.withPropertiesOf(state)
        }
    }

    fun lessOxidized(state: BlockState): Optional<BlockState> {
        return Optional.ofNullable(oxidizingInverseMap!!.getOrDefault(state.block, null)).map {
            it.withPropertiesOf(state)
        }
    }

    fun waxed(state: BlockState): Optional<BlockState> {
        return Optional.ofNullable(waxingMap!!.getOrDefault(state.block, null)).map {
            it.withPropertiesOf(state)
        }
    }

    fun unwaxed(state: BlockState): Optional<BlockState> {
        return Optional.ofNullable(waxingInverseMap!!.getOrDefault(state.block, null)).map {
            it.withPropertiesOf(state)
        }
    }

    fun waxingMap(): Map<Block, Block> = waxingMap!!
}