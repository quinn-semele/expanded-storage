package compasses.expandedstorage.impl.datagen.providers;

import compasses.expandedstorage.impl.datagen.content.ThreadTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        RecipeHelper recipeHelper = new RecipeHelper(
                BuiltInRegistries.ITEM::getKey,
                ConventionalItemTags.COPPER_INGOTS, ThreadTags.Items.IRON_NUGGETS, ConventionalItemTags.IRON_INGOTS, ConventionalItemTags.GOLD_INGOTS, ConventionalItemTags.DIAMOND_GEMS, ThreadTags.Items.OBSIDIAN, ConventionalItemTags.NETHERITE_INGOTS,
                ConventionalItemTags.WOODEN_CHESTS, ConventionalItemTags.WOODEN_BARRELS,
                ConventionalItemTags.GLASS_BLOCKS, ConventionalItemTags.RED_DYES, ConventionalItemTags.WHITE_DYES, ThreadTags.Items.BAMBOO
        );

        recipeHelper.registerRecipes(output);
    }

    @NotNull
    @Override
    public String getName() {
        return "Expanded Storage - Recipes";
    }
}
