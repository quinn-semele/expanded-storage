package compasses.expandedstorage.impl.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockMutatorBehaviour {
    ToolUsageResult attempt(UseOnContext context, Level level, BlockState state, BlockPos pos, ItemStack stack);
}
