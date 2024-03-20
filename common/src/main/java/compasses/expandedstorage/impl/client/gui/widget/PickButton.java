package compasses.expandedstorage.impl.client.gui.widget;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public class PickButton {
    private final ResourceLocation texture;
    private final Component title;
    private final List<Component> warningText;

    public PickButton(ResourceLocation texture, Component title, Component... warningText) {
        this.texture = texture;
        this.title = title;
        this.warningText = Arrays.asList(warningText);
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public Component getTitle() {
        return title;
    }

    public boolean shouldShowWarning(int scaledWidth, int scaledHeight) {
        return false;
    }

    public List<Component> getWarningText() {
        return warningText;
    }
}
