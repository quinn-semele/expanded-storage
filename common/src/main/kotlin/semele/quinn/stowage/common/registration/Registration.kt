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

import net.minecraft.Util
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.datafix.fixes.References
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.entity.BlockEntityType
import semele.quinn.stowage.common.Utils
import semele.quinn.stowage.common.Utils.COPPER_SLOTS
import semele.quinn.stowage.common.Utils.DIAMOND_SLOTS
import semele.quinn.stowage.common.Utils.GOLD_SLOTS
import semele.quinn.stowage.common.Utils.IRON_SLOTS
import semele.quinn.stowage.common.Utils.NETHERITE_SLOTS
import semele.quinn.stowage.common.Utils.OBSIDIAN_SLOTS
import semele.quinn.stowage.common.Utils.barrel
import semele.quinn.stowage.common.Utils.copper
import semele.quinn.stowage.common.Utils.diamond
import semele.quinn.stowage.common.Utils.flammable
import semele.quinn.stowage.common.Utils.gold
import semele.quinn.stowage.common.Utils.iron
import semele.quinn.stowage.common.Utils.netherite
import semele.quinn.stowage.common.Utils.obsidian
import semele.quinn.stowage.common.barrel.BarrelBlock
import semele.quinn.stowage.common.barrel.BarrelBlockEntity
import semele.quinn.stowage.common.barrel.CopperBarrelBlock
import net.minecraft.world.item.Item.Properties as ItemProperties
import net.minecraft.world.level.block.state.BlockBehaviour.Properties as BlockProperties

object Registration {
    val BARREL_OBJECT_TYPE: ResourceLocation = Utils.id("barrel")
    val CHEST_OBJECT_TYPE: ResourceLocation = Utils.id("chest")
    val OLD_CHEST_OBJECT_TYPE: ResourceLocation = Utils.id("old_chest")
    val MINI_STORAGE_OBJECT_TYPE: ResourceLocation = Utils.id("mini_storage")

    fun constructBarrelContent(callback: BarrelContent.() -> Unit) {
        val blocks = arrayListOf<NamedValue<BarrelBlock>>()
        val items = arrayListOf<NamedValue<BlockItem>>()
        val stats = arrayListOf<ResourceLocation>()

        val createStat = { id: String ->
            val stat = Utils.id(id)
            stats.add(stat)
            stat
        }

        val copperStat = createStat("open_copper_barrel")
        val ironStat = createStat("open_iron_barrel")
        val goldStat = createStat("open_gold_barrel")
        val diamondStat = createStat("open_diamond_barrel")
        val obsidianStat = createStat("open_obsidian_barrel")
        val netheriteStat = createStat("open_netherite_barrel")

        val createBarrel = {
            id: ResourceLocation,
            stat: ResourceLocation,
            blockProps: BlockProperties,
            itemProps: ItemProperties,
            slots: Int ->

            val barrelBlock = NamedValue(id) {
                BarrelBlock(blockProps, stat, slots)
            }

            val barrelItem = NamedValue(id) {
                BlockItem(barrelBlock.value, itemProps)
            }

            blocks.add(barrelBlock)
            items.add(barrelItem)
        }

        val copperBlockProperties = BlockProperties.of().barrel().copper().flammable()

        createBarrel(Utils.id("waxed_copper_barrel"), copperStat, copperBlockProperties, ItemProperties(), COPPER_SLOTS)
        createBarrel(Utils.id("waxed_exposed_copper_barrel"), copperStat, copperBlockProperties, ItemProperties(), COPPER_SLOTS)
        createBarrel(Utils.id("waxed_weathered_copper_barrel"), copperStat, copperBlockProperties, ItemProperties(), COPPER_SLOTS)
        createBarrel(Utils.id("waxed_oxidized_copper_barrel"), copperStat, copperBlockProperties, ItemProperties(), COPPER_SLOTS)
        createBarrel(Utils.id("iron_barrel"), ironStat, BlockProperties.of().barrel().iron().flammable(), ItemProperties(), IRON_SLOTS)
        createBarrel(Utils.id("gold_barrel"), goldStat, BlockProperties.of().barrel().gold().flammable(), ItemProperties(), GOLD_SLOTS)
        createBarrel(Utils.id("diamond_barrel"), diamondStat, BlockProperties.of().barrel().diamond().flammable(), ItemProperties(), DIAMOND_SLOTS)
        createBarrel(Utils.id("obsidian_barrel"), obsidianStat, BlockProperties.of().barrel().obsidian().flammable(), ItemProperties(), OBSIDIAN_SLOTS)
        createBarrel(Utils.id("netherite_barrel"), netheriteStat, BlockProperties.of().barrel().netherite(), ItemProperties().netherite(), NETHERITE_SLOTS)

        val copperBarrels = mapOf(
            Utils.id("copper_barrel") to WeatheringCopper.WeatherState.UNAFFECTED,
            Utils.id("exposed_copper_barrel") to WeatheringCopper.WeatherState.EXPOSED,
            Utils.id("weathered_copper_barrel") to WeatheringCopper.WeatherState.WEATHERED,
            Utils.id("oxidized_copper_barrel") to WeatheringCopper.WeatherState.OXIDIZED,
        )

        for ((id, state) in copperBarrels) {
            val barrelBlock = NamedValue(id) {
                CopperBarrelBlock(copperBlockProperties, copperStat, COPPER_SLOTS, state)
            }

            val barrelItem = NamedValue(id) {
                BlockItem(barrelBlock.value, ItemProperties())
            }

            blocks.add(barrelBlock as NamedValue<BarrelBlock>)
            items.add(barrelItem)
        }

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val blockEntity =
            NamedValue(BARREL_OBJECT_TYPE) {
                BlockEntityType.Builder.of(::BarrelBlockEntity, *blocks.map { it.value }.toTypedArray())
                    .build(Util.fetchChoiceType(References.BLOCK_ENTITY, BARREL_OBJECT_TYPE.toString()))
            }

        val content = BarrelContent(
            blocks = blocks,
            items = items,
            blockEntity = blockEntity,
            stats = stats
        )

        callback.invoke(content)
    }
}