package compasses.expandedstorage.impl.datagen.providers;

import compasses.expandedstorage.impl.datagen.content.ThreadTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
        RecipeHelper recipeHelper = new RecipeHelper(
                BuiltInRegistries.ITEM::getKey,
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
