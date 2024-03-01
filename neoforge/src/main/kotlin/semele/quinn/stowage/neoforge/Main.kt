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

import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.registries.DeferredRegister
import semele.quinn.stowage.common.Utils
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