package compasses.expandedstorage.impl.item;

import compasses.expandedstorage.impl.block.MiniStorageBlock;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class MiniStorageBlockItem extends BlockItem {
    public MiniStorageBlockItem(MiniStorageBlock block, Properties settings) {
        super(block, settings);
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (allowedIn(category)) {
            items.add(this.getDefaultInstance());

            ItemStack stack = this.getDefaultInstance();
            CompoundTag tag = new CompoundTag();
            CompoundTag blockStateTag = new CompoundTag();
            blockStateTag.putString("sparrow", "true");
            tag.put("BlockStateTag", blockStateTag);
            stack.setTag(tag);
            items.add(stack);
        }
    }
}
