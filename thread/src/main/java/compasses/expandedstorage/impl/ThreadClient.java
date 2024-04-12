package compasses.expandedstorage.impl;

import com.google.common.base.Suppliers;
import compasses.expandedstorage.impl.block.entity.ChestBlockEntity;
import compasses.expandedstorage.impl.client.ChestBlockEntityRenderer;
import compasses.expandedstorage.impl.client.compat.inventory_tabs.ExpandedBlockTabProvider;
import compasses.expandedstorage.impl.entity.ChestMinecart;
import compasses.expandedstorage.impl.item.ChestMinecartItem;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.networking.UpdateRecipesPacketPayload;
import compasses.expandedstorage.impl.recipe.ConversionRecipeManager;
import compasses.expandedstorage.impl.registration.ModItems;
import compasses.expandedstorage.impl.client.gui.AbstractScreen;
import compasses.expandedstorage.impl.registration.NamedValue;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ThreadClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CommonClient.initialize(new ThreadClientHelper());
        MenuScreens.register(CommonMain.platformHelper().getScreenHandlerType(), AbstractScreen::createScreen);

        var content = ThreadMain.getContentForClient();

        ThreadClient.registerChestBlockEntityRenderer();
        ThreadClient.registerItemRenderers(content.getChestItems());
        ThreadClient.registerMinecartEntityRenderers(content.getChestMinecartEntityTypes());
        ThreadClient.registerMinecartItemRenderers(content.getChestMinecartAndTypes());
        ThreadClient.registerInventoryTabsCompat();

        ItemProperties.registerGeneric(Utils.id("sparrow"), CommonClient::hasSparrowProperty);
        ItemProperties.register(ModItems.STORAGE_MUTATOR, Utils.id("tool_mode"), CommonClient::currentMutatorToolMode);

        ClientPlayConnectionEvents.INIT.register((_listener, _client) -> {
            ClientPlayNetworking.registerReceiver(UpdateRecipesPacketPayload.TYPE, ThreadClient::handleUpdateRecipesPacket);
        });
    }

    @SuppressWarnings("unused")
    public static void handleUpdateRecipesPacket(UpdateRecipesPacketPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> ConversionRecipeManager.INSTANCE.replaceAllRecipes(payload.blockRecipes(), payload.entityRecipes()));
    }

    public static void registerChestBlockEntityRenderer() {
        BlockEntityRenderers.register(CommonMain.getChestBlockEntityType(), ChestBlockEntityRenderer::new);
    }

    public static void registerItemRenderers(List<NamedValue<BlockItem>> items) {
        for (NamedValue<BlockItem> item : items) {
            ChestBlockEntity renderEntity = CommonMain.getChestBlockEntityType().create(BlockPos.ZERO, item.getValue().getBlock().defaultBlockState());

            BuiltinItemRendererRegistry.INSTANCE.register(item.getValue(), (itemStack, context, stack, source, light, overlay) -> {
                renderEntity.setCustomName(itemStack.getHoverName());
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(renderEntity, stack, source, light, overlay);
            });
        }

        EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.SINGLE_LAYER, ChestBlockEntityRenderer::createSingleBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.LEFT_LAYER, ChestBlockEntityRenderer::createLeftBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.RIGHT_LAYER, ChestBlockEntityRenderer::createRightBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.TOP_LAYER, ChestBlockEntityRenderer::createTopBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.BOTTOM_LAYER, ChestBlockEntityRenderer::createBottomBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.FRONT_LAYER, ChestBlockEntityRenderer::createFrontBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ChestBlockEntityRenderer.BACK_LAYER, ChestBlockEntityRenderer::createBackBodyLayer);
    }

    public static void registerMinecartEntityRenderers(List<NamedValue<EntityType<ChestMinecart>>> chestMinecartEntityTypes) {
        for (NamedValue<EntityType<ChestMinecart>> type : chestMinecartEntityTypes) {
            EntityRendererRegistry.register(type.getValue(), context -> new MinecartRenderer<>(context, ModelLayers.CHEST_MINECART));
        }
    }

    public static void registerMinecartItemRenderers(List<Map.Entry<NamedValue<ChestMinecartItem>, NamedValue<EntityType<ChestMinecart>>>> chestMinecartAndTypes) {
        for (var pair : chestMinecartAndTypes) {
            Supplier<ChestMinecart> renderEntity = Suppliers.memoize(() -> pair.getValue().getValue().create(Minecraft.getInstance().level));

            BuiltinItemRendererRegistry.INSTANCE.register(pair.getKey().getValue(), (itemStack, transform, stack, source, light, overlay) ->
                    Minecraft.getInstance().getEntityRenderDispatcher().render(renderEntity.get(), 0, 0, 0, 0, 0, stack, source, light));
        }
    }

    public static void registerInventoryTabsCompat() {
        if (FabricLoader.getInstance().isModLoaded("inventory-tabs")) {
            ExpandedBlockTabProvider.register();
        }
    }
}
