package dev.compasses.expandedstorage.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class InventoryBlock extends BaseEntityBlock {
    protected InventoryBlock(Properties properties) {
        super(properties);
    }

    protected abstract BlockEntityType<?> getBlockEntityType();

    @Override
    protected abstract VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntityType().create(pos, state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getCollisionShape(level, pos);
    }

    @Override // 1.21: No need to implement, only used for test/debug code.
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new IllegalStateException("Block#codec is not implemented on Expanded Storage's blocks.");
    }

    @Override
    @SuppressWarnings("deprecation")
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    protected <A extends BlockEntity, E extends BlockEntity> BlockEntityTicker<A> createEntityTicker(
            BlockEntityType<A> actualType, BlockEntityType<E> expectedType, BlockEntityTicker<E> ticker
    ) {
        //noinspection unchecked
        return actualType == expectedType ? (BlockEntityTicker<A>) ticker : null;
    }
}
