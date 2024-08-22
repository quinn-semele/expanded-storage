package dev.compasses.expandedstorage.datagen;

import dev.compasses.expandedstorage.Utils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static net.neoforged.fml.common.EventBusSubscriber.Bus.MOD;

@EventBusSubscriber(modid = Utils.MOD_ID, bus = MOD)
public class NeoForgeData {
    @SubscribeEvent
    public static void gatherDataProviders(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        boolean useCommonDataProviders = System.getProperty("expandedstorage.datagen.common", "false").equals("true");

        if (useCommonDataProviders) {
            generator.addProvider(event.includeClient(), new CommonBlockStateAndModelsProvider(output, Utils.MOD_ID, fileHelper));
        } else { // NeoForge specific data providers

        }
    }
}
