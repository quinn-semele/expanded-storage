package compasses.expandedstorage.impl.mixin.client;

import compasses.expandedstorage.impl.client.gui.SizedSimpleTexture;
import compasses.expandedstorage.impl.client.helpers.ErrorlessTextureGetter;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.util.Map;

@Mixin(TextureManager.class)
public abstract class ErrorlessTextureManager implements ErrorlessTextureGetter {
    @Shadow
    @Final
    private Map<ResourceLocation, AbstractTexture> byPath;

    @Shadow
    @Final
    private ResourceManager resourceManager;

    @Override
    public boolean expandedstorage$isTexturePresent(ResourceLocation location) {
        AbstractTexture texture = byPath.get(location);
        if (texture == null) {
            texture = new SizedSimpleTexture(location);
            try {
                texture.load(resourceManager);
                byPath.put(location, texture);
            } catch (IOException ignored) {

            }
        }

        return byPath.get(location) != null;
    }
}
