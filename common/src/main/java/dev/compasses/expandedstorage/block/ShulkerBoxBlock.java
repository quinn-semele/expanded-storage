package dev.compasses.expandedstorage.block;

import dev.compasses.expandedstorage.block.entity.ShulkerBoxBlockEntity;
import dev.compasses.expandedstorage.block.misc.BlockColor;
import dev.compasses.expandedstorage.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShulkerBoxBlock extends InventoryBlock {
    private final BlockColor color;

    public ShulkerBoxBlock(Properties properties, BlockColor color) {
        super(properties);
        this.color = color;

        this.registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.FACING, Direction.UP)
                .setValue(BlockStateProperties.OPEN, false)
        );
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ShulkerBoxBlockEntity entity) {
            entity.toggleOpen(player);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public BlockColor color() {
        return color;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
        builder.add(BlockStateProperties.OPEN);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createEntityTicker(type, ModBlockEntities.SHULKER_BOX, ShulkerBoxBlockEntity::progressLidAnimation);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return ModBlockEntities.SHULKER_BOX;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }
}
