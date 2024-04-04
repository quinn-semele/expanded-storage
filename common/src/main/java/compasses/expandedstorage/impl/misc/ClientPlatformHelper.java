package compasses.expandedstorage.impl.misc;

import compasses.expandedstorage.impl.client.config.ConfigWrapper;

public interface ClientPlatformHelper {
    boolean isConfigKeyPressed(int keyCode, int scanCode, int modifiers);

    boolean isModLoaded(String modId);

    ConfigWrapper configWrapper();
}
