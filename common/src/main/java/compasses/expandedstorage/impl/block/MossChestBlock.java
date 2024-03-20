package compasses.expandedstorage.impl.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MossChestBlock extends ChestBlock implements BonemealableBlock {
    public MossChestBlock(Properties settings, ResourceLocation openingStat, int slotCount) {
        super(settings, openingStat, slotCount);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return level.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource source, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource source, BlockPos pos, BlockState state) {
        level.registryAccess()
             .registry(Registries.CONFIGURED_FEATURE)
             .flatMap(registry -> registry.getHolder(CaveFeatures.MOSS_PATCH_BONEMEAL))
             .ifPresent(feature -> feature.value().place(level, level.getChunkSource().getGenerator(), source, pos.above()));
    }
}
