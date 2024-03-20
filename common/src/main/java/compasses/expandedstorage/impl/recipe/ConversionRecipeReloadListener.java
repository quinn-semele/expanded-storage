package compasses.expandedstorage.impl.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import compasses.expandedstorage.impl.recipe.conditions.RecipeCondition;
import compasses.expandedstorage.impl.recipe.misc.JsonHelper;
import compasses.expandedstorage.impl.recipe.misc.PartialBlockState;
import compasses.expandedstorage.impl.recipe.misc.RecipeTool;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConversionRecipeReloadListener extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger("expanded-storage");
    private final List<BlockConversionRecipe<?>> blockRecipes = new ArrayList<>();
    private final List<EntityConversionRecipe<?>> entityRecipes = new ArrayList<>();
    private static final Gson GSON = new GsonBuilder().create();

    public ConversionRecipeReloadListener() {
        super(GSON, "conversion_recipes");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> recipes, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        recipes.forEach((name, json) -> {
            try {
                parseRecipe(json);
            } catch (Exception e) {
                LOGGER.error("Invalid conversion recipe " + name, e);
            }
        });

        ConversionRecipeManager.INSTANCE.replaceAllRecipes(blockRecipes, entityRecipes);
        blockRecipes.clear();
        entityRecipes.clear();
    }

    private void parseRecipe(JsonElement json) throws JsonSyntaxException {
        if (!json.isJsonObject()) {
            throw new JsonSyntaxException("root must be a json object");
        }
        JsonObject root = json.getAsJsonObject();

        ResourceLocation type = JsonHelper.getJsonResourceLocation(root, "type");
        RecipeTool recipeTool = RecipeTool.fromJsonObject(JsonHelper.getJsonObject(root, "tool"));

        if (type.toString().equals("expandedstorage:block_conversion")) {
            parseBlockRecipe(root, recipeTool);
        } else if (type.toString().equals("expandedstorage:entity_conversion")) {
            parseEntityRecipe(root, recipeTool);
        } else {
            throw new JsonSyntaxException("type must be either: \"expandedstorage:block_conversion\" or \"expandedstorage:entity_conversion\"");
        }
    }

    private void parseBlockRecipe(JsonObject root, RecipeTool recipeTool) {
        PartialBlockState<?> output = PartialBlockState.readFromJson(JsonHelper.getJsonObject(root, "result"));
        if (output == null) {
            return;
        }
        JsonHelper.checkHasEntry(root, "inputs");
        blockRecipes.add(new BlockConversionRecipe<>(recipeTool, output, RecipeCondition.readBlockCondition(root.get("inputs"))));
    }

    private void parseEntityRecipe(JsonObject root, RecipeTool recipeTool) {
        ResourceLocation resultId = JsonHelper.getJsonResourceLocation(root, "result");
        if (resultId.toString().equals("minecraft:air")) {
            return;
        }
        EntityType<?> output = BuiltInRegistries.ENTITY_TYPE.getOptional(resultId).orElseThrow();
        JsonHelper.checkHasEntry(root, "inputs");
        entityRecipes.add(new EntityConversionRecipe<>(recipeTool, output, RecipeCondition.readEntityCondition(root.get("inputs"))));
    }
}
