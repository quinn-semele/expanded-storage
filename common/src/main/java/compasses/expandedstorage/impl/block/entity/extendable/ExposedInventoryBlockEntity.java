package compasses.expandedstorage.impl.block.entity.extendable;

import compasses.expandedstorage.impl.inventory.ExposedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ExposedInventoryBlockEntity extends OpenableBlockEntity implements ExposedInventory {
    private final NonNullList<ItemStack> items;

    public ExposedInventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, ResourceLocation blockId, Component defaultName, int inventorySize) {
        super(type, pos, state, blockId, defaultName);
        items = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.isValidAndPlayerInRange(player);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        this.loadInventoryFromTag(tag, provider);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        this.saveInventoryToTag(tag, provider);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public Container getInventory() {
        return this;
    }
}
