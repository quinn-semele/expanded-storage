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

package semele.quinn.stowage.common.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeTab.class)
public abstract class UpdateCreativeTabIcon {
    @Shadow private @Nullable ItemStack iconItemStack;

    @Shadow @Final private Component displayName;

    @Inject(
            method = "buildContents(Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;)V",
            at = @At("TAIL")
    )
    private void stowage$resetTabIcon(CreativeModeTab.ItemDisplayParameters parameters, CallbackInfo ci) {
        if (displayName.getContents() instanceof TranslatableContents contents) {
            if ("itemGroup.stowage.tab".equals(contents.getKey())) {
                iconItemStack = null;
            }
        }
    }
}
