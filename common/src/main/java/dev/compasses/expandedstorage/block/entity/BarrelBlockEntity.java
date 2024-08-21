package dev.compasses.expandedstorage.block.entity;

import dev.compasses.expandedstorage.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BarrelBlockEntity extends BlockEntity {
    public BarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.BARREL, pos, blockState);
    }
}
