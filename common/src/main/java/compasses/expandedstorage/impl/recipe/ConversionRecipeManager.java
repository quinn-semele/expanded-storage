package compasses.expandedstorage.impl.recipe;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ConversionRecipeManager {
    public static final ConversionRecipeManager INSTANCE = new ConversionRecipeManager();

    private final List<BlockConversionRecipe<?>> blockRecipes = new ArrayList<>();
    private final List<EntityConversionRecipe<?>> entityRecipes = new ArrayList<>();

    public BlockConversionRecipe<?> getBlockRecipe(BlockState state, ItemStack tool) {
        return blockRecipes.stream().map(recipe -> Map.entry(recipe, recipe.getRecipeWeight(state, tool)))
                           .filter(entry -> entry.getValue() > 0)
                           .max(Comparator.comparingInt(Map.Entry::getValue))
                           .map(Map.Entry::getKey)
                           .orElse(null);
    }

    public EntityConversionRecipe<?> getEntityRecipe(Entity entity, ItemStack tool) {
        return entityRecipes.stream().map(recipe -> Map.entry(recipe, recipe.getRecipeWeight(entity, tool)))
                            .filter(entry -> entry.getValue() > 0)
                            .max(Comparator.comparingInt(Map.Entry::getValue))
                            .map(Map.Entry::getKey)
                            .orElse(null);
    }

    public List<BlockConversionRecipe<?>> getBlockRecipes() {
        return Collections.unmodifiableList(blockRecipes);
    }

    public List<EntityConversionRecipe<?>> getEntityRecipes() {
        return Collections.unmodifiableList(entityRecipes);
    }

    public void replaceAllRecipes(List<BlockConversionRecipe<?>> blockRecipes, List<EntityConversionRecipe<?>> entityRecipes) {
        this.blockRecipes.clear();
        this.blockRecipes.addAll(blockRecipes);
        this.entityRecipes.clear();
        this.entityRecipes.addAll(entityRecipes);
    }
}
