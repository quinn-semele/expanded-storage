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
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.registries.RegisterEvent
import semele.quinn.stowage.common.Utils
import semele.quinn.stowage.common.registration.Registration

@Mod("stowage")
class Main(container: ModContainer, bus: IEventBus) {
    init {
        Utils.LOGGER.info("Hello from Stowage. (NeoForge)")

        Registration.constructBarrelContent {
            bus.addListener<RegisterEvent> { event ->
                event.register(Registries.BLOCK) { registry ->
                    for ((name, block) in blocks) {
                        registry.register(name, block)
                    }
                }

                event.register(Registries.ITEM) { registry ->
                    for ((name, item) in items) {
                        registry.register(name, item)
                    }
                }

                event.register((Registries.BLOCK_ENTITY_TYPE)) { registry ->
                    registry.register(blockEntity.name, blockEntity.value)
                }

                event.register(Registries.CUSTOM_STAT) { registry ->
                    for (stat in stats) {
                        registry.register(stat, stat)
                    }
                }
            }
        }
    }
}