package compasses.expandedstorage.impl.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class ScreenPickButton extends Button {
    private static final ResourceLocation WARNING_TEXTURE = Utils.id("textures/gui/warning.png");
    private final ResourceLocation texture;
    private final boolean showWarningSymbol;
    private final boolean isCurrentPreference;

    public ScreenPickButton(int x, int y, int width, int height, ResourceLocation texture, Component message, boolean showWarningSymbol, boolean isCurrentPreference, OnPress onPress, Tooltip tooltip) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.texture = texture;
        this.showWarningSymbol = showWarningSymbol;
        this.isCurrentPreference = isCurrentPreference;
        this.setTooltip(tooltip);
    }

    private int getTextureY() {
        return height * (this.isHoveredOrFocused() ? 1 : isCurrentPreference ? 2 : 0);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        graphics.blit(texture, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0, this.getTextureY(), this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight() * 3);
        if (showWarningSymbol) {
            graphics.blit(WARNING_TEXTURE, this.getX() + width - 28, this.getY() + 9, 0, 0, 16, 32, 16, 32);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
