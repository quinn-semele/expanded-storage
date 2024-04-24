package compasses.expandedstorage.impl.datagen.providers;

import compasses.expandedstorage.impl.recipe.conditions.IsInTagCondition;
import compasses.expandedstorage.impl.recipe.conditions.OrCondition;
import compasses.expandedstorage.impl.recipe.conditions.RecipeCondition;
import compasses.expandedstorage.impl.datagen.content.ThreadTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;

public class ThreadConversionRecipeProvider extends ConversionRecipeProvider {
    public ThreadConversionRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void registerBlockRecipes() {
        super.registerBlockRecipes(
                new OrCondition(new IsInTagCondition(ConventionalBlockTags.WOODEN_BARRELS), RecipeCondition.IS_WOODEN_BARREL),
                new OrCondition(new IsInTagCondition(ConventionalBlockTags.WOODEN_CHESTS), RecipeCondition.IS_WOODEN_CHEST)
        );
    }

    @Override
    protected void registerEntityRecipes() {
        super.registerEntityRecipes(new IsInTagCondition(ThreadTags.Entities.WOODEN_CHEST_MINECARTS));
    }
}
