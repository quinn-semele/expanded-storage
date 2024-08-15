package compasses.expandedstorage.impl.mixin.common;

import compasses.expandedstorage.impl.misc.Utils;
import me.steven.carrier.Carrier;
import me.steven.carrier.Config;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Carrier.class, remap = false)
public class AllowCarryingESBlocks {
    @Shadow(remap = false)
    public static Config CONFIG;

    @Inject(
            method = "canCarry(Lnet/minecraft/resources/ResourceLocation;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void expandedstorage$canCarry(ResourceLocation id, CallbackInfoReturnable<Boolean> cir) {
        if (id.getNamespace().equals(Utils.MOD_ID) && CONFIG.getType() == Config.ListType.WHITELIST) {
            cir.setReturnValue(true);
        }
    }
}
