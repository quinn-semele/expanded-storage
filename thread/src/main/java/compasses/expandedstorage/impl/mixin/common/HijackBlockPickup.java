package compasses.expandedstorage.impl.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import compasses.expandedstorage.impl.misc.Utils;
import me.steven.carrier.HolderInteractCallback;
import me.steven.carrier.api.Carriable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = HolderInteractCallback.class, remap = false)
public class HijackBlockPickup {

    @Inject(
            method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/InteractionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/steven/carrier/api/Carriable;tryPickup(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/InteractionResult;"
            ),
            cancellable = true
    )
    private void expandedstorage$earlyPickupReturn(
            Player player, Level world, InteractionHand hand, BlockPos pos, Direction hitDirection, boolean canPickup,
            CallbackInfoReturnable<InteractionResult> cir, @Local(ordinal = 0) Block block, @Local(ordinal = 0) Carriable<Block> carriable
    ) {
        if (block.builtInRegistryHolder().key().location().getNamespace().equals(Utils.MOD_ID)) {
            InteractionResult result = carriable.tryPickup(player, world, pos, null);
            if (result.consumesAction()) {
                cir.setReturnValue(result);
            }
        }
    }
}
