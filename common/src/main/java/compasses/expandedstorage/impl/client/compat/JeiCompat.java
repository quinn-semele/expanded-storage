package compasses.expandedstorage.impl.client.compat;

import compasses.expandedstorage.impl.client.gui.FakePickScreen;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.client.gui.AbstractScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public final class JeiCompat implements IModPlugin {
    @NotNull
    @Override
    public ResourceLocation getPluginUid() {
        return Utils.id("jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(AbstractScreen.class, new IGuiContainerHandler<>() {
            @NotNull
            @Override
            public List<Rect2i> getGuiExtraAreas(AbstractScreen screen) {
                return screen.getExclusionZones();
            }
        });

        registration.addGuiScreenHandler(FakePickScreen.class, fakePickScreen -> null);
    }
}
