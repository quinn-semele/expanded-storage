package compasses.expandedstorage.impl.client.compat;

import compasses.expandedstorage.impl.block.MiniStorageBlock;
import compasses.expandedstorage.impl.client.gui.FakePickScreen;
import compasses.expandedstorage.impl.client.gui.AbstractScreen;
import compasses.expandedstorage.impl.misc.Utils;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.OverlayDecider;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public final class ReiCompat implements REIClientPlugin {
    private static Rectangle asReiRectangle(Rect2i rect) {
        return new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    @Override
    public void registerExclusionZones(ExclusionZones zones) {
        zones.register(AbstractScreen.class, (AbstractScreen screen) -> CollectionUtils.map(screen.getExclusionZones(), ReiCompat::asReiRectangle));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerDecider(new OverlayDecider() {
            @Override
            public <R extends Screen> boolean isHandingScreen(Class<R> screen) {
                return screen == FakePickScreen.class;
            }

            @Override
            public <R extends Screen> InteractionResult shouldScreenBeOverlaid(R screen) {
                return InteractionResult.FAIL;
            }
        });
    }

    @Override
    public void registerEntries(EntryRegistry registry) {
        registry.removeEntryIf(entry -> {
            if (Utils.MOD_ID.equals(entry.getContainingNamespace())) {
                if (entry.getValue() instanceof ItemStack stack) {
                    if (stack.getItem() instanceof BlockItem item) {
                        if (item.getBlock() instanceof MiniStorageBlock) {
                            return MiniStorageBlock.hasSparrowProperty(stack);
                        }
                    }
                }
            }

            return false;
        });
    }
}
