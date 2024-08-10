package compasses.expandedstorage.impl.compat.carrier;

import compasses.expandedstorage.impl.block.MiniStorageBlock;
import me.steven.carrier.api.CarriablePlacementContext;
import me.steven.carrier.api.CarryingData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

final class CarriableMiniBlock extends CarriableOpenableBlock {
    public CarriableMiniBlock(ResourceLocation id, Block parent) {
        super(id, parent);
    }

    @Override
    protected BlockState getPlacementState(Level level, BlockPos pos, CarriablePlacementContext context, CarryingData data, Player player) {
        return super.getPlacementState(level, pos, context, data, player)
                    .setValue(MiniStorageBlock.SPARROW, data.getBlockState().getValue(MiniStorageBlock.SPARROW));
    }
}
