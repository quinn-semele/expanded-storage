package compasses.expandedstorage.impl.datagen.providers;

import compasses.expandedstorage.impl.datagen.content.ThreadTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateRecipes(Consumer<FinishedRecipe> exporter) {
        RecipeHelper recipeHelper = new RecipeHelper(
                Registry.ITEM::getKey,
                ThreadTags.Items.COPPER_INGOTS, ThreadTags.Items.IRON_NUGGETS, ThreadTags.Items.IRON_INGOTS, ThreadTags.Items.GOLD_INGOTS, ThreadTags.Items.DIAMONDS, ThreadTags.Items.OBSIDIAN, ThreadTags.Items.NETHERITE_INGOTS,
                ThreadTags.Items.WOODEN_CHESTS, ThreadTags.Items.WOODEN_BARRELS,
                ThreadTags.Items.GLASS_BLOCKS, ThreadTags.Items.RED_DYES, ThreadTags.Items.WHITE_DYES, ThreadTags.Items.BAMBOO
        );

        recipeHelper.registerRecipes(exporter);
    }

    @NotNull
    @Override
    public String getName() {
        return "Expanded Storage - Recipes";
    }
}
