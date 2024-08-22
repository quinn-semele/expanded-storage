package dev.compasses.expandedstorage.datagen;

import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.ChestBlock;
import dev.compasses.expandedstorage.block.misc.DoubleBlockType;
import dev.compasses.expandedstorage.registration.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CommonBlockStateAndModelsProvider extends BlockStateProvider {
    public CommonBlockStateAndModelsProvider(PackOutput output, String modid, ExistingFileHelper fileHelper) {
        super(output, modid, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(ModBlocks.WOODEN_CHEST).forAllStates(state -> {
            String modelPath = "wooden_chest";

            DoubleBlockType chestType = state.getValue(ChestBlock.CHEST_TYPE);
            if (chestType != DoubleBlockType.SINGLE) {
                modelPath += "_" + chestType.getSerializedName();
            }

            if (state.getValue(BlockStateProperties.OPEN)) {
                modelPath += "_open";
            }

            int rotation = 90 * ((state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() + 2) % 4);

            return new ConfiguredModel[]{new ConfiguredModel(models().getExistingFile(Utils.id(modelPath)), 0, rotation, false)};
        });
    }
}
