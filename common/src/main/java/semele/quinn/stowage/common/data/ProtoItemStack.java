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

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Function;

public record ProtoItemStack(
        ResourceLocation itemId,
        Optional<CompoundTag> tag
) {
    private static final Codec<ProtoItemStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("itemId").forGetter(ProtoItemStack::itemId),
            CompoundTag.CODEC.optionalFieldOf("tag").forGetter(ProtoItemStack::tag)
    ).apply(instance, ProtoItemStack::new));

    private static final Codec<ProtoItemStack> SIMPLE_CODEC = ResourceLocation.CODEC.xmap(itemId -> new ProtoItemStack(itemId, Optional.empty()), ProtoItemStack::itemId);

    public static final Codec<ProtoItemStack> FULL_CODEC = Codec.either(CODEC, SIMPLE_CODEC)
    .xmap(either -> either.map(Function.identity(), Function.identity()), stack -> {
        if (stack.tag().isPresent()) {
            return Either.left(stack);
        } else {
            return Either.right(stack);
        }
    });
}
