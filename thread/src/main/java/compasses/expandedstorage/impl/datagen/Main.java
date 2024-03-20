package compasses.expandedstorage.impl.datagen;

import compasses.expandedstorage.impl.datagen.providers.BlockLootProvider;
import compasses.expandedstorage.impl.datagen.providers.BlockStateProvider;
import compasses.expandedstorage.impl.datagen.providers.RecipeProvider;
import compasses.expandedstorage.impl.datagen.providers.TagProvider;
import compasses.expandedstorage.impl.datagen.providers.ThreadConversionRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class Main implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(RecipeProvider::new);
        pack.addProvider(TagProvider.Block::new);
        pack.addProvider(TagProvider.Item::new);
        pack.addProvider(TagProvider.EntityTypes::new);
        pack.addProvider(BlockLootProvider::new);
        pack.addProvider(BlockStateProvider::new);
        pack.addProvider(ThreadConversionRecipeProvider::new);
    }
}
