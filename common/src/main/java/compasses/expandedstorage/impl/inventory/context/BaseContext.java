package compasses.expandedstorage.impl.inventory.context;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class BaseContext {
    private final ServerLevel level;
    private final ServerPlayer player;

    public BaseContext(ServerLevel level, ServerPlayer player) {
        this.level = level;
        this.player = player;
    }

    public final ServerLevel getLevel() {
        return level;
    }

    public final ServerPlayer getPlayer() {
        return player;
    }
}
