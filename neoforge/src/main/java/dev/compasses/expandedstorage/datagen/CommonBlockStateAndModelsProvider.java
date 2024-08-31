package dev.compasses.expandedstorage.datagen;

import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.BarrelBlock;
import dev.compasses.expandedstorage.block.ChestBlock;
import dev.compasses.expandedstorage.block.ShulkerBoxBlock;
import dev.compasses.expandedstorage.block.misc.DoubleBlockType;
import dev.compasses.expandedstorage.registration.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CommonBlockStateAndModelsProvider extends BlockStateProvider {
    public CommonBlockStateAndModelsProvider(PackOutput output, String modid, ExistingFileHelper fileHelper) {
        super(output, modid, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(ModBlocks.WOODEN_CHEST).forAllStatesExcept(state -> {
            String modelPath = "wooden_chest";

            DoubleBlockType chestType = state.getValue(ChestBlock.CHEST_TYPE);
            if (chestType != DoubleBlockType.SINGLE) {
                modelPath += "_" + chestType.getSerializedName();
            }

            if (state.getValue(BlockStateProperties.OPEN) && chestType != DoubleBlockType.BOTTOM) {
                modelPath += "_open";
            }

            int yRotation = 90 * ((state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() + 2) % 4);

            return new ConfiguredModel[]{new ConfiguredModel(models().getExistingFile(Utils.id(modelPath)), 0, yRotation, false)};
        }, BlockStateProperties.WATERLOGGED);

        simpleBlockItem(ModBlocks.WOODEN_CHEST, models().getExistingFile(Utils.id("wooden_chest")));

        getVariantBuilder(ModBlocks.WOODEN_BARREL).forAllStates(state -> {
            String modelPath = "wooden_barrel";

            DoubleBlockType barrelType = state.getValue(BarrelBlock.BARREL_TYPE);
            if (barrelType != DoubleBlockType.SINGLE) {
                modelPath += "_" + barrelType.getSerializedName();
            }

            if (state.getValue(BlockStateProperties.OPEN) && barrelType != DoubleBlockType.BOTTOM) {
                modelPath += "_open";
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

            return new ConfiguredModel[]{new ConfiguredModel(models().getExistingFile(Utils.id(modelPath)), xRotation, yRotation, false)};
        });

        simpleBlockItem(ModBlocks.WOODEN_BARREL, models().getExistingFile(Utils.id("wooden_barrel")));

        for (ShulkerBoxBlock shulkerBox : ModBlocks.SHULKER_BOXES) {
            String colorPrefix = shulkerBox.color().prefix();

            BlockModelBuilder closedModel = models().withExistingParent(colorPrefix + "shulker_box", Utils.id("block/base/shulker_box"))
                    .texture("atlas", "expandedstorage:block/" + colorPrefix + "shulker_box")
                    .texture("particle", "expandedstorage:block/" + colorPrefix + "shulker_box_particle");

            BlockModelBuilder openModel = models().getBuilder(colorPrefix + "shulker_box_open")
                    .texture("particle", "expandedstorage:block/" + colorPrefix + "shulker_box_particle");

            getVariantBuilder(shulkerBox).forAllStates(state -> {
                BlockModelBuilder model = closedModel;

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

            simpleBlockItem(shulkerBox, models().getExistingFile(closedModel.getLocation()));
        }


    }
}
