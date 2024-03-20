package compasses.expandedstorage.impl.block;

import compasses.expandedstorage.impl.block.misc.CopperBlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class CopperBarrelBlock extends BarrelBlock implements WeatheringCopper {
    private final WeatherState weatherState;

    public CopperBarrelBlock(Properties settings, ResourceLocation openingStat, int slotCount, WeatheringCopper.WeatherState weatherState) {
        super(settings, openingStat, slotCount);
        this.weatherState = weatherState;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return CopperBlockHelper.getNextOxidisedState(state).isPresent();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.onRandomTick(state, level, pos, random);
    }

    @NotNull
    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return CopperBlockHelper.getNextOxidisedState(state);
    }

    @NotNull
    @Override
    public WeatherState getAge() {
        return weatherState;
    }
}
