package dev.compasses.expandedstorage.block.entity;

import dev.compasses.expandedstorage.block.misc.LidController;
import dev.compasses.expandedstorage.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ChestBlockEntity extends BlockEntity {
    private final LidController lidController = new LidController();

    public ChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHEST, pos, state);
    }

    public static void progressLidAnimation(Level level, BlockPos pos, BlockState state, ChestBlockEntity entity) {
        entity.lidController.tick();
    }

    public void toggleOpen(Player player) {
        if (lidController.isClosed()) {
            lidController.startViewingInventory(player);
        } else {
            lidController.stopViewingInventory(player);
        }
    }

    public LidController lidController() {
        return lidController;
    }
}
