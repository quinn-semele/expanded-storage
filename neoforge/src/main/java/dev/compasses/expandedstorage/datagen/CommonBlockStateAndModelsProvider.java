package dev.compasses.expandedstorage.datagen;

import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.BarrelBlock;
import dev.compasses.expandedstorage.block.ChestBlock;
import dev.compasses.expandedstorage.block.ShulkerBoxBlock;
import dev.compasses.expandedstorage.block.misc.DoubleBlockType;
import dev.compasses.expandedstorage.registration.ModBlocks;
import dev.compasses.expandedstorage.registration.ModItems;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Arrays;

public class CommonBlockStateAndModelsProvider extends BlockStateProvider {
    public CommonBlockStateAndModelsProvider(PackOutput output, String modid, ExistingFileHelper fileHelper) {
        super(output, modid, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        Arrays.stream(ModItems.UPGRADES).forEach(itemModels()::basicItem);

        generateChestAssets(ModBlocks.COMMON_CHEST, blockTexture(Blocks.OAK_PLANKS));
        generateChestAssets(ModBlocks.COPPER_CHEST, blockTexture(Blocks.COPPER_BLOCK));
        generateChestAssets(ModBlocks.EXPOSED_COPPER_CHEST, blockTexture(Blocks.EXPOSED_COPPER));
        generateChestAssets(ModBlocks.WEATHERED_COPPER_CHEST, blockTexture(Blocks.WEATHERED_COPPER));
        generateChestAssets(ModBlocks.OXIDIZED_COPPER_CHEST, blockTexture(Blocks.OXIDIZED_COPPER));
        generateChestAssets(ModBlocks.IRON_CHEST, blockTexture(Blocks.IRON_BLOCK));
        generateChestAssets(ModBlocks.GOLDEN_CHEST, blockTexture(Blocks.GOLD_BLOCK));
        generateChestAssets(ModBlocks.DIAMOND_CHEST, blockTexture(Blocks.DIAMOND_BLOCK));
        generateChestAssets(ModBlocks.OBSIDIAN_CHEST, blockTexture(Blocks.OBSIDIAN));
        generateChestAssets(ModBlocks.NETHERITE_CHEST, blockTexture(Blocks.NETHERITE_BLOCK));

        Arrays.stream(ModBlocks.BARRELS).forEach(this::generateBarrelAssets);

        Arrays.stream(ModBlocks.SHULKER_BOXES).forEach(this::generateShulkerBoxAssets);
    }

    private BlockModelBuilder chestModel(String prefix, ResourceLocation particle, boolean open, DoubleBlockType blockType) {
        String openSuffix = open ? "_open" : "";
        String parentSuffix = (blockType == DoubleBlockType.TOP ? DoubleBlockType.SINGLE : blockType).suffix();

        return models().withExistingParent(prefix + "chest" + blockType.suffix() + openSuffix, Utils.id("block/base/chest" + parentSuffix + openSuffix))
                .texture("atlas", Utils.id("block/" + prefix + "chest" + blockType.suffix()))
                .texture("particle", particle);
    }

    private void generateChestAssets(ChestBlock block, ResourceLocation particle) {
        String prefix = block.builtInRegistryHolder().key().location().getPath().replace("chest", "");

        var closedChest = chestModel(prefix, particle, false, DoubleBlockType.SINGLE);
        var openChest = chestModel(prefix, particle, true, DoubleBlockType.SINGLE);
        var closedLeftChest = chestModel(prefix, particle, false, DoubleBlockType.LEFT);
        var openLeftChest = chestModel(prefix, particle, true, DoubleBlockType.LEFT);
        var closedRightChest = chestModel(prefix, particle, false, DoubleBlockType.RIGHT);
        var openRightChest = chestModel(prefix, particle, true, DoubleBlockType.RIGHT);
        var closedFrontChest = chestModel(prefix, particle, false, DoubleBlockType.FRONT);
        var openFrontChest = chestModel(prefix, particle, true, DoubleBlockType.FRONT);
        var closedBackChest = chestModel(prefix, particle, false, DoubleBlockType.BACK);
        var openBackChest = chestModel(prefix, particle, true, DoubleBlockType.BACK);
        var closedTopChest = chestModel(prefix, particle, false, DoubleBlockType.TOP);
        var openTopChest = chestModel(prefix, particle, true, DoubleBlockType.TOP);
        var bottomChest = chestModel(prefix, particle, false, DoubleBlockType.BOTTOM);

        getVariantBuilder(block).forAllStatesExcept(state -> {
            DoubleBlockType chestType = state.getValue(ChestBlock.CHEST_TYPE);

            var model = switch (chestType) {
                case SINGLE -> closedChest;
                case LEFT -> closedLeftChest;
                case RIGHT -> closedRightChest;
                case FRONT -> closedFrontChest;
                case BACK -> closedBackChest;
                case TOP -> closedTopChest;
                case BOTTOM -> bottomChest;
            };

            if (state.getValue(BlockStateProperties.OPEN)) {
                model = switch (chestType) {
                    case SINGLE -> openChest;
                    case LEFT -> openLeftChest;
                    case RIGHT -> openRightChest;
                    case FRONT -> openFrontChest;
                    case BACK -> openBackChest;
                    case TOP -> openTopChest;
                    case BOTTOM -> model;
                };
            }

            int yRotation = 90 * ((state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() + 2) % 4);

            return new ConfiguredModel[]{new ConfiguredModel(models().getExistingFile(model.getLocation()), 0, yRotation, false)};
        }, BlockStateProperties.WATERLOGGED);

        simpleBlockItem(block, models().getExistingFile(closedChest.getLocation()));
    }

    private void generateBarrelAssets(BarrelBlock block) {
        String prefix = block.builtInRegistryHolder().key().location().getPath().replace("barrel", "");

        var closedModel = models().withExistingParent(prefix + "barrel", "block/barrel")
                        .texture("side", Utils.id("block/" + prefix + "barrel_side"));

        var openModel = models().withExistingParent(prefix + "barrel_open", "block/barrel_open")
                .texture("side", Utils.id("block/" + prefix + "barrel_side"));

        var closedTopModel = models().withExistingParent(prefix + "barrel_top", Utils.id("block/base/barrel_top"))
                .texture("side", Utils.id("block/" + prefix + "barrel_side_top"));

        var openTopModel = models().withExistingParent(prefix + "barrel_top_open", Utils.id("block/base/barrel_top_open"))
                .texture("side", Utils.id("block/" + prefix + "barrel_side_top"));

        var bottomModel = models().withExistingParent(prefix + "barrel_bottom", Utils.id("block/base/barrel_bottom"))
                .texture("side", Utils.id("block/" + prefix + "barrel_side_bottom"));

        getVariantBuilder(block).forAllStates(state -> {
            var model = closedModel;

            DoubleBlockType barrelType = state.getValue(BarrelBlock.BARREL_TYPE);
            if (barrelType == DoubleBlockType.TOP) {
                model = closedTopModel;
            } else if (barrelType == DoubleBlockType.BOTTOM) {
                model = bottomModel;
            }

            if (state.getValue(BlockStateProperties.OPEN)) {
                if (model == closedModel) {
                    model = openModel;
                } else if (model == closedTopModel) {
                    model = openTopModel;
                }
            }

            Direction facing = state.getValue(BlockStateProperties.FACING);

            int xRotation = switch(facing) {
                case UP -> 0;
                case DOWN -> 180;
                default -> 90;
            };

            int yRotation = switch (facing) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };

            return new ConfiguredModel[]{new ConfiguredModel(models().getExistingFile(model.getLocation()), xRotation, yRotation, false)};
        });

        simpleBlockItem(block, models().getExistingFile(closedModel.getLocation()));
    }

    private void generateShulkerBoxAssets(ShulkerBoxBlock block) {
        String colorPrefix = block.color().prefix();

        var closedModel = models().withExistingParent(colorPrefix + "shulker_box", Utils.id("block/base/shulker_box"))
                .texture("atlas", "expandedstorage:block/" + colorPrefix + "shulker_box")
                .texture("particle", "expandedstorage:block/" + colorPrefix + "shulker_box_particle");

        var openModel = models().getBuilder(colorPrefix + "shulker_box_open")
                .texture("particle", "expandedstorage:block/" + colorPrefix + "shulker_box_particle");

        getVariantBuilder(block).forAllStates(state -> {
            var model = closedModel;

            if (state.getValue(BlockStateProperties.OPEN)) {
                model = openModel;
            }

            Direction facing = state.getValue(BlockStateProperties.FACING);

            int xRotation = switch(facing) {
                case UP -> 0;
                case DOWN -> 180;
                default -> 90;
            };

            int yRotation = switch (facing) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };

            return new ConfiguredModel[]{new ConfiguredModel(models().getExistingFile(model.getLocation()), xRotation, yRotation, false)};
        });

        simpleBlockItem(block, models().getExistingFile(closedModel.getLocation()));
    }
}
