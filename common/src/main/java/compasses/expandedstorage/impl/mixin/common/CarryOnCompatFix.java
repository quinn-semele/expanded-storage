package compasses.expandedstorage.impl.mixin.common;

import compasses.expandedstorage.impl.block.AbstractChestBlock;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tschipp.carryon.common.config.ListHandler;

import java.util.Set;

@Mixin(value = ListHandler.class, remap = false)
public class CarryOnCompatFix {

    @Shadow
    private static Set<Property<?>> PROPERTY_EXCEPTION_CLASSES;

    @Inject(
            method = "initConfigLists()V",
            at = @At("TAIL"),
            remap = false
    )
    private static void expandedstorage$addChestPropertiesToIgnore(CallbackInfo ci) {
        PROPERTY_EXCEPTION_CLASSES.add(AbstractChestBlock.CURSED_CHEST_TYPE);
    }
}
