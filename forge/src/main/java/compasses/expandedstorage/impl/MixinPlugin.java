package compasses.expandedstorage.impl;

import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    private static final int MIXIN_PACKAGE_LENGTH = "compasses.expandedstorage.impl.mixin".length() + 1;

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        boolean isQuarkPresent = FMLLoader.getLoadingModList().getModFileById("quark") != null;
        boolean isCarryOnPresent = FMLLoader.getLoadingModList().getModFileById("carryon") != null;
        String className = mixinClassName.substring(MIXIN_PACKAGE_LENGTH);
        return switch (className) {
            case "client.QuarkButtonAllowedMixin" -> isQuarkPresent;
            case "common.CarryOnCompatFix" -> isCarryOnPresent;
            default -> true;
        };
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
