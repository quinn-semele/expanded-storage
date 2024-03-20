package compasses.expandedstorage.impl.datagen.providers;

import com.google.gson.JsonElement;
import compasses.expandedstorage.impl.block.MiniStorageBlock;
import compasses.expandedstorage.impl.datagen.content.ModEntityTypes;
import compasses.expandedstorage.impl.datagen.content.ModTags;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.recipe.BlockConversionRecipe;
import compasses.expandedstorage.impl.recipe.EntityConversionRecipe;
import compasses.expandedstorage.impl.recipe.conditions.AndCondition;
import compasses.expandedstorage.impl.recipe.conditions.HasPropertyCondition;
import compasses.expandedstorage.impl.recipe.conditions.IsInTagCondition;
import compasses.expandedstorage.impl.recipe.conditions.IsRegistryObject;
import compasses.expandedstorage.impl.recipe.conditions.RecipeCondition;
import compasses.expandedstorage.impl.recipe.misc.PartialBlockState;
import compasses.expandedstorage.impl.recipe.misc.RecipeTool;
import compasses.expandedstorage.impl.registration.ModBlocks;
import compasses.expandedstorage.impl.registration.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public abstract class ConversionRecipeProvider implements DataProvider {
    protected static final RecipeTool UNNAMED_MUTATOR = new RecipeTool.MutatorTool(null);
    protected static final RecipeTool SPARROW_MUTATOR = new RecipeTool.MutatorTool("sparrow");

    protected static final RecipeTool WOOD_TO_COPPER_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.WOOD_TO_COPPER_CONVERSION_KIT);
    protected static final RecipeTool WOOD_TO_IRON_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.WOOD_TO_IRON_CONVERSION_KIT);
    protected static final RecipeTool WOOD_TO_GOLD_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.WOOD_TO_GOLD_CONVERSION_KIT);
    protected static final RecipeTool WOOD_TO_DIAMOND_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT);
    protected static final RecipeTool WOOD_TO_OBSIDIAN_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.WOOD_TO_OBSIDIAN_CONVERSION_KIT);
    protected static final RecipeTool WOOD_TO_NETHERITE_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.WOOD_TO_NETHERITE_CONVERSION_KIT);

    protected static final RecipeTool COPPER_TO_IRON_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.COPPER_TO_IRON_CONVERSION_KIT);
    protected static final RecipeTool COPPER_TO_GOLD_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.COPPER_TO_GOLD_CONVERSION_KIT);
    protected static final RecipeTool COPPER_TO_DIAMOND_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.COPPER_TO_DIAMOND_CONVERSION_KIT);
    protected static final RecipeTool COPPER_TO_OBSIDIAN_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.COPPER_TO_OBSIDIAN_CONVERSION_KIT);
    protected static final RecipeTool COPPER_TO_NETHERITE_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.COPPER_TO_NETHERITE_CONVERSION_KIT);

    protected static final RecipeTool IRON_TO_GOLD_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.IRON_TO_GOLD_CONVERSION_KIT);
    protected static final RecipeTool IRON_TO_DIAMOND_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.IRON_TO_DIAMOND_CONVERSION_KIT);
    protected static final RecipeTool IRON_TO_OBSIDIAN_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.IRON_TO_OBSIDIAN_CONVERSION_KIT);
    protected static final RecipeTool IRON_TO_NETHERITE_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.IRON_TO_NETHERITE_CONVERSION_KIT);

    protected static final RecipeTool GOLD_TO_DIAMOND_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT);
    protected static final RecipeTool GOLD_TO_OBSIDIAN_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT);
    protected static final RecipeTool GOLD_TO_NETHERITE_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT);

    protected static final RecipeTool DIAMOND_TO_OBSIDIAN_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT);
    protected static final RecipeTool DIAMOND_TO_NETHERITE_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.DIAMOND_TO_NETHERITE_CONVERSION_KIT);

    protected static final RecipeTool OBSIDIAN_TO_NETHERITE_CONVERSION_KIT = new RecipeTool.UpgradeTool(ModItems.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT);

    protected final PackOutput.PathProvider pathProvider;
    private final HashMap<ResourceLocation, BlockConversionRecipe<?>> blockRecipes = new HashMap<>();
    private final HashMap<ResourceLocation, EntityConversionRecipe<?>> entityRecipes = new HashMap<>();

    public ConversionRecipeProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "conversion_recipes");

        pathProvider.json(new ResourceLocation("expandedstorage", "block/wood_to_copper_chest"));
    }

    protected void registerBlockRecipe(ResourceLocation id, BlockConversionRecipe<?> recipe) {
        ResourceLocation realId = new ResourceLocation(id.getNamespace(), "block/" + id.getPath());
        if (blockRecipes.containsKey(realId)) {
            throw new IllegalStateException("Tried registering duplicate block recipe with id: " + realId);
        }
        blockRecipes.put(realId, recipe);
    }

    protected void registerEntityRecipe(ResourceLocation id, EntityConversionRecipe<?> recipe) {
        ResourceLocation realId = new ResourceLocation(id.getNamespace(), "entity/" + id.getPath());
        if (entityRecipes.containsKey(realId)) {
            throw new IllegalStateException("Tried registering duplicate entity recipe with id: " + realId);
        }
        entityRecipes.put(realId, recipe);
    }

    protected void simpleBlockThemeSwap(ResourceLocation id, Block from, Block to) {
        this.registerBlockRecipe(id,
                new BlockConversionRecipe<>(UNNAMED_MUTATOR, new PartialBlockState<>(to),
                        new IsRegistryObject(BuiltInRegistries.BLOCK, from.builtInRegistryHolder().key().location())
                )
        );
    }

    protected void sparrowBlockThemeSwap(ResourceLocation id, Block from, boolean fromSparrow, Block to, boolean toSparrow) {
        ResourceLocation fromId = from.builtInRegistryHolder().key().location();
        this.registerBlockRecipe(id,
                new BlockConversionRecipe<>(SPARROW_MUTATOR, new PartialBlockState<>(to, Map.of(MiniStorageBlock.SPARROW, toSparrow)),
                        new AndCondition(new IsRegistryObject(BuiltInRegistries.BLOCK, fromId),
                                new HasPropertyCondition(fromId, Map.of(MiniStorageBlock.SPARROW, fromSparrow), false))
                )
        );
    }

    protected void sparrowReversibleBlockThemeSwap(String blockName, Block block) {
        ResourceLocation blockId = block.builtInRegistryHolder().key().location();
        this.registerBlockRecipe(Utils.id("%s_to_with_sparrow".formatted(blockName)),
                new BlockConversionRecipe<>(SPARROW_MUTATOR, new PartialBlockState<>(block, Map.of(MiniStorageBlock.SPARROW, true)),
                        new AndCondition(new IsRegistryObject(BuiltInRegistries.BLOCK, blockId), new HasPropertyCondition(blockId, Map.of(MiniStorageBlock.SPARROW, false), false))
                )
        );
        this.registerBlockRecipe(Utils.id("%s_to_without_sparrow".formatted(blockName)),
                new BlockConversionRecipe<>(UNNAMED_MUTATOR, new PartialBlockState<>(block, Map.of(MiniStorageBlock.SPARROW, false)),
                        new AndCondition(new IsRegistryObject(BuiltInRegistries.BLOCK, blockId), new HasPropertyCondition(blockId, Map.of(MiniStorageBlock.SPARROW, true), false))
                )
        );
    }

    protected void simpleEntityThemeSwap(ResourceLocation id, EntityType<?> from, EntityType<?> to) {
        this.registerEntityRecipe(id,
                new EntityConversionRecipe<>(UNNAMED_MUTATOR, to,
                        new IsRegistryObject(BuiltInRegistries.ENTITY_TYPE, from.builtInRegistryHolder().key().location())
                )
        );
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        blockRecipes.clear();
        entityRecipes.clear();

        this.registerBlockRecipes();
        this.registerEntityRecipes();

        return CompletableFuture.allOf(Stream.concat(blockRecipes.entrySet().stream(), entityRecipes.entrySet().stream())
                                             .map(entry -> {
                                                 JsonElement json = entry.getValue().toJson();
                                                 Path path = pathProvider.json(entry.getKey());
                                                 return DataProvider.saveStable(cachedOutput, json, path);
                                             }).toArray(CompletableFuture[]::new));
    }

    protected abstract void registerBlockRecipes();

    protected void registerBlockRecipes(RecipeCondition isWoodBarrel, RecipeCondition isWoodChest) {
        // Chest upgrade recipes
        {
            var isWoodTier = new IsInTagCondition(ModTags.Blocks.ES_WOODEN_CHESTS);
//            var isCopperTier = List.of(new IsInTagCondition(ModTags.Blocks.COPPER_CHESTS));
            var isIronTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.IRON_CHEST.getBlockId());
            var isGoldTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.GOLD_CHEST.getBlockId());
            var isDiamondTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.DIAMOND_CHEST.getBlockId());
            var isObsidianTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.OBSIDIAN_CHEST.getBlockId());
            var ironChest = new PartialBlockState<>(ModBlocks.IRON_CHEST);
            var goldChest = new PartialBlockState<>(ModBlocks.GOLD_CHEST);
            var diamondChest = new PartialBlockState<>(ModBlocks.DIAMOND_CHEST);
            var obsidianChest = new PartialBlockState<>(ModBlocks.OBSIDIAN_CHEST);
            var netheriteChest = new PartialBlockState<>(ModBlocks.NETHERITE_CHEST);
