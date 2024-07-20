package compasses.expandedstorage.impl.misc;

import com.mojang.blaze3d.platform.InputConstants;
import compasses.expandedstorage.impl.client.config.ConfigWrapper;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.nio.file.Path;

public class ForgeClientHelper implements ClientPlatformHelper {
    private final ConfigWrapperImpl configWrapper;
    private final KeyMapping binding = new KeyMapping("key.expandedstorage.config", KeyConflictContext.GUI, KeyModifier.SHIFT, InputConstants.Type.KEYSYM, Utils.KEY_BIND_KEY, "key.categories.inventory");

    public ForgeClientHelper(IEventBus modBus) {
        modBus.addListener((RegisterKeyMappingsEvent event) -> event.register(binding));

        Path configDir = FMLPaths.CONFIGDIR.get();
        configWrapper = new ConfigWrapperImpl(configDir.resolve(Utils.CONFIG_PATH), configDir.resolve("expandedstorage-client.toml"));
    }

    @Override
    public boolean isConfigKeyPressed(int keyCode, int scanCode, int modifiers) {
        return binding.matches(keyCode, scanCode);
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public ConfigWrapper configWrapper() {
        return configWrapper;
    }
}
