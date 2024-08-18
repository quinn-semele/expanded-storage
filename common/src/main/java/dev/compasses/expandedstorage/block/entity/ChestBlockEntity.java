package dev.compasses.expandedstorage.block.entity;

import dev.compasses.expandedstorage.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ChestBlockEntity extends BlockEntity {
    private final ChestLidController lidController = new ChestLidController();

    public ChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHEST, pos, state);
    }

    @SuppressWarnings("unused")
    public static void progressLidAnimation(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        ((ChestBlockEntity) blockEntity).lidController.tickLid();
    }

    @Override
    public void setBlockState(BlockState state) {
        super.setBlockState(state);

        lidController.shouldBeOpen(state.getValue(BlockStateProperties.OPEN));
    }

    public float getOpenness(float partialTick) {
        return lidController.getOpenness(partialTick);
    }
}