//            registerBlockRecipe(Utils.id("wood_to_copper_chest"),
//                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_COPPER_CONVERSION_KIT, new PartialBlockState<>(ModBlocks.COPPER_CHEST), isWoodBarrel)
//            );
            registerBlockRecipe(Utils.id("wood_to_iron_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_IRON_CONVERSION_KIT, ironChest, isWoodTier)
            );
            registerBlockRecipe(Utils.id("wood_to_gold_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_GOLD_CONVERSION_KIT, goldChest, isWoodTier)
            );
            registerBlockRecipe(Utils.id("wood_to_diamond_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_DIAMOND_CONVERSION_KIT, diamondChest, isWoodTier)
            );
            registerBlockRecipe(Utils.id("wood_to_obsidian_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_OBSIDIAN_CONVERSION_KIT, obsidianChest, isWoodTier)
            );
            registerBlockRecipe(Utils.id("wood_to_netherite_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_NETHERITE_CONVERSION_KIT, netheriteChest, isWoodTier)
            );
//            registerBlockRecipe(Utils.id("copper_to_iron_chest"),
//                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_IRON_CONVERSION_KIT, ironChest, isCopperTier)
//            );
//            registerBlockRecipe(Utils.id("copper_to_gold_chest"),
//                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_GOLD_CONVERSION_KIT, goldChest, isCopperTier)
//            );
//            registerBlockRecipe(Utils.id("copper_to_diamond_chest"),
//                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_DIAMOND_CONVERSION_KIT, diamondChest, isCopperTier)
//            );
//            registerBlockRecipe(Utils.id("copper_to_obsidian_chest"),
//                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_OBSIDIAN_CONVERSION_KIT, obsidianChest, isCopperTier)
//            );
//            registerBlockRecipe(Utils.id("copper_to_netherite_chest"),
//                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_NETHERITE_CONVERSION_KIT, netheriteChest, isCopperTier)
//            );
            registerBlockRecipe(Utils.id("iron_to_gold_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.IRON_TO_GOLD_CONVERSION_KIT, goldChest, isIronTier)
            );
            registerBlockRecipe(Utils.id("iron_to_diamond_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.IRON_TO_DIAMOND_CONVERSION_KIT, diamondChest, isIronTier)
            );
            registerBlockRecipe(Utils.id("iron_to_obsidian_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.IRON_TO_OBSIDIAN_CONVERSION_KIT, obsidianChest, isIronTier)
            );
            registerBlockRecipe(Utils.id("iron_to_netherite_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.IRON_TO_NETHERITE_CONVERSION_KIT, netheriteChest, isIronTier)
            );
            registerBlockRecipe(Utils.id("gold_to_diamond_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.GOLD_TO_DIAMOND_CONVERSION_KIT, diamondChest, isGoldTier)
            );
            registerBlockRecipe(Utils.id("gold_to_obsidian_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.GOLD_TO_OBSIDIAN_CONVERSION_KIT, obsidianChest, isGoldTier)
            );
            registerBlockRecipe(Utils.id("gold_to_netherite_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.GOLD_TO_NETHERITE_CONVERSION_KIT, netheriteChest, isGoldTier)
            );
            registerBlockRecipe(Utils.id("diamond_to_obsidian_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT, obsidianChest, isDiamondTier)
            );
            registerBlockRecipe(Utils.id("diamond_to_netherite_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.DIAMOND_TO_NETHERITE_CONVERSION_KIT, netheriteChest, isDiamondTier)
            );
            registerBlockRecipe(Utils.id("obsidian_to_netherite_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT, netheriteChest, isObsidianTier)
            );
        }

        // Old chest upgrade recipes
        {
            var isWoodTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.OLD_WOOD_CHEST.getBlockId());
//            var isCopperTier = List.of(new IsInTagCondition(ModTags.Blocks.OLD_COPPER_CHESTS));
            var isIronTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.OLD_IRON_CHEST.getBlockId());
            var isGoldTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.OLD_GOLD_CHEST.getBlockId());
            var isDiamondTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.OLD_DIAMOND_CHEST.getBlockId());
            var isObsidianTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.OLD_OBSIDIAN_CHEST.getBlockId());
//            var copperChest = new PartialBlockState<>(ModBlocks.OLD_COPPER_CHEST);
            var ironChest = new PartialBlockState<>(ModBlocks.OLD_IRON_CHEST);
            var goldChest = new PartialBlockState<>(ModBlocks.OLD_GOLD_CHEST);
            var diamondChest = new PartialBlockState<>(ModBlocks.OLD_DIAMOND_CHEST);
            var obsidianChest = new PartialBlockState<>(ModBlocks.OLD_OBSIDIAN_CHEST);
            var netheriteChest = new PartialBlockState<>(ModBlocks.OLD_NETHERITE_CHEST);
//            registerBlockRecipe(Utils.id("wood_to_copper_old_chest"),
//                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_IRON_CONVERSION_KIT, copperChest, isWoodTier)
//            );
            registerBlockRecipe(Utils.id("wood_to_iron_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_IRON_CONVERSION_KIT, ironChest, isWoodTier)
            );
            registerBlockRecipe(Utils.id("wood_to_gold_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_GOLD_CONVERSION_KIT, goldChest, isWoodTier)
            );
            registerBlockRecipe(Utils.id("wood_to_diamond_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_DIAMOND_CONVERSION_KIT, diamondChest, isWoodTier)
            );
            registerBlockRecipe(Utils.id("wood_to_obsidian_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_OBSIDIAN_CONVERSION_KIT, obsidianChest, isWoodTier)
            );
            registerBlockRecipe(Utils.id("wood_to_netherite_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_NETHERITE_CONVERSION_KIT, netheriteChest, isWoodTier)
            );
//            registerBlockRecipe(Utils.id("copper_to_iron_old_chest"),
//                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_IRON_CONVERSION_KIT, ironChest, isCopperTier)
//            );
//            registerBlockRecipe(Utils.id("copper_to_gold_old_chest"),
//                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_GOLD_CONVERSION_KIT, goldChest, isCopperTier)
//            );
//            registerBlockRecipe(Utils.id("copper_to_diamond_old_chest"),
//                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_DIAMOND_CONVERSION_KIT, diamondChest, isCopperTier)
//            );
//            registerBlockRecipe(Utils.id("copper_to_obsidian_old_chest"),
//                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_OBSIDIAN_CONVERSION_KIT, obsidianChest, isCopperTier)
//            );
//            registerBlockRecipe(Utils.id("copper_to_netherite_old_chest"),
//                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_NETHERITE_CONVERSION_KIT, netheriteChest, isCopperTier)
//            );
            registerBlockRecipe(Utils.id("iron_to_gold_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.IRON_TO_GOLD_CONVERSION_KIT, goldChest, isIronTier)
            );
            registerBlockRecipe(Utils.id("iron_to_diamond_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.IRON_TO_DIAMOND_CONVERSION_KIT, diamondChest, isIronTier)
            );
            registerBlockRecipe(Utils.id("iron_to_obsidian_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.IRON_TO_OBSIDIAN_CONVERSION_KIT, obsidianChest, isIronTier)
            );
            registerBlockRecipe(Utils.id("iron_to_netherite_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.IRON_TO_NETHERITE_CONVERSION_KIT, netheriteChest, isIronTier)
            );
            registerBlockRecipe(Utils.id("gold_to_diamond_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.GOLD_TO_DIAMOND_CONVERSION_KIT, diamondChest, isGoldTier)
            );
            registerBlockRecipe(Utils.id("gold_to_obsidian_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.GOLD_TO_OBSIDIAN_CONVERSION_KIT, obsidianChest, isGoldTier)
            );
            registerBlockRecipe(Utils.id("gold_to_netherite_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.GOLD_TO_NETHERITE_CONVERSION_KIT, netheriteChest, isGoldTier)
            );
            registerBlockRecipe(Utils.id("diamond_to_obsidian_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT, obsidianChest, isDiamondTier)
            );
            registerBlockRecipe(Utils.id("diamond_to_netherite_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.DIAMOND_TO_NETHERITE_CONVERSION_KIT, netheriteChest, isDiamondTier)
            );
            registerBlockRecipe(Utils.id("obsidian_to_netherite_old_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT, netheriteChest, isObsidianTier)
            );
        }

        // Barrel upgrade recipes
        {
            var isCopperTier = new IsInTagCondition(ModTags.Blocks.COPPER_BARRELS);
            var isIronTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.IRON_BARREL.getBlockId());
            var isGoldTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.GOLD_BARREL.getBlockId());
            var isDiamondTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.DIAMOND_BARREL.getBlockId());
            var isObsidianTier = new IsRegistryObject(BuiltInRegistries.BLOCK, ModBlocks.OBSIDIAN_BARREL.getBlockId());
            var ironBarrel = new PartialBlockState<>(ModBlocks.IRON_BARREL);
            var goldBarrel = new PartialBlockState<>(ModBlocks.GOLD_BARREL);
            var diamondBarrel = new PartialBlockState<>(ModBlocks.DIAMOND_BARREL);
            var obsidianBarrel = new PartialBlockState<>(ModBlocks.OBSIDIAN_BARREL);
            var netheriteBarrel = new PartialBlockState<>(ModBlocks.NETHERITE_BARREL);
            registerBlockRecipe(Utils.id("wood_to_copper_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_COPPER_CONVERSION_KIT, new PartialBlockState<>(ModBlocks.COPPER_BARREL), isWoodBarrel)
            );
            registerBlockRecipe(Utils.id("wood_to_iron_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_IRON_CONVERSION_KIT, ironBarrel, isWoodBarrel)
            );
            registerBlockRecipe(Utils.id("wood_to_gold_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_GOLD_CONVERSION_KIT, goldBarrel, isWoodBarrel)
            );
            registerBlockRecipe(Utils.id("wood_to_diamond_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_DIAMOND_CONVERSION_KIT, diamondBarrel, isWoodBarrel)
            );
            registerBlockRecipe(Utils.id("wood_to_obsidian_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_OBSIDIAN_CONVERSION_KIT, obsidianBarrel, isWoodBarrel)
            );
            registerBlockRecipe(Utils.id("wood_to_netherite_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_NETHERITE_CONVERSION_KIT, netheriteBarrel, isWoodBarrel)
            );
            registerBlockRecipe(Utils.id("copper_to_iron_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_IRON_CONVERSION_KIT, ironBarrel, isCopperTier)
            );
            registerBlockRecipe(Utils.id("copper_to_gold_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_GOLD_CONVERSION_KIT, goldBarrel, isCopperTier)
            );
            registerBlockRecipe(Utils.id("copper_to_diamond_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_DIAMOND_CONVERSION_KIT, diamondBarrel, isCopperTier)
            );
            registerBlockRecipe(Utils.id("copper_to_obsidian_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_OBSIDIAN_CONVERSION_KIT, obsidianBarrel, isCopperTier)
            );
            registerBlockRecipe(Utils.id("copper_to_netherite_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.COPPER_TO_NETHERITE_CONVERSION_KIT, netheriteBarrel, isCopperTier)
            );
            registerBlockRecipe(Utils.id("iron_to_gold_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.IRON_TO_GOLD_CONVERSION_KIT, goldBarrel, isIronTier)
            );
            registerBlockRecipe(Utils.id("iron_to_diamond_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.IRON_TO_DIAMOND_CONVERSION_KIT, diamondBarrel, isIronTier)
            );
            registerBlockRecipe(Utils.id("iron_to_obsidian_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.IRON_TO_OBSIDIAN_CONVERSION_KIT, obsidianBarrel, isIronTier)
            );
            registerBlockRecipe(Utils.id("iron_to_netherite_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.IRON_TO_NETHERITE_CONVERSION_KIT, netheriteBarrel, isIronTier)
            );
            registerBlockRecipe(Utils.id("gold_to_diamond_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.GOLD_TO_DIAMOND_CONVERSION_KIT, diamondBarrel, isGoldTier)
            );
            registerBlockRecipe(Utils.id("gold_to_obsidian_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.GOLD_TO_OBSIDIAN_CONVERSION_KIT, obsidianBarrel, isGoldTier)
            );
            registerBlockRecipe(Utils.id("gold_to_netherite_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.GOLD_TO_NETHERITE_CONVERSION_KIT, netheriteBarrel, isGoldTier)
            );
            registerBlockRecipe(Utils.id("diamond_to_obsidian_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT, obsidianBarrel, isDiamondTier)
            );
            registerBlockRecipe(Utils.id("diamond_to_netherite_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.DIAMOND_TO_NETHERITE_CONVERSION_KIT, netheriteBarrel, isDiamondTier)
            );
            registerBlockRecipe(Utils.id("obsidian_to_netherite_barrel"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT, netheriteBarrel, isObsidianTier)
            );
        }

        // Chest theme swap recipes
        {
            registerBlockRecipe(Utils.id("vanilla_to_wood_chest"),
                    new BlockConversionRecipe<>(ConversionRecipeProvider.UNNAMED_MUTATOR, new PartialBlockState<>(ModBlocks.WOOD_CHEST), isWoodChest)
            );

            simpleBlockThemeSwap(Utils.id("wood_to_pumpkin_chest"), ModBlocks.WOOD_CHEST, ModBlocks.PUMPKIN_CHEST);
            simpleBlockThemeSwap(Utils.id("pumpkin_to_present_chest"), ModBlocks.PUMPKIN_CHEST, ModBlocks.PRESENT);
            simpleBlockThemeSwap(Utils.id("present_to_bamboo_chest"), ModBlocks.PRESENT, ModBlocks.BAMBOO_CHEST);
            simpleBlockThemeSwap(Utils.id("bamboo_to_moss_chest"), ModBlocks.BAMBOO_CHEST, ModBlocks.MOSS_CHEST);
            simpleBlockThemeSwap(Utils.id("moss_to_old_wood_chest"), ModBlocks.MOSS_CHEST, ModBlocks.OLD_WOOD_CHEST);
            simpleBlockThemeSwap(Utils.id("old_wood_chest_to_wood_chest"), ModBlocks.OLD_WOOD_CHEST, ModBlocks.WOOD_CHEST);

            simpleBlockThemeSwap(Utils.id("iron_to_old_iron_chest"), ModBlocks.IRON_CHEST, ModBlocks.OLD_IRON_CHEST);
            simpleBlockThemeSwap(Utils.id("old_iron_to_iron_chest"), ModBlocks.OLD_IRON_CHEST, ModBlocks.IRON_CHEST);

            simpleBlockThemeSwap(Utils.id("gold_to_old_gold_chest"), ModBlocks.GOLD_CHEST, ModBlocks.OLD_GOLD_CHEST);
            simpleBlockThemeSwap(Utils.id("old_gold_to_gold_chest"), ModBlocks.OLD_GOLD_CHEST, ModBlocks.GOLD_CHEST);

            simpleBlockThemeSwap(Utils.id("diamond_to_old_diamond_chest"), ModBlocks.DIAMOND_CHEST, ModBlocks.OLD_DIAMOND_CHEST);
            simpleBlockThemeSwap(Utils.id("old_diamond_to_diamond_chest"), ModBlocks.OLD_DIAMOND_CHEST, ModBlocks.DIAMOND_CHEST);

            simpleBlockThemeSwap(Utils.id("obsidian_to_old_obsidian_chest"), ModBlocks.OBSIDIAN_CHEST, ModBlocks.OLD_OBSIDIAN_CHEST);
            simpleBlockThemeSwap(Utils.id("old_obsidian_to_obsidian_chest"), ModBlocks.OLD_OBSIDIAN_CHEST, ModBlocks.OBSIDIAN_CHEST);

            simpleBlockThemeSwap(Utils.id("netherite_to_old_netherite_chest"), ModBlocks.NETHERITE_CHEST, ModBlocks.OLD_NETHERITE_CHEST);
            simpleBlockThemeSwap(Utils.id("old_netherite_to_netherite_chest"), ModBlocks.OLD_NETHERITE_CHEST, ModBlocks.NETHERITE_CHEST);
        }

        // Mini storage theme swap recipes
        {
            simpleBlockThemeSwap(Utils.id("vanilla_to_wood_mini_chest"), ModBlocks.VANILLA_WOOD_MINI_CHEST, ModBlocks.WOOD_MINI_CHEST);
            simpleBlockThemeSwap(Utils.id("wood_to_pumpkin_mini_chest"), ModBlocks.WOOD_MINI_CHEST, ModBlocks.PUMPKIN_MINI_CHEST);
            simpleBlockThemeSwap(Utils.id("pumpkin_to_red_mini_present"), ModBlocks.PUMPKIN_MINI_CHEST, ModBlocks.RED_MINI_PRESENT);
            simpleBlockThemeSwap(Utils.id("red_to_white_mini_present"), ModBlocks.RED_MINI_PRESENT, ModBlocks.WHITE_MINI_PRESENT);
            simpleBlockThemeSwap(Utils.id("white_to_candy_cane_mini_present"), ModBlocks.WHITE_MINI_PRESENT, ModBlocks.CANDY_CANE_MINI_PRESENT);
            simpleBlockThemeSwap(Utils.id("candy_cane_to_green_mini_present"), ModBlocks.CANDY_CANE_MINI_PRESENT, ModBlocks.GREEN_MINI_PRESENT);
            simpleBlockThemeSwap(Utils.id("green_to_lavender_present"), ModBlocks.GREEN_MINI_PRESENT, ModBlocks.LAVENDER_MINI_PRESENT);
            simpleBlockThemeSwap(Utils.id("lavender_to_pink_amethyst_mini_present"), ModBlocks.LAVENDER_MINI_PRESENT, ModBlocks.PINK_AMETHYST_MINI_PRESENT);
            simpleBlockThemeSwap(Utils.id("pink_amethyst_to_vanilla_mini_chest"), ModBlocks.PINK_AMETHYST_MINI_PRESENT, ModBlocks.VANILLA_WOOD_MINI_CHEST);

            sparrowBlockThemeSwap(Utils.id("pink_amethyst_to_vanilla_mini_chest_with_sparrow"), ModBlocks.PINK_AMETHYST_MINI_PRESENT, false, ModBlocks.VANILLA_WOOD_MINI_CHEST, true);
            sparrowBlockThemeSwap(Utils.id("vanilla_to_wood_mini_chest_with_sparrow"), ModBlocks.VANILLA_WOOD_MINI_CHEST, true, ModBlocks.WOOD_MINI_CHEST, true);
            sparrowBlockThemeSwap(Utils.id("wood_to_pumpkin_mini_chest_with_sparrow"), ModBlocks.WOOD_MINI_CHEST, true, ModBlocks.PUMPKIN_MINI_CHEST, true);
            sparrowBlockThemeSwap(Utils.id("pumpkin_to_red_mini_present_with_sparrow"), ModBlocks.PUMPKIN_MINI_CHEST, true, ModBlocks.RED_MINI_PRESENT, true);
            sparrowBlockThemeSwap(Utils.id("red_to_white_mini_present_with_sparrow"), ModBlocks.RED_MINI_PRESENT, true, ModBlocks.WHITE_MINI_PRESENT, true);
            sparrowBlockThemeSwap(Utils.id("white_to_candy_cane_mini_present_with_sparrow"), ModBlocks.WHITE_MINI_PRESENT, true, ModBlocks.CANDY_CANE_MINI_PRESENT, true);
            sparrowBlockThemeSwap(Utils.id("candy_cane_to_green_mini_present_with_sparrow"), ModBlocks.CANDY_CANE_MINI_PRESENT, true, ModBlocks.GREEN_MINI_PRESENT, true);
            sparrowBlockThemeSwap(Utils.id("green_to_lavender_present_with_sparrow"), ModBlocks.GREEN_MINI_PRESENT, true, ModBlocks.LAVENDER_MINI_PRESENT, true);
            sparrowBlockThemeSwap(Utils.id("lavender_to_pink_amethyst_mini_present_with_sparrow"), ModBlocks.LAVENDER_MINI_PRESENT, true, ModBlocks.PINK_AMETHYST_MINI_PRESENT, true);
            sparrowBlockThemeSwap(Utils.id("pink_amethyst_with_sparrow_to_vanilla_mini_chest"), ModBlocks.PINK_AMETHYST_MINI_PRESENT, true, ModBlocks.VANILLA_WOOD_MINI_CHEST, false);

            sparrowReversibleBlockThemeSwap("iron_mini_chest", ModBlocks.IRON_MINI_CHEST);
            sparrowReversibleBlockThemeSwap("gold_mini_chest", ModBlocks.GOLD_MINI_CHEST);
            sparrowReversibleBlockThemeSwap("diamond_mini_chest", ModBlocks.DIAMOND_MINI_CHEST);
            sparrowReversibleBlockThemeSwap("obsidian_mini_chest", ModBlocks.OBSIDIAN_MINI_CHEST);
            sparrowReversibleBlockThemeSwap("netherite_mini_chest", ModBlocks.NETHERITE_MINI_CHEST);

            sparrowReversibleBlockThemeSwap("copper_mini_barrel", ModBlocks.COPPER_MINI_BARREL);
            sparrowReversibleBlockThemeSwap("exposed_copper_mini_barrel", ModBlocks.EXPOSED_COPPER_MINI_BARREL);
            sparrowReversibleBlockThemeSwap("weathered_copper_mini_barrel", ModBlocks.WEATHERED_COPPER_MINI_BARREL);
            sparrowReversibleBlockThemeSwap("oxidized_copper_mini_barrel", ModBlocks.OXIDIZED_COPPER_MINI_BARREL);
            sparrowReversibleBlockThemeSwap("waxed_copper_mini_barrel", ModBlocks.WAXED_COPPER_MINI_BARREL);
            sparrowReversibleBlockThemeSwap("waxed_exposed_copper_mini_barrel", ModBlocks.WAXED_EXPOSED_COPPER_MINI_BARREL);
            sparrowReversibleBlockThemeSwap("waxed_weathered_copper_mini_barrel", ModBlocks.WAXED_WEATHERED_COPPER_MINI_BARREL);
            sparrowReversibleBlockThemeSwap("waxed_oxidized_copper_mini_barrel", ModBlocks.WAXED_OXIDIZED_COPPER_MINI_BARREL);
            sparrowReversibleBlockThemeSwap("iron_mini_barrel", ModBlocks.IRON_MINI_BARREL);
            sparrowReversibleBlockThemeSwap("gold_mini_barrel", ModBlocks.GOLD_MINI_BARREL);
            sparrowReversibleBlockThemeSwap("diamond_mini_barrel", ModBlocks.DIAMOND_MINI_BARREL);
            sparrowReversibleBlockThemeSwap("obsidian_mini_barrel", ModBlocks.OBSIDIAN_MINI_BARREL);
            sparrowReversibleBlockThemeSwap("netherite_mini_barrel", ModBlocks.NETHERITE_MINI_BARREL);
        }
    }

    protected abstract void registerEntityRecipes();

    protected void registerEntityRecipes(RecipeCondition isWoodenMinecart) {
        {
            var isWoodTier = new IsInTagCondition(ModTags.Entities.ES_WOODEN_CHEST_MINECARTS);
            var isIronTier = new IsRegistryObject(BuiltInRegistries.ENTITY_TYPE, ModEntityTypes.IRON_CHEST_MINECART.builtInRegistryHolder().key().location());
            var isGoldTier = new IsRegistryObject(BuiltInRegistries.ENTITY_TYPE, ModEntityTypes.GOLD_CHEST_MINECART.builtInRegistryHolder().key().location());
            var isDiamondTier = new IsRegistryObject(BuiltInRegistries.ENTITY_TYPE, ModEntityTypes.DIAMOND_CHEST_MINECART.builtInRegistryHolder().key().location());
            var isObsidianTier = new IsRegistryObject(BuiltInRegistries.ENTITY_TYPE, ModEntityTypes.OBSIDIAN_CHEST_MINECART.builtInRegistryHolder().key().location());

            registerEntityRecipe(Utils.id("wood_to_iron_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_IRON_CONVERSION_KIT, ModEntityTypes.IRON_CHEST_MINECART, isWoodTier)
            );

            registerEntityRecipe(Utils.id("wood_to_gold_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_GOLD_CONVERSION_KIT, ModEntityTypes.GOLD_CHEST_MINECART, isWoodTier)
            );

            registerEntityRecipe(Utils.id("wood_to_diamond_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_DIAMOND_CONVERSION_KIT, ModEntityTypes.DIAMOND_CHEST_MINECART, isWoodTier)
            );

            registerEntityRecipe(Utils.id("wood_to_obsidian_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_OBSIDIAN_CONVERSION_KIT, ModEntityTypes.OBSIDIAN_CHEST_MINECART, isWoodTier)
            );

            registerEntityRecipe(Utils.id("wood_to_netherite_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.WOOD_TO_NETHERITE_CONVERSION_KIT, ModEntityTypes.NETHERITE_CHEST_MINECART, isWoodTier)
            );

            registerEntityRecipe(Utils.id("iron_to_gold_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.IRON_TO_GOLD_CONVERSION_KIT, ModEntityTypes.GOLD_CHEST_MINECART, isIronTier)
            );

            registerEntityRecipe(Utils.id("iron_to_diamond_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.IRON_TO_DIAMOND_CONVERSION_KIT, ModEntityTypes.DIAMOND_CHEST_MINECART, isIronTier)
            );

            registerEntityRecipe(Utils.id("iron_to_obsidian_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.IRON_TO_OBSIDIAN_CONVERSION_KIT, ModEntityTypes.OBSIDIAN_CHEST_MINECART, isIronTier)
            );

            registerEntityRecipe(Utils.id("iron_to_netherite_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.IRON_TO_NETHERITE_CONVERSION_KIT, ModEntityTypes.NETHERITE_CHEST_MINECART, isIronTier)
            );

            registerEntityRecipe(Utils.id("gold_to_diamond_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.GOLD_TO_DIAMOND_CONVERSION_KIT, ModEntityTypes.DIAMOND_CHEST_MINECART, isGoldTier)
            );

            registerEntityRecipe(Utils.id("gold_to_obsidian_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.GOLD_TO_OBSIDIAN_CONVERSION_KIT, ModEntityTypes.OBSIDIAN_CHEST_MINECART, isGoldTier)
            );

            registerEntityRecipe(Utils.id("gold_to_netherite_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.GOLD_TO_NETHERITE_CONVERSION_KIT, ModEntityTypes.NETHERITE_CHEST_MINECART, isGoldTier)
            );

            registerEntityRecipe(Utils.id("diamond_to_obsidian_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT, ModEntityTypes.OBSIDIAN_CHEST_MINECART, isDiamondTier)
            );

            registerEntityRecipe(Utils.id("diamond_to_netherite_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.DIAMOND_TO_NETHERITE_CONVERSION_KIT, ModEntityTypes.NETHERITE_CHEST_MINECART, isDiamondTier)
            );

            registerEntityRecipe(Utils.id("obsidian_to_netherite_chest_minecart"),
                    new EntityConversionRecipe<>(ConversionRecipeProvider.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT, ModEntityTypes.NETHERITE_CHEST_MINECART, isObsidianTier)
            );
        }

        registerEntityRecipe(Utils.id("vanilla_to_wood_chest_minecart"),
                new EntityConversionRecipe<>(UNNAMED_MUTATOR, ModEntityTypes.WOOD_CHEST_MINECART, isWoodenMinecart)
        );

        simpleEntityThemeSwap(Utils.id("wood_to_pumpkin_chest_minecart"), ModEntityTypes.WOOD_CHEST_MINECART, ModEntityTypes.PUMPKIN_CHEST_MINECART);
        simpleEntityThemeSwap(Utils.id("pumpkin_to_present_chest_minecart"), ModEntityTypes.PUMPKIN_CHEST_MINECART, ModEntityTypes.PRESENT_MINECART);
        simpleEntityThemeSwap(Utils.id("present_to_bamboo_chest_minecart"), ModEntityTypes.PRESENT_MINECART, ModEntityTypes.BAMBOO_CHEST_MINECART);
        simpleEntityThemeSwap(Utils.id("bamboo_to_moss_chest_minecart"), ModEntityTypes.BAMBOO_CHEST_MINECART, ModEntityTypes.MOSS_CHEST_MINECART);
        simpleEntityThemeSwap(Utils.id("moss_to_wood_chest_minecart"), ModEntityTypes.MOSS_CHEST_MINECART, ModEntityTypes.WOOD_CHEST_MINECART);
    }

    @NotNull
    @Override
    public String getName() {
        return "Expanded Storage - Conversion Recipes";
    }
}
