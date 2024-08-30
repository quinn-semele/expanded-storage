package dev.compasses.expandedstorage.client;

import dev.compasses.expandedstorage.client.render.ChestBlockRenderer;
import dev.compasses.expandedstorage.client.render.ShulkerBoxRenderer;
import dev.compasses.expandedstorage.registration.ModBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class ThreadClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(ModBlockEntities.CHEST, ChestBlockRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.SHULKER_BOX, ShulkerBoxRenderer::new);
    }
}
