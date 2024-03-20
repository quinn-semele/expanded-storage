package compasses.expandedstorage.impl.client.config;

import compasses.expandedstorage.impl.client.config.ConfigV0;
import compasses.expandedstorage.impl.client.config.LegacyFactory;
import compasses.expandedstorage.impl.misc.ConfigWrapper;

import java.nio.file.Path;

public final class ConfigWrapperImpl extends ConfigWrapper {
    public ConfigWrapperImpl(Path configPath, Path oldConfigPath) {
        super(configPath, oldConfigPath);
    }

    @Override
    protected ConfigV0 readOldConfig(String configLines, Path oldConfigPath) {
        return this.convertToConfig(configLines, LegacyFactory.INSTANCE, oldConfigPath);
    }
}
