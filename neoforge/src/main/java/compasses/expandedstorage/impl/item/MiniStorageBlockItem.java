package compasses.expandedstorage.impl.item;

import compasses.expandedstorage.impl.block.MiniStorageBlock;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class MiniStorageBlockItem extends BlockItem {
    public MiniStorageBlockItem(MiniStorageBlock block, Properties settings) {
        super(block, settings);
    }

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }
}
