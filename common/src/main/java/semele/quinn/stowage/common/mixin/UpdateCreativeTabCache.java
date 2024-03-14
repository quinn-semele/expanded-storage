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

import net.minecraft.core.HolderLookup;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import semele.quinn.stowage.common.core.CreativeTabReloadListener;

@Mixin(ItemDisplayParameters.class)
public abstract class UpdateCreativeTabCache {
    @Inject(
            method = "needsUpdate(Lnet/minecraft/world/flag/FeatureFlagSet;ZLnet/minecraft/core/HolderLookup$Provider;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stowage$updateTabCaches(FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider holders, CallbackInfoReturnable<Boolean> cir) {
        if (CreativeTabReloadListener.INSTANCE.shouldRefresh()) {
            cir.setReturnValue(true);
        }
    }
}
