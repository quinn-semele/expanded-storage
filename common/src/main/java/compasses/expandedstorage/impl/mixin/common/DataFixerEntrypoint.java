package compasses.expandedstorage.impl.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
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
                    ordinal = 133,
                    remap = false
            )
    )
    private static void expandedstorage$register1_17DataFixer(DataFixerBuilder builder, CallbackInfo ci, @Local(ordinal = 139) Schema schema2707) {
        DataFixerUtils.register1_17DataFixer(builder, schema2707);
    }

    @Inject(
            method = "addFixers(Lcom/mojang/datafixers/DataFixerBuilder;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/datafixers/DataFixerBuilder;addSchema(ILjava/util/function/BiFunction;)Lcom/mojang/datafixers/schemas/Schema;",
                    ordinal = 145,
                    remap = false
            )
    )
    private static void expandedstorage$register1_18DataFixer(DataFixerBuilder builder, CallbackInfo ci, @Local(ordinal = 151) Schema schema2852) {
        DataFixerUtils.register1_18DataFixer(builder, schema2852);
    }
}
