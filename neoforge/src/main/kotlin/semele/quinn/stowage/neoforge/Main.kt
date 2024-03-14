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

package semele.quinn.stowage.neoforge

import com.google.common.base.Suppliers
import com.google.common.collect.ImmutableBiMap
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.HoneycombItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.common.ToolActions
import net.neoforged.neoforge.event.AddReloadListenerEvent
import net.neoforged.neoforge.event.level.BlockEvent
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.RegisterEvent
import semele.quinn.stowage.common.Utils
import semele.quinn.stowage.common.core.CreativeTabReloadListener
import semele.quinn.stowage.common.registration.CopperBlockHelper
import semele.quinn.stowage.common.registration.Registration
import semele.quinn.stowage.common.registration.SimpleContentHolder

@Mod(Utils.MOD_ID)
class Main(container: ModContainer, bus: IEventBus) {
    private val BLOCKS = DeferredRegister.createBlocks(Utils.MOD_ID)
    private val ITEMS = DeferredRegister.createItems(Utils.MOD_ID)
    private val BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Utils.MOD_ID)
    private val CUSTOM_STATS = DeferredRegister.create(Registries.CUSTOM_STAT, Utils.MOD_ID)

    init {
        Utils.LOGGER.info("Hello from Stowage. (NeoForge)")

        BLOCKS.register(bus)
        ITEMS.register(bus)
        BLOCK_ENTITIES.register(bus)
        CUSTOM_STATS.register(bus)

        Registration.constructBarrelContent { consumeContent() }
        Registration.constructOldChestContent { consumeContent() }

        val originalWaxables = HoneycombItem.WAXABLES

        HoneycombItem.WAXABLES = Suppliers.memoize {
            ImmutableBiMap.builder<Block, Block>()
                .putAll(originalWaxables.get())
                .putAll(CopperBlockHelper.dewaxingMap().inverse())
                .build()
        }

        NeoForge.EVENT_BUS.addListener<BlockEvent.BlockToolModificationEvent> { event ->
            if (event.toolAction == ToolActions.AXE_SCRAPE) {
                CopperBlockHelper.getPreviousState(event.state).ifPresent {
                    event.finalState = it
                }
            } else if (event.toolAction == ToolActions.AXE_WAX_OFF) {
                CopperBlockHelper.getDewaxed(event.state).ifPresent {
                    event.finalState = it
                }
            }
        }

        NeoForge.EVENT_BUS.addListener(AddReloadListenerEvent::class.java, this::registerReloadListeners)

        bus.addListener(RegisterEvent::class.java, this::registerCreativeTab)
    }

    private fun registerReloadListeners(event: AddReloadListenerEvent) {
        event.addListener(CreativeTabReloadListener)
    }

    private fun registerCreativeTab(event: RegisterEvent) {
        event.register(Registries.CREATIVE_MODE_TAB) {
            it.register(Utils.id("tab"), CreativeModeTab.builder()
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
    }

    private fun <B: Block, BE: BlockEntity> SimpleContentHolder<B, BE>.consumeContent() {
        for (block in blocks) {
            BLOCKS.register(block.name.path) { _ -> block.value }
        }

        for (item in items) {
            ITEMS.register(item.name.path) { _ -> item.value }
        }

        BLOCK_ENTITIES.register(blockEntity.name.path) { _ -> blockEntity.value }

        for (stat in stats) {
            CUSTOM_STATS.register(stat.path) { _ -> stat }
        }
    }
}