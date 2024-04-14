package compasses.expandedstorage.impl.mixin.common;

import com.mojang.datafixers.DataFixerBuilder;
import compasses.expandedstorage.impl.fixer.DataFixerUtils;
import net.minecraft.util.datafix.DataFixers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataFixers.class)
public abstract class DataFixerEntrypoint {
    @Inject(
            method = "addFixers(Lcom/mojang/datafixers/DataFixerBuilder;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/datafixers/DataFixerBuilder;addSchema(ILjava/util/function/BiFunction;)Lcom/mojang/datafixers/schemas/Schema;",
                    ordinal = 132,
                    remap = false
            )
    )
    private static void expandedstorage$register1_17DataFixer(DataFixerBuilder builder, CallbackInfo ci) {
        DataFixerUtils.register1_17DataFixer(builder, 2707, 1);
    }

    @Inject(
            method = "addFixers(Lcom/mojang/datafixers/DataFixerBuilder;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/datafixers/DataFixerBuilder;addSchema(ILjava/util/function/BiFunction;)Lcom/mojang/datafixers/schemas/Schema;",
                    ordinal = 144,
                    remap = false
            )
    )
    private static void expandedstorage$register1_18DataFixer(DataFixerBuilder builder, CallbackInfo ci) {
        DataFixerUtils.register1_18DataFixer(builder, 2852, 1);
    }
}
