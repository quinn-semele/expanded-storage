package compasses.expandedstorage.impl.client.config;

import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ConfigV0 implements Config {
    private final boolean restrictiveScrolling;
    private final boolean preferSmallerScreens;
    private ResourceLocation screenType;
    private final boolean fitVanillaConstraints;

    public ConfigV0() {
        this(null, false, true, false);
    }

    public ConfigV0(ResourceLocation screenType, boolean restrictiveScrolling, boolean preferSmallerScreens, boolean fitVanillaConstraints) {
        if (String.valueOf(screenType).equals("expandedstorage:auto")) {
            this.screenType = null;
        } else {
            this.screenType = screenType;
        }
        this.restrictiveScrolling = restrictiveScrolling;
        this.preferSmallerScreens = preferSmallerScreens;
        this.fitVanillaConstraints = fitVanillaConstraints;
    }

    public ResourceLocation getScreenType() {
        return screenType;
    }

    public void setScreenType(ResourceLocation screenType) {
        this.screenType = screenType;
    }

    public boolean isScrollingRestricted() {
        return restrictiveScrolling;
    }

    public boolean preferSmallerScreens() {
        return this.preferSmallerScreens;
    }

    public boolean fitVanillaConstraints() {
        return this.fitVanillaConstraints;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Converter<Map<String, Object>, ConfigV0> getConverter() {
        return Factory.INSTANCE;
    }

    public static final class Factory implements Converter<Map<String, Object>, ConfigV0> {
        public static final Factory INSTANCE = new Factory();

        private Factory() {

        }

        @Override
        public ConfigV0 fromSource(Map<String, Object> source) {
            if (source.get("container_type") instanceof String screenType && source.get("restrictive_scrolling") instanceof Boolean restrictiveScrolling) {
                Boolean preferSmallerScreens = Boolean.FALSE;
                if (source.containsKey("prefer_smaller_screens") && source.get("prefer_smaller_screens") instanceof Boolean bool) {
                    preferSmallerScreens = bool;
                }
                // Accidentally serialised this under the wrong name...
                if (source.containsKey("prefer_bigger_screens")  && source.get("prefer_bigger_screens") instanceof Boolean bool) {
                    preferSmallerScreens = bool;
                }

                ResourceLocation screenTypeRl = switch (screenType) {
                    case "expandedstorage:page" -> Utils.PAGINATED_SCREEN_TYPE;
                    case "expandedstorage:scroll" -> Utils.SCROLLABLE_SCREEN_TYPE;
                    default -> ResourceLocation.tryParse(screenType);
                };

                Boolean fitVanillaConstraints = Boolean.FALSE;
                if (source.containsKey("fit_vanilla_constraints")  && source.get("fit_vanilla_constraints") instanceof Boolean bool) {
                    fitVanillaConstraints = bool;
                }

                return new ConfigV0(screenTypeRl, restrictiveScrolling, preferSmallerScreens, fitVanillaConstraints);
            }
            return null;
        }

        @Override
        public Map<String, Object> toSource(ConfigV0 target) {
            Map<String, Object> values = new HashMap<>();
            values.put("container_type", target.screenType);
            values.put("restrictive_scrolling", target.restrictiveScrolling);
            values.put("prefer_smaller_screens", target.preferSmallerScreens);
            values.put("fit_vanilla_constraints", target.fitVanillaConstraints);
            return values;
        }

        @Override
        public int getSourceVersion() {
            return 0;
        }

        @Override
        public int getTargetVersion() {
            return 0;
        }
    }
}
