package compasses.expandedstorage.impl.client;

import compasses.expandedstorage.impl.misc.Utils;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;

public class WrappedVanillaKeybind implements Keybinding {
    private final KeyMapping binding = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.expandedstorage.open_config_screen", Utils.KEY_BIND_KEY, KeyMapping.CATEGORY_INVENTORY));

    @Override
    public boolean matches(int keyCode, int scanCode) {
        return binding.matches(keyCode, scanCode) && Screen.hasShiftDown();
    }
}
