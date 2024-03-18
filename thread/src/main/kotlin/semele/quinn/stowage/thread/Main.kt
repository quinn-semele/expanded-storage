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

package semele.quinn.stowage.thread

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.server.packs.PackType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import semele.quinn.stowage.common.Utils
import semele.quinn.stowage.common.core.CreativeTabReloadListener
import semele.quinn.stowage.common.data.BiConvertibleMapData
import semele.quinn.stowage.common.registration.CopperBlockHelper
import semele.quinn.stowage.common.registration.Registration
import semele.quinn.stowage.common.registration.SimpleContentHolder

object Main : ModInitializer {
    override fun onInitialize() {
        Utils.LOGGER.info("Hello from Stowage. (Fabric/Quilt)")
        val container: ModContainer = FabricLoader.getInstance().getModContainer(Utils.MOD_ID).orElseThrow()

        Registration.constructBarrelContent { consumeContent() }
        Registration.constructOldChestContent { consumeContent() }

        val waxingData = BiConvertibleMapData.fromFile(container.findPath("data/${Utils.MOD_ID}/waxing.json").orElseThrow())
        val oxidizingData = BiConvertibleMapData.fromFile(container.findPath("data/${Utils.MOD_ID}/oxidizing.json").orElseThrow())

        val waxingBlocks = waxingData?.toBlocks() ?: mapOf()
        val oxidizingBlocks = oxidizingData?.toBlocks() ?: mapOf()

        oxidizingBlocks.forEach(OxidizableBlocksRegistry::registerOxidizableBlockPair)
        waxingBlocks.forEach(OxidizableBlocksRegistry::registerWaxableBlockPair)

        CopperBlockHelper.setOxidizingMap(oxidizingBlocks)
        CopperBlockHelper.setWaxingMap(waxingBlocks)

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(ThreadWrappedListener(Utils.id("creative_tab"), CreativeTabReloadListener))

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Utils.id("tab"), FabricItemGroup.builder()
            .title(Component.translatable("itemGroup.stowage.tab"))
            .icon {
                CreativeTabReloadListener.getIcon()
            }
            .displayItems { _, output ->
                CreativeTabReloadListener.getContents().forEach(output::accept)
            }
            .build()
        )
    }

    private fun <B: Block, BE: BlockEntity> SimpleContentHolder<B, BE>.consumeContent() {
        for ((name, block) in blocks) {
            Registry.register(BuiltInRegistries.BLOCK, name, block)
        }

        for ((name, item) in items) {
            Registry.register(BuiltInRegistries.ITEM, name, item)
        }

        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, blockEntity.name, blockEntity.value)

        for (stat in stats) {
            Registry.register(BuiltInRegistries.CUSTOM_STAT, stat, stat)
        }
    }
}