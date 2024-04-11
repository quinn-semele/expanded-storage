package compasses.expandedstorage.impl.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public abstract class BlockEntityIdFix {
    @Inject(
            method = "loadStatic(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/level/block/entity/BlockEntity;",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void expandedstorage$fixChestIds(BlockPos pos, BlockState state, CompoundTag tag, HolderLookup.Provider provider, CallbackInfoReturnable<BlockEntity> cir) {
        String id = tag.getString("id");
        if (id.equals("expandedstorage:cursed_chest")) {
            tag.putString("id", "expandedstorage:chest");
            cir.setReturnValue(BlockEntity.loadStatic(pos, state, tag, provider));
        } else if (id.equals("expandedstorage:old_cursed_chest")) {
            tag.putString("id", "expandedstorage:old_chest");
            cir.setReturnValue(BlockEntity.loadStatic(pos, state, tag, provider));
        }
    }
}
