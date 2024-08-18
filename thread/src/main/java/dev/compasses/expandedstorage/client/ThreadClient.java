package dev.compasses.expandedstorage.client;

import dev.compasses.expandedstorage.client.render.ChestBlockRenderer;
import dev.compasses.expandedstorage.registration.ModBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class ThreadClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(ModBlockEntities.CHEST, ChestBlockRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(ChestBlockRenderer.SINGLE_LID_LAYER, ChestBlockRenderer::createSingleLidLayer);
    }
}
