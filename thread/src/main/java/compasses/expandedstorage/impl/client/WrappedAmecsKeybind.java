package compasses.expandedstorage.impl.client;

import com.mojang.blaze3d.platform.InputConstants;
import compasses.expandedstorage.impl.misc.Utils;
import de.siphalor.amecs.api.AmecsKeyBinding;
import de.siphalor.amecs.api.KeyModifiers;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class WrappedAmecsKeybind implements Keybinding {
    private final KeyMapping binding = KeyBindingHelper.registerKeyBinding(new AmecsKeyBinding(Utils.id("open_config_screen"), InputConstants.Type.KEYSYM, Utils.KEY_BIND_KEY, KeyMapping.CATEGORY_INVENTORY, new KeyModifiers().setShift(true)));

    @Override
    public boolean matches(int keyCode, int scanCode) {
        return binding.matches(keyCode, scanCode);
    }
}
