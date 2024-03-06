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

package semele.quinn.stowage.common.core.data.config

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.minecraft.resources.ResourceLocation
import semele.quinn.stowage.common.Utils

@Serializable
data class CommonConfig0(
    val configVersion: Int = 0,
    val enabledModules: Map<@Contextual ResourceLocation, Boolean> = mapOf(
        Utils.CHEST_CONTENT to true,
        Utils.BARREL_CONTENT to true,
        Utils.OLD_CHEST_CONTENT to true,
        Utils.MINI_STORAGE_CONTENT to true
    )
)