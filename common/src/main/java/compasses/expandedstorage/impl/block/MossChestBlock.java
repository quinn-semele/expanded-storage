package compasses.expandedstorage.impl.block;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MossChestBlock extends ChestBlock implements BonemealableBlock {
    public MossChestBlock(Properties settings, ResourceLocation openingStat, int slotCount) {
        super(settings, openingStat, slotCount);
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter blockLevel, BlockPos pos, BlockState state, boolean isClient) {
        return blockLevel.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource source, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource source, BlockPos pos, BlockState state) {
        CaveFeatures.MOSS_PATCH_BONEMEAL.value().place(level, level.getChunkSource().getGenerator(), source, pos.above());
    }
}
