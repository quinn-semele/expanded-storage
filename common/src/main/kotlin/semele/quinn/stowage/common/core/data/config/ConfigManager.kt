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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf
import net.minecraft.resources.ResourceLocation
import semele.quinn.stowage.common.Utils
import semele.quinn.stowage.common.core.helpers.PlatformHelper
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

object ConfigManager {
    @OptIn(ExperimentalSerializationApi::class)
    private val json: Json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = true
        prettyPrintIndent = "  "
        serializersModule = serializersModuleOf(ResourceLocation::class, ResourceLocationSerializer)
    }

    private var config: CommonConfig0? = null

    fun getConfig(): CommonConfig0 {
        val validConfig = config ?: loadConfig()

        if (config == null) {
            config = validConfig
        }

        return validConfig
    }

    fun updateConfig(function: CommonConfig0.() -> CommonConfig0) {
        val config = config ?: loadConfig()
        val newConfig = config.function()

        if (newConfig != config) {
            ConfigManager.config = newConfig

            saveConfig(newConfig, "updated")
        }
    }

    private fun loadConfig(): CommonConfig0 {
        val configPath = PlatformHelper.instance.getConfigDirectory().resolve("stowage.json")

        if (!configPath.exists()) {
            return defaultConfig().also {
                saveConfig(it, "default")
            }
        }

        val configText = try {
            configPath.readText(Charsets.UTF_8)

        } catch (exception: Exception) {
            Utils.LOGGER.error("Failed to read the config file, using default config.", exception)

            return defaultConfig()
        }

        val configVersion: ConfigVersion = try {
            json.decodeFromString(configText)
        } catch (exception: Exception) {
            Utils.LOGGER.error("Failed to read the config version from the config file, using default config.", exception)

            return defaultConfig()
        }

        if (configVersion.configVersion != 0) {
            Utils.LOGGER.error("Unsupported config version, using default config.")

            return defaultConfig()
        }

        try {
            return json.decodeFromString(configText)
        } catch (exception: Exception) {
            Utils.LOGGER.error("Failed to deserialize the config file, using default config.", exception)

            return defaultConfig()
        }
    }

    private fun saveConfig(config: CommonConfig0, type: String) {
        val configText = try {
            json.encodeToString(config)
        } catch (exception: Exception) {
            Utils.LOGGER.error("Failed to serialize the $type config.", exception)
            Utils.LOGGER.info("Current config: $config")

            return
        }

        val configPath = PlatformHelper.instance.getConfigDirectory().resolve("stowage.json")

        try {
            configPath.writeText(configText, Charsets.UTF_8)
        } catch (exception: Exception) {
            Utils.LOGGER.error("Failed to save the $type config.", exception)
            Utils.LOGGER.info("Current config: $config")
        }
    }

    private fun defaultConfig(): CommonConfig0 {
        val config = CommonConfig0()

        ConfigManager.config = config

        return config
    }
}