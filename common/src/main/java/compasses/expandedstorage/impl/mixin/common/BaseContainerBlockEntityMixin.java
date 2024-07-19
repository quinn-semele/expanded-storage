package compasses.expandedstorage.impl.mixin.common;

import compasses.expandedstorage.impl.misc.ItemAccessor;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BaseContainerBlockEntity.class)
public abstract class BaseContainerBlockEntityMixin implements ItemAccessor {
    @Shadow
    public abstract NonNullList<ItemStack> getItems();

    @Override
    public NonNullList<ItemStack> expandedstorage$getItems() {
        return getItems();
    }
}
