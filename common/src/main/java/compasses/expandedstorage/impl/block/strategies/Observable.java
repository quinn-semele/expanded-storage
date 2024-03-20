package compasses.expandedstorage.impl.block.strategies;

import net.minecraft.world.entity.player.Player;

public interface Observable {
    void playerStartViewing(Player player);

    void playerStopViewing(Player player);
}
