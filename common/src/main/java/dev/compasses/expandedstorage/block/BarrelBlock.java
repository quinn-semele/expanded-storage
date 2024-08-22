package dev.compasses.expandedstorage.block;

import dev.compasses.expandedstorage.block.misc.DoubleBlockType;
import dev.compasses.expandedstorage.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BarrelBlock extends InventoryBlock {
    public static final EnumProperty<DoubleBlockType> BARREL_TYPE = EnumProperty.create("type", DoubleBlockType.class, DoubleBlockType.SINGLE, DoubleBlockType.TOP, DoubleBlockType.BOTTOM);

    public BarrelBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(defaultBlockState()
                .setValue(BARREL_TYPE, DoubleBlockType.SINGLE)
                .setValue(BlockStateProperties.FACING, Direction.UP)
                .setValue(BlockStateProperties.OPEN, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BARREL_TYPE);
        builder.add(BlockStateProperties.FACING);
        builder.add(BlockStateProperties.OPEN);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return ModBlockEntities.BARREL;
    }

    @Override
    protected  VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }
}
