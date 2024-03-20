package compasses.expandedstorage.impl.mixin.common;

import compasses.expandedstorage.impl.inventory.OpenableInventoryProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class SpectatorEntityInventoryViewingFix {
    @Inject(
            method = "interactOn(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void expandedstorage$idk(Entity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Player self = (Player) (Object) this;
        if (self.isSpectator()) {
            if (entity instanceof OpenableInventoryProvider<?>) {
                entity.interact(self, hand);
                cir.setReturnValue(InteractionResult.PASS);
            }
        }
    }
}
