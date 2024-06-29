package compasses.expandedstorage.impl.mixin.common;

import compasses.expandedstorage.impl.block.CopperBarrelBlock;
import compasses.expandedstorage.impl.block.CopperMiniStorageBlock;
import compasses.expandedstorage.impl.block.misc.CopperBlockHelper;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Mixin(value = {CopperBarrelBlock.class, CopperMiniStorageBlock.class}, remap = false)
public class OxidizableBlockMixin extends Block {
    public OxidizableBlockMixin(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility action, boolean simulate) {
        boolean isRemovingOxidisation = action == ItemAbilities.AXE_SCRAPE;
        boolean isRemovingWax = action == ItemAbilities.AXE_WAX_OFF;
        if (isRemovingOxidisation || isRemovingWax) {
            Optional<BlockState> possibleValue;
            if (action == ItemAbilities.AXE_SCRAPE) {
                possibleValue = CopperBlockHelper.getPreviousOxidisedState(state);
            } else {
                possibleValue = CopperBlockHelper.getDewaxed(state);
            }
            if (possibleValue.isPresent()) {
                return possibleValue.get();
            }
        }

        return null;
    }
}
