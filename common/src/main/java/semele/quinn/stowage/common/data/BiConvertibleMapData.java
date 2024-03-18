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

package semele.quinn.stowage.common.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import semele.quinn.stowage.common.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public record BiConvertibleMapData(Map<ResourceLocation, ResourceLocation> entries) {
    public static final Codec<BiConvertibleMapData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(ResourceLocation.CODEC, ResourceLocation.CODEC).fieldOf("entries").forGetter(BiConvertibleMapData::entries)
    ).apply(instance, BiConvertibleMapData::new));

    @Nullable
    public static BiConvertibleMapData fromFile(Path path) {
        try {
            String value = Files.readString(path);
            JsonObject object = JsonParser.parseString(value).getAsJsonObject();

            DataResult<BiConvertibleMapData> result = CODEC.parse(JsonOps.INSTANCE, object);

            if (result.result().isPresent()) {
                return result.result().get();
            } else {
                Utils.INSTANCE.getLOGGER().error(path + " is an invalid.");
                Utils.INSTANCE.getLOGGER().error(result.error().get().message());
            }
        } catch (IOException e) {
            Utils.INSTANCE.getLOGGER().error("Failed to read " + path + ": " + e.getMessage());
        }

        return null;
    }

    public Map<Block, Block> toBlocks() {
        return entries.entrySet().stream().map(entry -> {
            var key = BuiltInRegistries.BLOCK.getOptional(entry.getKey());
            var value = BuiltInRegistries.BLOCK.getOptional(entry.getValue());

            if (key.isEmpty() || value.isEmpty()) {
                return null;
            }

            return Map.entry(key.get(), value.get());
        }).filter(Objects::nonNull).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
