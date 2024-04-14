package compasses.expandedstorage.impl.mixin.common;

import compasses.expandedstorage.impl.misc.Utils;
import me.steven.carrier.HolderInteractCallback;
import me.steven.carrier.api.Carriable;
import me.steven.carrier.api.CarrierComponent;
import me.steven.carrier.api.CarryingData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = HolderInteractCallback.class, remap = false)
public class HijackBlockPickup {

    @Inject(
            method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/InteractionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/steven/carrier/api/Carriable;tryPickup(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/InteractionResult;"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void expandedstorage$earlyPickupReturn(
            Player player, Level world, InteractionHand hand, BlockPos pos, Direction hitDirection, boolean canPickup,
            CallbackInfoReturnable<InteractionResult> cir, BlockState blockState, Block block, CarrierComponent carrier,
            CarryingData carrying, ResourceLocation id, Carriable carriable, BlockEntity blockEntity
    ) {
        if (block.builtInRegistryHolder().key().location().getNamespace().equals(Utils.MOD_ID)) {
            InteractionResult result = carriable.tryPickup(player, world, pos, null);
            if (result.consumesAction()) {
                cir.setReturnValue(result);
            }
        }
    }
}
