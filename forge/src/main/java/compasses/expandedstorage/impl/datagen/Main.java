package compasses.expandedstorage.impl.datagen;

import compasses.expandedstorage.impl.datagen.providers.ForgeConversionRecipeProvider;
import compasses.expandedstorage.impl.datagen.providers.ItemModelProvider;
import compasses.expandedstorage.impl.datagen.providers.LootTableProvider;
import compasses.expandedstorage.impl.datagen.providers.RecipeProvider;
import compasses.expandedstorage.impl.datagen.providers.TagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Main {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        final PackOutput output = generator.getPackOutput();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        final ExistingFileHelper fileHelper = event.getExistingFileHelper();
        final BlockTagsProvider blockTagsProvider = new TagProvider.Block(output, lookupProvider, fileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new TagProvider.Item(output, lookupProvider, blockTagsProvider, fileHelper));
        generator.addProvider(event.includeServer(), new TagProvider.EntityType(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new RecipeProvider(output));
        generator.addProvider(event.includeServer(), new LootTableProvider(output));
        generator.addProvider(event.includeClient(), new ItemModelProvider(output, fileHelper));
        generator.addProvider(event.includeServer(), new ForgeConversionRecipeProvider(output));
    }
}
