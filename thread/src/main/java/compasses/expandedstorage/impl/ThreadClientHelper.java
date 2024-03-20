package compasses.expandedstorage.impl;

import compasses.expandedstorage.impl.misc.ClientPlatformHelper;
import compasses.expandedstorage.impl.misc.ConfigWrapper;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.client.Keybinding;
import compasses.expandedstorage.impl.client.WrappedAmecsKeybind;
import compasses.expandedstorage.impl.client.WrappedVanillaKeybind;
import compasses.expandedstorage.impl.client.config.ConfigWrapperImpl;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ThreadClientHelper implements ClientPlatformHelper {
    private final ConfigWrapperImpl configWrapper;
    private final Keybinding binding;

    protected ThreadClientHelper() {
        Path configDir = FabricLoader.getInstance().getConfigDir();

        configWrapper = new ConfigWrapperImpl(configDir.resolve(Utils.CONFIG_PATH), configDir.resolve("ninjaphenix-container-library.json"));

        if (isModLoaded("amecs")) {
            binding = new WrappedAmecsKeybind();
        } else {
            binding = new WrappedVanillaKeybind();
        }
    }

    @Override
    public boolean isConfigKeyPressed(int keyCode, int scanCode, int modifiers) {
        return binding.matches(keyCode, scanCode);
    }

    @Override
    public ConfigWrapper configWrapper() {
        return configWrapper;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
