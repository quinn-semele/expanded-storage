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

package semele.quinn.stowage.common.core

import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.item.ItemStack
import semele.quinn.stowage.common.Utils
import semele.quinn.stowage.common.data.CreativeTabData

object CreativeTabReloadListener : SimplePreparableReloadListener<CreativeTabData>() {
    private val CREATIVE_TAB_DATA_LOCATION = Utils.id("creative_tab.json")

    private var icon: ItemStack = ItemStack.EMPTY
    private var contents: List<ItemStack> = listOf()

    private var lastSuccessfulRefreshes = 0
    private var successfulRefreshes = 0

    override fun prepare(resourceManager: ResourceManager, profiler: ProfilerFiller): CreativeTabData {
        try {
            val reader = resourceManager.openAsReader(CREATIVE_TAB_DATA_LOCATION)

            val jsonObject = JsonParser.parseReader(reader).asJsonObject

            return CreativeTabData.CODEC.parse(JsonOps.INSTANCE, jsonObject).result().get()
        } catch (exception: Exception) {
            Utils.LOGGER.warn("Failed to load creative tab data.")
        }

        return CreativeTabData(listOf(), listOf())
    }

    override fun apply(data: CreativeTabData, resourceManager: ResourceManager, profiler: ProfilerFiller) {
        val tabIcon = data.tabIcon()
        val tabContents = data.tabContents()

        // todo: implement logic to cache these?
        successfulRefreshes++
        icon = tabIcon
        contents = tabContents
    }

    fun getIcon(): ItemStack = icon

    fun getContents(): List<ItemStack> = contents

    fun shouldRefresh(): Boolean {
        val needsRefresh = lastSuccessfulRefreshes < successfulRefreshes

        lastSuccessfulRefreshes = successfulRefreshes

        return needsRefresh && successfulRefreshes > 1
    }
}