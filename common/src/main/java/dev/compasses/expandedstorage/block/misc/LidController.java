package dev.compasses.expandedstorage.block.misc;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity.AnimationStatus;

public class LidController {
    private AnimationStatus status = AnimationStatus.CLOSED;
    private int openCount = 0;
    private float previousOpenness = 0.0f;
    private float openness = 0.0f;

    public void startViewingInventory(Player player) {
        if (player.isSpectator()) {
            return;
        }

        openCount++;

        if (status == AnimationStatus.CLOSED) {
            status = AnimationStatus.OPENING;
        }
    }

    public void stopViewingInventory(Player player) {
        openCount--;

        if (status == AnimationStatus.OPENED) {
            status = AnimationStatus.CLOSING;
        }
    }

    public void tick() {
        this.previousOpenness = this.openness;
        switch (status) {
            case CLOSED, OPENED -> {

            }
            case OPENING -> {
                openness += 0.1f;

                if (openness >= 1.0f) {
                    status = AnimationStatus.OPENED;
                    openness = 1.0F;
                }
            }
            case CLOSING -> {
                openness -= 0.1f;

                if (openness <= 0.0f) {
                    status = AnimationStatus.CLOSED;
                    openness = 0.0f;
                }
            }
        }
    }

    public boolean isClosed() {
        return status == AnimationStatus.CLOSED;
    }

    public float getOpenness(float partialTick) {
        return Mth.lerp(partialTick, previousOpenness, openness);
    }
}
