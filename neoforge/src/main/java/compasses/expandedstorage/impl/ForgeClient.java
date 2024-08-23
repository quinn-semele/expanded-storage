package compasses.expandedstorage.impl;

import compasses.expandedstorage.impl.block.ChestBlock;
import compasses.expandedstorage.impl.block.entity.ChestBlockEntity;
import compasses.expandedstorage.impl.client.ChestBlockClientExtensions;
import compasses.expandedstorage.impl.client.ChestBlockEntityRenderer;
import compasses.expandedstorage.impl.client.ChestCartClientExtensions;
import compasses.expandedstorage.impl.client.gui.PageScreen;
import compasses.expandedstorage.impl.client.gui.PickScreen;
import compasses.expandedstorage.impl.entity.ChestMinecart;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.registration.Content;
import compasses.expandedstorage.impl.registration.ModItems;
import compasses.expandedstorage.impl.registration.NamedValue;
import compasses.expandedstorage.impl.misc.ForgeClientHelper;
import compasses.expandedstorage.impl.client.gui.AbstractScreen;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

import java.util.Map;
import java.util.stream.Collectors;

@Mod(value = "expandedstorage", dist = Dist.CLIENT)
public class ForgeClient {
    public ForgeClient(IEventBus bus, ModContainer mod) {
        ForgeMain mainEntryPoint = mod.getCustomExtension(ForgeMain.class).orElseThrow();
        Content content = mainEntryPoint.getContentForClient();

        CommonClient.initialize(new ForgeClientHelper(bus));
        ModLoadingContext.get().getActiveContainer().registerExtensionPoint(IConfigScreenFactory.class,
                (container, screen) -> new PickScreen(screen)
        );

        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, (ScreenEvent.Init.Post event) -> {
            if (event.getScreen() instanceof PageScreen screen) {
                screen.addPageButtons();
            }
        });

        bus.addListener((FMLClientSetupEvent event) -> {
            ItemProperties.registerGeneric(Utils.id("sparrow"), (ClampedItemPropertyFunction) CommonClient::hasSparrowProperty);
            ItemProperties.register(ModItems.STORAGE_MUTATOR, Utils.id("tool_mode"), (ClampedItemPropertyFunction) CommonClient::currentMutatorToolMode);
        });

        bus.addListener((RegisterMenuScreensEvent event) -> {
            event.register(CommonMain.platformHelper().getScreenHandlerType(), AbstractScreen::createScreen);
        });

        bus.addListener((EntityRenderersEvent.RegisterRenderers event) -> {
            event.registerBlockEntityRenderer(content.getChestBlockEntityType().getValue(), ChestBlockEntityRenderer::new);
        });

        bus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) -> {
            event.registerLayerDefinition(ChestBlockEntityRenderer.SINGLE_LAYER, ChestBlockEntityRenderer::createSingleBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.LEFT_LAYER, ChestBlockEntityRenderer::createLeftBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.RIGHT_LAYER, ChestBlockEntityRenderer::createRightBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.TOP_LAYER, ChestBlockEntityRenderer::createTopBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.BOTTOM_LAYER, ChestBlockEntityRenderer::createBottomBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.FRONT_LAYER, ChestBlockEntityRenderer::createFrontBodyLayer);
            event.registerLayerDefinition(ChestBlockEntityRenderer.BACK_LAYER, ChestBlockEntityRenderer::createBackBodyLayer);
        });

        bus.addListener((EntityRenderersEvent.RegisterRenderers event) -> {
            for (NamedValue<EntityType<ChestMinecart>> type : content.getChestMinecartEntityTypes()) {
                event.registerEntityRenderer(type.getValue(), context -> new MinecartRenderer<>(context, ModelLayers.CHEST_MINECART));
            }
        });





        bus.addListener((RegisterClientExtensionsEvent event) -> {
            final Map<ChestBlock, ChestBlockEntity> blockRenderers = content
                    .getChestBlocks().stream()
                    .map(NamedValue::getValue)
                    .map(block -> Map.entry(block, CommonMain.getChestBlockEntityType().create(BlockPos.ZERO, block.defaultBlockState())))
                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

            event.registerItem(new ChestBlockClientExtensions(blockRenderers), content.getChestItems().stream().map(NamedValue::getValue).toArray(Item[]::new));

            final Map<Item, EntityType<ChestMinecart>> entityRenderers = content
                    .getChestMinecartAndTypes().stream()
                    .map(entry -> Map.entry(entry.getKey().getValue(), entry.getValue().getValue()))
                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

            event.registerItem(new ChestCartClientExtensions(entityRenderers), content.getChestMinecartAndTypes().stream().map(Map.Entry::getKey).map(NamedValue::getValue).toArray(Item[]::new));
        });
    }
}
