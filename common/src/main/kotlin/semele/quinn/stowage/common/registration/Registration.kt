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
import semele.quinn.stowage.common.Utils.WOOD_SLOTS
import semele.quinn.stowage.common.Utils.barrel
import semele.quinn.stowage.common.Utils.copperChest
import semele.quinn.stowage.common.Utils.diamondChest
import semele.quinn.stowage.common.Utils.strongMetal
import semele.quinn.stowage.common.Utils.flammable
import semele.quinn.stowage.common.Utils.weakMetal
import semele.quinn.stowage.common.Utils.netherite
import semele.quinn.stowage.common.Utils.explosionProof
import semele.quinn.stowage.common.Utils.goldChest
import semele.quinn.stowage.common.Utils.ironChest
import semele.quinn.stowage.common.Utils.netheriteChest
import semele.quinn.stowage.common.Utils.obsidianChest
import semele.quinn.stowage.common.Utils.woodChest
import semele.quinn.stowage.common.barrel.BarrelBlock
import semele.quinn.stowage.common.barrel.BarrelBlockEntity
import semele.quinn.stowage.common.barrel.CopperBarrelBlock
import semele.quinn.stowage.common.old_chest.CopperOldChestBlock
import semele.quinn.stowage.common.old_chest.OldChestBlock
import semele.quinn.stowage.common.old_chest.OldChestBlockEntity
import net.minecraft.world.item.Item.Properties as ItemProperties
import net.minecraft.world.level.block.state.BlockBehaviour.Properties as BlockProperties

object Registration {
    val BARREL_OBJECT_TYPE: ResourceLocation = Utils.id("barrel")
    val CHEST_OBJECT_TYPE: ResourceLocation = Utils.id("chest")
    val OLD_CHEST_OBJECT_TYPE: ResourceLocation = Utils.id("old_chest")
    val MINI_STORAGE_OBJECT_TYPE: ResourceLocation = Utils.id("mini_storage")

    fun constructBarrelContent(callback: SimpleContentHolder<BarrelBlock, BarrelBlockEntity>.() -> Unit) {
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

        val copperBlockProperties = BlockProperties.of().barrel().weakMetal().flammable()

        val copperBarrels = mapOf(
            "copper_barrel" to WeatheringCopper.WeatherState.UNAFFECTED,
            "exposed_copper_barrel" to WeatheringCopper.WeatherState.EXPOSED,
            "weathered_copper_barrel" to WeatheringCopper.WeatherState.WEATHERED,
            "oxidized_copper_barrel" to WeatheringCopper.WeatherState.OXIDIZED,
        )

        for ((id, _) in copperBarrels) {
            createBarrel(Utils.id("waxed_$id"), copperStat, copperBlockProperties, ItemProperties(), COPPER_SLOTS)
        }

        for ((id, state) in copperBarrels) {
            val barrelId = Utils.id(id)

            val barrelBlock = NamedValue(barrelId) {
                CopperBarrelBlock(copperBlockProperties, copperStat, COPPER_SLOTS, state)
            }

            val barrelItem = NamedValue(barrelId) {
                BlockItem(barrelBlock.value, ItemProperties())
            }

            blocks.add(barrelBlock as NamedValue<BarrelBlock>)
            items.add(barrelItem)
        }

        createBarrel(Utils.id("iron_barrel"), ironStat, BlockProperties.of().barrel().strongMetal().flammable(), ItemProperties(), IRON_SLOTS)
        createBarrel(Utils.id("gold_barrel"), goldStat, BlockProperties.of().barrel().weakMetal().flammable(), ItemProperties(), GOLD_SLOTS)
        createBarrel(Utils.id("diamond_barrel"), diamondStat, BlockProperties.of().barrel().strongMetal().flammable(), ItemProperties(), DIAMOND_SLOTS)
        createBarrel(Utils.id("obsidian_barrel"), obsidianStat, BlockProperties.of().barrel().explosionProof().flammable(), ItemProperties(), OBSIDIAN_SLOTS)
        createBarrel(Utils.id("netherite_barrel"), netheriteStat, BlockProperties.of().barrel().explosionProof(), ItemProperties().netherite(), NETHERITE_SLOTS)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val blockEntity =
            NamedValue(BARREL_OBJECT_TYPE) {
                BlockEntityType.Builder.of(::BarrelBlockEntity, *blocks.map { it.value }.toTypedArray())
                    .build(Util.fetchChoiceType(References.BLOCK_ENTITY, BARREL_OBJECT_TYPE.toString()))
            }

        val content = SimpleContentHolder(
            blocks = blocks,
            items = items,
            blockEntity = blockEntity,
            stats = stats
        )

        callback.invoke(content)
    }

