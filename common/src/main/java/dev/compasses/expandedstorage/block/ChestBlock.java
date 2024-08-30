package dev.compasses.expandedstorage.block;

import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.entity.ChestBlockEntity;
import dev.compasses.expandedstorage.block.misc.DoubleBlockType;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class ChestBlock extends InventoryBlock {
    public static final EnumProperty<DoubleBlockType> CHEST_TYPE = EnumProperty.create("type", DoubleBlockType.class);
    private static final VoxelShape SINGLE_SHAPE = Block.box(1, 0, 1, 15, 14, 15);
    private static final VoxelShape BOTTOM_SHAPE = Block.box(1, 0, 1, 15, 16, 15);
    private static final VoxelShape NORTH_SOUTH_SHAPE = Block.box(1, 0, 0, 15, 14, 15);
    private static final VoxelShape SOUTH_NORTH_SHAPE = Block.box(1, 0, 1, 16, 14, 15);
    private static final VoxelShape EAST_WEST_SHAPE = Block.box(1, 0, 1, 15, 14, 16);
    private static final VoxelShape WEST_EAST_SHAPE = Block.box(0, 0, 1, 15, 14, 15);

    public ChestBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.defaultBlockState()
                .setValue(CHEST_TYPE, DoubleBlockType.SINGLE)
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ChestBlockEntity entity) {
            entity.toggleOpen(player);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHEST_TYPE);
        builder.add(HORIZONTAL_FACING);
        builder.add(OPEN);
        builder.add(WATERLOGGED);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        DoubleBlockType type = state.getValue(CHEST_TYPE);
        if (type == DoubleBlockType.SINGLE || type == DoubleBlockType.TOP) {
            return SINGLE_SHAPE;
        } else if (type == DoubleBlockType.BOTTOM) {
            return BOTTOM_SHAPE;
        }

        int offset = switch (type) {
            case LEFT -> 1; case RIGHT -> 3;
            case FRONT -> 0; case BACK -> 2;
            default -> throw Utils.codeError(1);
        };

        int index = (state.getValue(HORIZONTAL_FACING).get2DDataValue() + offset) % 4;

        return switch (index) {
            case 0 -> NORTH_SOUTH_SHAPE; case 1 -> SOUTH_NORTH_SHAPE;
            case 2 -> EAST_WEST_SHAPE; case 3 -> WEST_EAST_SHAPE;
            default -> throw Utils.codeError(2);
        };
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return ModBlockEntities.CHEST;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createEntityTicker(type, ModBlockEntities.CHEST, ChestBlockEntity::progressLidAnimation);
    }
}
