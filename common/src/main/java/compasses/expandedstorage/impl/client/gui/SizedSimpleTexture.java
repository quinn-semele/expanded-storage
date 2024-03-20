package compasses.expandedstorage.impl.client.gui;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SizedSimpleTexture extends SimpleTexture {
    private int width;
    private int height;

    public SizedSimpleTexture(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @NotNull
    @Override
    protected TextureImage getTextureImage(ResourceManager resourceManager) {
        TextureImage rv = super.getTextureImage(resourceManager);
        try {
            NativeImage image = rv.getImage();
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException ignored) {

        }
        return rv;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
