package compasses.expandedstorage.impl.config;

import com.electronwill.nightconfig.core.Config;
import compasses.expandedstorage.impl.client.config.ConfigV0;
import compasses.expandedstorage.impl.client.config.Converter;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class LegacyFactory implements Converter<Config, ConfigV0> {
    public static final LegacyFactory INSTANCE = new LegacyFactory();

    private LegacyFactory() {

    }

    @Nullable
    @Override
    public ConfigV0 fromSource(@Nullable Config source) {
        if (source != null) {
            if (source.get("client.preferred_container_type") instanceof String screenType &&
                    source.get("client.restrictive_scrolling") instanceof Boolean restrictiveScrolling) {
                if ("expandedstorage:paged".equals(screenType)) {
                    screenType = Utils.PAGINATED_SCREEN_TYPE.toString();
                } else if ("expandedstorage:scrollable".equals(screenType)) {
                    screenType = Utils.SCROLLABLE_SCREEN_TYPE.toString();
                }
                return new ConfigV0(ResourceLocation.tryParse(screenType), restrictiveScrolling, false, false);
            }
        }
        return null;
    }

    @Override
    public Config toSource(ConfigV0 target) {
        throw new UnsupportedOperationException("Legacy configs cannot be saved.");
    }

    @Override
    public int getSourceVersion() {
        return -1;
    }

    @Override
    public int getTargetVersion() {
        return 0;
    }
}
