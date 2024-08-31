package dev.compasses.expandedstorage.block.entity;

import dev.compasses.expandedstorage.block.ShulkerBoxBlock;
import dev.compasses.expandedstorage.block.misc.BlockColor;
import dev.compasses.expandedstorage.block.misc.LidController;
import dev.compasses.expandedstorage.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ShulkerBoxBlockEntity extends BlockEntity {
    private final BlockColor color;
    private final LidController lidController = new LidController();

    public ShulkerBoxBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHULKER_BOX, pos, state);
        this.color = ((ShulkerBoxBlock) state.getBlock()).color();
    }

    public static void progressLidAnimation(Level level, BlockPos pos, BlockState state, ShulkerBoxBlockEntity entity) {
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

    public BlockColor getColor() {
        return color;
    }
}
