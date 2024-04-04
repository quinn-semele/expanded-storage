package compasses.expandedstorage.impl.misc;

import com.mojang.blaze3d.platform.InputConstants;
import compasses.expandedstorage.impl.client.config.ConfigWrapper;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

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
