package compasses.expandedstorage.impl.mixin.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class SparrowItemIdFix {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(
            method = "of(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void expandedstorage$sparrowItemIdFix(CompoundTag tag, CallbackInfoReturnable<ItemStack> cir) {
        if (tag.contains("id", Tag.TAG_STRING)) {
            String id = tag.getString("id");
            if (id.startsWith("expandedstorage:") && id.endsWith("_with_sparrow")) {
                CompoundTag fixedTag = new CompoundTag();
                fixedTag.putString("id", id.substring(0, id.length() - 13));
                CompoundTag itemTag = new CompoundTag();
                CompoundTag properties = new CompoundTag();
                properties.putString("sparrow", "true");
                itemTag.put("BlockStateTag", properties);
                fixedTag.put("tag", itemTag);
                tag.merge(fixedTag);
                try {
                    cir.setReturnValue(new ItemStack(tag));
                } catch (RuntimeException e) {
                    LOGGER.debug("Expanded Storage: Tried to load invalid item: {}", tag, e);
                    cir.setReturnValue(ItemStack.EMPTY);
                }
            }
        }
    }
}
