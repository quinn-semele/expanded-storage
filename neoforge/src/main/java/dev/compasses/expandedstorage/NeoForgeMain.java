package dev.compasses.expandedstorage;

import dev.compasses.expandedstorage.registration.ModBlockEntities;
import dev.compasses.expandedstorage.registration.ModBlocks;
import dev.compasses.expandedstorage.registration.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Utils.MOD_ID)
public class NeoForgeMain {
    public NeoForgeMain(IEventBus modBus, ModContainer mod) {
        modBus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey() == Registries.BLOCK) {
                ModBlocks.registerContent();
            } else if (event.getRegistryKey() == Registries.ITEM) {
                ModItems.registerContent();
            } else if (event.getRegistryKey() == Registries.BLOCK_ENTITY_TYPE) {
                ModBlockEntities.registerContent();
            } else if (event.getRegistryKey() == Registries.CREATIVE_MODE_TAB) {
                ModItems.registerCreativeTab(CreativeModeTab.builder());
            }
        });
    }
}