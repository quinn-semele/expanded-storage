package compasses.expandedstorage.impl.mixin.client;

import compasses.expandedstorage.impl.client.gui.PageScreen;
import compasses.expandedstorage.impl.client.gui.ScrollScreen;
import compasses.expandedstorage.impl.client.gui.SingleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.violetmoon.quark.api.IQuarkButtonAllowed;

@Mixin(value = {PageScreen.class, SingleScreen.class, ScrollScreen.class}, remap = false)
public class QuarkButtonAllowedMixin implements IQuarkButtonAllowed {

}
