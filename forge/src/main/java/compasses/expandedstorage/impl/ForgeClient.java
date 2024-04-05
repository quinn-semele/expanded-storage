package compasses.expandedstorage.impl;

import compasses.expandedstorage.impl.client.ChestBlockEntityRenderer;
import compasses.expandedstorage.impl.client.gui.PageScreen;
import compasses.expandedstorage.impl.client.gui.PickScreen;
import compasses.expandedstorage.impl.entity.ChestMinecart;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.registration.Content;
import compasses.expandedstorage.impl.registration.ModItems;
import compasses.expandedstorage.impl.registration.NamedValue;
import compasses.expandedstorage.impl.misc.ForgeClientHelper;
import compasses.expandedstorage.impl.client.gui.AbstractScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ForgeClient {
    public static void initialize(IEventBus modBus, Content content) {
        CommonClient.initialize(new ForgeClientHelper(modBus));
        ModLoadingContext.get().getActiveContainer().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((client, screen) -> new PickScreen(screen))
        );

        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, (ScreenEvent.Init.Post event) -> {
            if (event.getScreen() instanceof PageScreen screen) {
                screen.addPageButtons();
            }
        });

        modBus.addListener((FMLClientSetupEvent event) -> {
            MenuScreens.register(CommonMain.platformHelper().getScreenHandlerType(), AbstractScreen::createScreen);
            ItemProperties.registerGeneric(Utils.id("sparrow"), CommonClient::hasSparrowProperty);
            ItemProperties.register(ModItems.STORAGE_MUTATOR, Utils.id("tool_mode"), CommonClient::currentMutatorToolMode);
        });

        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> {
            event.registerBlockEntityRenderer(content.getChestBlockEntityType().getValue(), ChestBlockEntityRenderer::new);
        });

        modBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) -> {
            event.registerLayerDefinition(ChestBlockEntityRenderer.SINGLE_LAYER, ChestBlockEntityRenderer::createSingleBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.LEFT_LAYER, ChestBlockEntityRenderer::createLeftBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.RIGHT_LAYER, ChestBlockEntityRenderer::createRightBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.TOP_LAYER, ChestBlockEntityRenderer::createTopBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.BOTTOM_LAYER, ChestBlockEntityRenderer::createBottomBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.FRONT_LAYER, ChestBlockEntityRenderer::createFrontBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.BACK_LAYER, ChestBlockEntityRenderer::createBackBodyLayer);
        });

        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> {
            for (NamedValue<EntityType<ChestMinecart>> type : content.getChestMinecartEntityTypes()) {
                event.registerEntityRenderer(type.getValue(), context -> new MinecartRenderer<>(context, ModelLayers.CHEST_MINECART));
            }
        });
    }
}
