package compasses.expandedstorage.impl.datagen;

import compasses.expandedstorage.impl.datagen.providers.ForgeConversionRecipeProvider;
import compasses.expandedstorage.impl.datagen.providers.ItemModelProvider;
import compasses.expandedstorage.impl.datagen.providers.LootTableProvider;
import compasses.expandedstorage.impl.datagen.providers.RecipeProvider;
import compasses.expandedstorage.impl.datagen.providers.TagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Main {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        final ExistingFileHelper fileHelper = event.getExistingFileHelper();
        final TagProvider.Block blockTagsProvider = new TagProvider.Block(generator, fileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new TagProvider.Item(generator, blockTagsProvider, fileHelper));
        generator.addProvider(event.includeServer(), new TagProvider.EntityType(generator, fileHelper));
        generator.addProvider(event.includeServer(), new RecipeProvider(generator));
        generator.addProvider(event.includeServer(), new LootTableProvider(generator));
        generator.addProvider(event.includeClient(), new ItemModelProvider(generator, fileHelper));
        generator.addProvider(event.includeServer(), new ForgeConversionRecipeProvider(generator));
    }
}
