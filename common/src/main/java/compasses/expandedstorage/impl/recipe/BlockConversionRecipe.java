package compasses.expandedstorage.impl.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import compasses.expandedstorage.api.EsChestType;
import compasses.expandedstorage.api.ExpandedStorageAccessors;
import compasses.expandedstorage.impl.block.AbstractChestBlock;
import compasses.expandedstorage.impl.block.entity.extendable.OpenableBlockEntity;
import compasses.expandedstorage.impl.item.StorageConversionKit;
import compasses.expandedstorage.impl.item.ToolUsageResult;
import compasses.expandedstorage.impl.recipe.conditions.RecipeCondition;
import compasses.expandedstorage.impl.recipe.misc.PartialBlockState;
import compasses.expandedstorage.impl.recipe.misc.RecipeTool;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockConversionRecipe<O extends Block> extends ConversionRecipe<BlockState> {
    private final PartialBlockState<O> output;

    public BlockConversionRecipe(RecipeTool recipeTool, PartialBlockState<O> output, RecipeCondition input) {
        super(recipeTool, input);
        this.output = output;
    }

    private record InputState(BlockState state, BlockEntity entity) {

    }

    public final ToolUsageResult process(Level level, Player player, ItemStack tool, BlockState clickedState, BlockPos clickedPos) {
        List<BlockPos> convertPositions = new ArrayList<>();
        convertPositions.add(clickedPos);
        if (clickedState.hasProperty(BlockStateProperties.CHEST_TYPE)) {
            ChestType type = clickedState.getValue(BlockStateProperties.CHEST_TYPE);
            if (type != ChestType.SINGLE) {
                convertPositions.add(clickedPos.relative(ChestBlock.getConnectedDirection(clickedState)));
            }
        } else if (clickedState.hasProperty(AbstractChestBlock.CURSED_CHEST_TYPE) && clickedState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            ExpandedStorageAccessors.getAttachedChestDirection(clickedState).ifPresent(direction -> convertPositions.add(clickedPos.relative(direction)));
        }

        if (tool.getCount() < convertPositions.size() && !player.isCreative()) {
            return StorageConversionKit.NOT_ENOUGH_UPGRADES;
        }

        HashMap<BlockPos, InputState> originalStates = new HashMap<>();

        for (BlockPos position : convertPositions) {
            BlockEntity entity = level.getBlockEntity(position);

            if (!(entity instanceof OpenableBlockEntity || entity instanceof RandomizableContainerBlockEntity)) {
                return ToolUsageResult.fail();
            }
            originalStates.put(position, new InputState(level.getBlockState(position), entity));
        }

        int toolsUsed = 0;
        for (BlockPos position : convertPositions) {
            InputState input = originalStates.get(position);
            BlockState originalState = input.state();
            BlockState newState = output.getBlock().withPropertiesOf(originalState);
            if (originalState.hasProperty(BlockStateProperties.CHEST_TYPE) && newState.hasProperty(AbstractChestBlock.CURSED_CHEST_TYPE)) {
                newState = newState.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, EsChestType.from(originalState.getValue(BlockStateProperties.CHEST_TYPE)));
            }
            newState = output.transform(newState);
            if (newState != originalState) {
                List<ItemStack> originalItems;
                Component customName = null;
                CompoundTag tagForLock = input.entity().saveWithoutMetadata();

                if (input.entity() instanceof OpenableBlockEntity entity) {
                    originalItems = entity.getItems();
                    if (entity.hasCustomName()) {
                        customName = entity.getName();
                    }
                } else if (input.entity() instanceof RandomizableContainerBlockEntity entity) {
                    originalItems = entity.getItems();
                    customName = entity.getCustomName();
                } else {
                    throw new IllegalStateException();
                }

                level.removeBlockEntity(position);
                if (level.setBlockAndUpdate(position, newState)) {
                    if (level.getBlockEntity(position) instanceof OpenableBlockEntity entity) {
                        List<ItemStack> newInventory = entity.getItems();
                        int commonSize = Math.min(originalItems.size(), newInventory.size());
                        for (int i = 0; i < commonSize; i++) {
                            newInventory.set(i, originalItems.get(i));
                        }

                        if (newInventory.size() < originalItems.size()) { // Why in god's name is someone making an upgrade convert to a smaller chest...
                            for (int i = newInventory.size(); i < originalItems.size(); i++) {
                                Containers.dropItemStack(level, position.getX(), position.getY(), position.getZ(), originalItems.get(i));
                            }
                        }

                        entity.setCustomName(customName);
                        entity.getLockable().readLock(tagForLock);
                    }
                    toolsUsed++;
                } else {
                    level.setBlockEntity(input.entity());
                }
            }
        }

        if (recipeTool instanceof RecipeTool.UpgradeTool) {
            tool.setCount(tool.getCount() - toolsUsed);
        }

        return toolsUsed > 0 ? ToolUsageResult.slowSuccess() : ToolUsageResult.fail();
    }

    public void writeToBuffer(FriendlyByteBuf buffer) {
        recipeTool.writeToBuffer(buffer);
        output.writeToBuffer(buffer);
        buffer.writeResourceLocation(input.getNetworkId());
        input.writeToBuffer(buffer);
    }

    public static BlockConversionRecipe<?> readFromBuffer(FriendlyByteBuf buffer) {
        RecipeTool recipeTool = RecipeTool.fromNetworkBuffer(buffer);
        PartialBlockState<?> output = PartialBlockState.readFromBuffer(buffer);
        RecipeCondition inputs = RecipeCondition.readFromNetworkBuffer(buffer);
        return new BlockConversionRecipe<>(recipeTool, output, inputs);
    }

    @Override
    public JsonElement toJson() {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "expandedstorage:block_conversion");
        recipe.add("tool", recipeTool.toJson());
        recipe.add("result", output.toJson());
        recipe.add("inputs", input.toJson(null));
        return recipe;
    }
}
