package compasses.expandedstorage.impl.mixin.common;

import net.minecraft.core.HolderLookup;
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

import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class SparrowItemIdFix {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(
            method = "parse(Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/nbt/Tag;)Ljava/util/Optional;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void expandedstorage$sparrowItemIdFix(HolderLookup.Provider provider, Tag tag, CallbackInfoReturnable<Optional<ItemStack>> cir) {
        if (tag instanceof CompoundTag compoundTag) {
            if (compoundTag.contains("id", Tag.TAG_STRING)) {
                String id = compoundTag.getString("id");
                if (id.startsWith("expandedstorage:") && id.endsWith("_with_sparrow")) {
                    CompoundTag fixedTag = new CompoundTag();
                    fixedTag.putString("id", id.substring(0, id.length() - 13));
                    CompoundTag itemTag = new CompoundTag();
                    CompoundTag properties = new CompoundTag();
                    properties.putString("sparrow", "true");
                    itemTag.put("BlockStateTag", properties);
                    fixedTag.put("tag", itemTag);
                    compoundTag.merge(fixedTag);

                    try {
                        cir.setReturnValue(ItemStack.parse(provider, compoundTag));
                    } catch (RuntimeException e) {
                        LOGGER.debug("Expanded Storage: Tried to load invalid item: {}", compoundTag, e);
                        cir.setReturnValue(Optional.empty());
                    }
                }
            }
        }
    }
}
