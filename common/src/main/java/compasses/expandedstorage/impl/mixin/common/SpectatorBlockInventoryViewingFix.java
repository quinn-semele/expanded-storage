package compasses.expandedstorage.impl.mixin.common;

import compasses.expandedstorage.impl.inventory.OpenableInventoryProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerGameMode.class)
public abstract class SpectatorBlockInventoryViewingFix {
    @Inject(
            method = "useItemOn(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getMenuProvider(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/MenuProvider;"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void expandedstorage$beforeVanillaSpectatorInventoryViewing(
            ServerPlayer player, Level level, ItemStack handStack, InteractionHand hand, BlockHitResult blockHit, CallbackInfoReturnable<InteractionResult> cir,
            BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof OpenableInventoryProvider<?>) {
            state.use(level, player, hand, blockHit);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
