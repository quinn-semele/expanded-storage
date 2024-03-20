package compasses.expandedstorage.impl.inventory.context;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class BlockContext extends BaseContext {
    private final BlockPos pos;

    public BlockContext(ServerLevel level, ServerPlayer player, BlockPos pos) {
        super(level, player);
        this.pos = pos;
    }

    public BlockPos getBlockPos() {
        return pos;
    }
}
