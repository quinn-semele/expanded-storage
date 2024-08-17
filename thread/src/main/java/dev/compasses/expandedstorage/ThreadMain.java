package dev.compasses.expandedstorage;

import dev.compasses.expandedstorage.registration.ModBlockEntities;
import dev.compasses.expandedstorage.registration.ModBlocks;
import dev.compasses.expandedstorage.registration.ModItems;
import net.fabricmc.api.ModInitializer;

public class ThreadMain implements ModInitializer {
    @Override
    public void onInitialize() {
        ModBlocks.registerContent();
        ModBlockEntities.registerContent();
        ModItems.registerContent();
    }
}
