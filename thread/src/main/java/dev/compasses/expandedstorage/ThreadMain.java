package dev.compasses.expandedstorage;

import dev.compasses.expandedstorage.registration.ModBlockEntities;
import dev.compasses.expandedstorage.registration.ModBlocks;
import dev.compasses.expandedstorage.registration.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

public class ThreadMain implements ModInitializer {
    @Override
    public void onInitialize() {
        ModBlocks.registerContent();
        ModBlockEntities.registerContent();
        ModItems.registerContent();
        ModItems.registerCreativeTab(FabricItemGroup.builder());
    }
}