    fun constructOldChestContent(callback: SimpleContentHolder<OldChestBlock, OldChestBlockEntity>.() -> Unit) {
        val blocks = arrayListOf<NamedValue<OldChestBlock>>()
        val items = arrayListOf<NamedValue<BlockItem>>()
        val stats = arrayListOf<ResourceLocation>()

        val createStat = { id: String ->
            val stat = Utils.id(id)
            stats.add(stat)
            stat
        }

        val woodStat = createStat("open_old_wood_chest")
        val copperStat = createStat("open_old_copper_chest")
        val ironStat = createStat("open_old_iron_chest")
        val goldStat = createStat("open_old_gold_chest")
        val diamondStat = createStat("open_old_diamond_chest")
        val obsidianStat = createStat("open_old_obsidian_chest")
        val netheriteStat = createStat("open_old_netherite_chest")

        val createChest = {
                id: ResourceLocation,
                stat: ResourceLocation,
                blockProps: BlockProperties,
                itemProps: ItemProperties,
                slots: Int ->

            val chestBlock = NamedValue(id) {
                OldChestBlock(blockProps, stat, slots)
            }

            val chestItem = NamedValue(id) {
                BlockItem(chestBlock.value, itemProps)
            }

            blocks.add(chestBlock)
            items.add(chestItem)
        }

        val copperBlockProperties = BlockProperties.of().copperChest()

        val copperChests = mapOf(
            "old_copper_chest" to WeatheringCopper.WeatherState.UNAFFECTED,
            "old_exposed_copper_chest" to WeatheringCopper.WeatherState.EXPOSED,
            "old_weathered_copper_chest" to WeatheringCopper.WeatherState.WEATHERED,
            "old_oxidized_copper_chest" to WeatheringCopper.WeatherState.OXIDIZED,
        )

        createChest(Utils.id("old_wood_chest"), woodStat, BlockProperties.of().woodChest(), ItemProperties(), WOOD_SLOTS)

        for ((id, _) in copperChests) {
            createChest(Utils.id("waxed_$id"), copperStat, copperBlockProperties, ItemProperties(), COPPER_SLOTS)
        }

        for ((id, state) in copperChests) {
            val chestId = Utils.id(id)

            val chestBlock = NamedValue(chestId) {
                CopperOldChestBlock(copperBlockProperties, copperStat, COPPER_SLOTS, state)
            }

            val chestItem = NamedValue(chestId) {
                BlockItem(chestBlock.value, ItemProperties())
            }

            blocks.add(chestBlock as NamedValue<OldChestBlock>)
            items.add(chestItem)
        }

        createChest(Utils.id("old_iron_chest"), ironStat, BlockProperties.of().ironChest(), ItemProperties(), IRON_SLOTS)
        createChest(Utils.id("old_gold_chest"), goldStat, BlockProperties.of().goldChest(), ItemProperties(), GOLD_SLOTS)
        createChest(Utils.id("old_diamond_chest"), diamondStat, BlockProperties.of().diamondChest(), ItemProperties(), DIAMOND_SLOTS)
        createChest(Utils.id("old_obsidian_chest"), obsidianStat, BlockProperties.of().obsidianChest(), ItemProperties(), OBSIDIAN_SLOTS)
        createChest(Utils.id("old_netherite_chest"), netheriteStat, BlockProperties.of().netheriteChest(), ItemProperties().netherite(), NETHERITE_SLOTS)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val blockEntity =
            NamedValue(OLD_CHEST_OBJECT_TYPE) {
                BlockEntityType.Builder.of(::OldChestBlockEntity, *blocks.map { it.value }.toTypedArray())
                    .build(Util.fetchChoiceType(References.BLOCK_ENTITY, OLD_CHEST_OBJECT_TYPE.toString()))
            }

        val content = SimpleContentHolder(
            blocks = blocks,
            items = items,
            blockEntity = blockEntity,
            stats = stats
        )

        callback.invoke(content)
    }
}