package compasses.expandedstorage.impl.mixin.client;

import compasses.expandedstorage.impl.client.gui.PageScreen;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PageScreen.class)
@IPNGuiHint(button = IPNButton.MOVE_TO_CONTAINER, horizontalOffset = 58)
public class InventoryProfilesNextSupport {
}
