package compasses.expandedstorage.impl.mixin.common;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import compasses.expandedstorage.impl.fixer.DataFixerUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(value = Schema.class, remap = false)
public class DataFixerSchemaImpl {

    @Final
    @Shadow(remap = false)
    private int versionKey;

    @Inject(
            method = "registerBlockEntities(Lcom/mojang/datafixers/schemas/Schema;)Ljava/util/Map;",
            at = @At("RETURN"),
            remap = false
    )
    private void expandedstorage$registerBlockEntities(Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> cir) {
        DataFixerUtils.registerBlockEntities(versionKey, cir.getReturnValue(), schema);
    }
}
