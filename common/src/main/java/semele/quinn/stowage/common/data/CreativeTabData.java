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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Objects;

public record CreativeTabData(List<ProtoItemStack> icons, List<ProtoItemStack> values) {
    public static final Codec<CreativeTabData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ProtoItemStack.FULL_CODEC.listOf().fieldOf("icons").forGetter(CreativeTabData::icons),
            ProtoItemStack.FULL_CODEC.listOf().fieldOf("entries").forGetter(CreativeTabData::values)
    ).apply(instance, CreativeTabData::new));

    public ItemStack tabIcon() {
        for (ProtoItemStack icon : icons) {
            var holder = BuiltInRegistries.ITEM.getHolder(ResourceKey.create(Registries.ITEM, icon.itemId()));

            if (holder.isPresent()) {
                return new ItemStack(holder.get(), 1, icon.tag());
            }
        }

        return Items.MILK_BUCKET.getDefaultInstance();
    }

    public List<ItemStack> tabContents() {
        return values.stream().map(proto -> {
            var holder = BuiltInRegistries.ITEM.getHolder(ResourceKey.create(Registries.ITEM, proto.itemId()));

            return holder.map(item -> new ItemStack(item, 1, proto.tag())).orElse(null);
        }).filter(Objects::nonNull).toList();
    }
}
