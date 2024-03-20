package compasses.expandedstorage.forge.misc;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import compasses.expandedstorage.impl.client.config.ConfigV0;
import compasses.expandedstorage.impl.misc.ConfigWrapper;
import compasses.expandedstorage.forge.config.LegacyFactory;

import java.io.StringReader;
import java.nio.file.Path;

public final class ConfigWrapperImpl extends ConfigWrapper {
    public ConfigWrapperImpl(Path configPath, Path oldConfigPath) {
        super(configPath, oldConfigPath);
    }

    @Override
    protected ConfigV0 readOldConfig(String configLines, Path oldConfigPath) {
        CommentedConfig tomlConfig = TomlFormat.instance().createParser().parse(new StringReader(configLines));
        return this.convert(tomlConfig, -1, LegacyFactory.INSTANCE);
    }
}
