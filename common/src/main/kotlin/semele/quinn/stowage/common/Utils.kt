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

package semele.quinn.stowage.common

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.item.Item.Properties as ItemProperties
import net.minecraft.world.level.block.state.BlockBehaviour.Properties as BlockProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Utils {
    const val MOD_ID = "stowage"
    val LOGGER: Logger = LoggerFactory.getLogger("Stowage")

    const val WOOD_SLOTS = 27
    const val COPPER_SLOTS = 45
    const val IRON_SLOTS = 54
    const val GOLD_SLOTS = 81
    const val DIAMOND_SLOTS = 108
    const val OBSIDIAN_SLOTS = 108
    const val NETHERITE_SLOTS = 135

    fun id(path: String) = ResourceLocation(MOD_ID, path)

    fun BlockProperties.flammable(): BlockProperties = ignitedByLava()

    fun BlockProperties.barrel(): BlockProperties {
        mapColor(MapColor.WOOD)
        instrument(NoteBlockInstrument.BASS)
        sound(SoundType.WOOD)

        return this
    }

    fun BlockProperties.copper(): BlockProperties {
        requiresCorrectToolForDrops()
        strength(3f, 6f)

        return this
    }

    fun BlockProperties.iron(): BlockProperties {
        requiresCorrectToolForDrops()
        strength(5f, 6f)

        return this
    }

    fun BlockProperties.gold(): BlockProperties {
        requiresCorrectToolForDrops()
        strength(3f, 6f)

        return this
    }

    fun BlockProperties.diamond(): BlockProperties {
        requiresCorrectToolForDrops()
        strength(5f, 6f)

        return this
    }

    fun BlockProperties.obsidian(): BlockProperties {
        requiresCorrectToolForDrops()
        strength(50f, 1200f)

        return this
    }

    fun BlockProperties.netherite(): BlockProperties {
        requiresCorrectToolForDrops()
        strength(50f, 1200f)

        return this
    }

    fun ItemProperties.netherite(): ItemProperties = fireResistant()
}