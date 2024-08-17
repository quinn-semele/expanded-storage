package dev.compasses.expandedstorage.client;

import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.client.render.ChestBlockRenderer;
import dev.compasses.expandedstorage.registration.ModBlockEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = Utils.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeClient {
    public NeoForgeClient(IEventBus modBus, ModContainer mod) {
        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> {
            event.registerBlockEntityRenderer(ModBlockEntities.CHEST, ChestBlockRenderer::new);
        });

        modBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) -> {
            event.registerLayerDefinition(ChestBlockRenderer.SINGLE_LID_LAYER, ChestBlockRenderer::createSingleLidLayer);
        });
    }
}
