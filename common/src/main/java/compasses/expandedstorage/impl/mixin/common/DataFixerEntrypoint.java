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
    private static void expandedstorage$register1_17DataFixer(DataFixerBuilder builder, CallbackInfo ci, @Local(name = "schema140") Schema schema) {
        DataFixerUtils.register1_17DataFixer(builder, schema);
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
    private static void expandedstorage$register1_18DataFixer(DataFixerBuilder builder, CallbackInfo ci, @Local(name = "schema152") Schema schema) {
        DataFixerUtils.register1_18DataFixer(builder, schema);
    }
}
