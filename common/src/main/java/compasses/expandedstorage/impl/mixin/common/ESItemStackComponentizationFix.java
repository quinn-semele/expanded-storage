package compasses.expandedstorage.impl.mixin.common;

import com.mojang.serialization.Dynamic;
import compasses.expandedstorage.impl.item.MutationMode;
import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.stream.IntStream;

@Mixin(ItemStackComponentizationFix.class)
public class ESItemStackComponentizationFix {
    @Inject(
            method = "fixItemStack(Lnet/minecraft/util/datafix/fixes/ItemStackComponentizationFix$ItemStackData;Lcom/mojang/serialization/Dynamic;)V",
            at = @At("HEAD")
    )
    private static void expandedstorage$addESFixes(ItemStackComponentizationFix.ItemStackData itemStackData, Dynamic<?> dynamic, CallbackInfo ci) {
        if (itemStackData.is("expandedstorage:storage_mutator")) {
            MutationMode mode = itemStackData.removeTag("mode").result().map(it -> MutationMode.from(it.asByte((byte) 0))).orElse(MutationMode.MERGE);

            var result = dynamic.emptyMap().set("mode", dynamic.createString(mode.getSerializedName()));

            var posTag = itemStackData.removeTag("pos").result();
            if (posTag.isPresent()) {
                Map<String, Integer> posMap = posTag.get().asMap(d -> d.asString().getOrThrow(), d -> d.asNumber().map(Number::intValue).getOrThrow());
                result = result.set("pos", dynamic.createIntList(IntStream.of(posMap.get("X"), posMap.get("Y"), posMap.get("Z"))));
            }

            itemStackData.setComponent("expandedstorage:mutator_data", result);
        }
    }
}
