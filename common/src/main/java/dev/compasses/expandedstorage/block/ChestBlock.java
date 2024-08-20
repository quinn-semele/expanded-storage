package dev.compasses.expandedstorage.block;

import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.entity.ChestBlockEntity;
import dev.compasses.expandedstorage.block.misc.CursedChestType;
import dev.compasses.expandedstorage.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChestBlock extends Block implements EntityBlock {
    private static final EnumProperty<CursedChestType> CHEST_TYPE = EnumProperty.create("type", CursedChestType.class);
    private static final VoxelShape SINGLE_SHAPE = Block.box(1, 0, 1, 15, 14, 15);
    private static final VoxelShape BOTTOM_SHAPE = Block.box(1, 0, 1, 15, 16, 15);
    private static final VoxelShape NORTH_SOUTH_SHAPE = Block.box(1, 0, 0, 15, 14, 15);
    private static final VoxelShape SOUTH_NORTH_SHAPE = Block.box(1, 0, 1, 16, 14, 15);
    private static final VoxelShape EAST_WEST_SHAPE = Block.box(1, 0, 1, 15, 14, 16);
    private static final VoxelShape WEST_EAST_SHAPE = Block.box(0, 0, 1, 15, 14, 15);

    public ChestBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(defaultBlockState()
                .setValue(CHEST_TYPE, CursedChestType.SINGLE)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(BlockStateProperties.OPEN, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHEST_TYPE);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(BlockStateProperties.OPEN);
    }

    @NotNull
    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        CursedChestType type = state.getValue(CHEST_TYPE);
        if (type == CursedChestType.SINGLE || type == CursedChestType.TOP) {
            return SINGLE_SHAPE;
        } else if (type == CursedChestType.BOTTOM) {
            return BOTTOM_SHAPE;
        }

        int offset = switch (type) {
            case LEFT -> 1; case RIGHT -> 3;
            case FRONT -> 0; case BACK -> 2;
            default -> throw Utils.codeError(1);
        };

        int index = (state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() + offset) % 4;

        return switch (index) {
            case 0 -> NORTH_SOUTH_SHAPE; case 1 -> SOUTH_NORTH_SHAPE;
            case 2 -> EAST_WEST_SHAPE; case 3 -> WEST_EAST_SHAPE;
            default -> throw Utils.codeError(2);
        };
    }

    @NotNull
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getCollisionShape(level, pos);
    }

    @NotNull
    @Override
    @SuppressWarnings("DataFlowIssue")
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.CHEST.create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() && blockEntityType == ModBlockEntities.CHEST ? ChestBlockEntity::progressLidAnimation : null;
    }
}
