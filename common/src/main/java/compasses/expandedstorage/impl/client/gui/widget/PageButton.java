package compasses.expandedstorage.impl.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class PageButton extends Button {
    private static final ResourceLocation TEXTURE = Utils.id("textures/gui/page_buttons.png");
    private final int textureOffset;

    public PageButton(int x, int y, int textureOffset, Component message, OnPress onPress) {
        super(x, y, 12, 12, message, onPress, DEFAULT_NARRATION);
        this.textureOffset = textureOffset;
        this.setTooltip(Tooltip.create(message));
    }

    public void setActive(boolean active) {
        this.active = active;
        if (!active) {
            this.setFocused(false);
        }
    }

    private int getTextureY() {
        if (!this.active) {
            return 0;
        } else if (this.isHoveredOrFocused()) {
            return 24;
        }
        return 12;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        graphics.blit(TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight(), textureOffset * 12, this.getTextureY(), this.getWidth(), this.getHeight(), 32, 48);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
