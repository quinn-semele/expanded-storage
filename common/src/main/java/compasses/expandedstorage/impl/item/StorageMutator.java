package compasses.expandedstorage.impl.item;

import compasses.expandedstorage.impl.CommonMain;
import compasses.expandedstorage.impl.misc.ESDataComponents;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.recipe.ConversionRecipeManager;
import compasses.expandedstorage.impl.recipe.EntityConversionRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public final class StorageMutator extends Item implements EntityInteractableItem {
    public StorageMutator(Properties settings) {
        super(settings);
    }

    public static MutationMode getMode(ItemStack stack) {
        if (!stack.has(ESDataComponents.MUTATOR_MODE)) {
            stack.set(ESDataComponents.MUTATOR_MODE, MutationMode.MERGE);
        }

        return stack.get(ESDataComponents.MUTATOR_MODE);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockMutatorBehaviour behaviour = CommonMain.getBlockMutatorBehaviour(state.getBlock(), StorageMutator.getMode(stack));
        if (behaviour != null) {
            ToolUsageResult returnValue = behaviour.attempt(context, level, state, pos, stack);
            if (returnValue.getResult().shouldSwing()) {
                //noinspection ConstantConditions
                context.getPlayer().getCooldowns().addCooldown(this, returnValue.getDelay());
            }
            return returnValue.getResult();
        }
        return InteractionResult.FAIL;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(hand);

            MutationMode nextMode = StorageMutator.getMode(stack).next();

            stack.set(ESDataComponents.MUTATOR_MODE, nextMode);
            stack.remove(ESDataComponents.STORED_LOCATION);

            if (!level.isClientSide())
                player.displayClientMessage(Component.translatable("tooltip.expandedstorage.storage_mutator.description_" + nextMode, Utils.ALT_USE), true);

            player.getCooldowns().addCooldown(this, Utils.TOOL_USAGE_QUICK_DELAY);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        StorageMutator.getMode(stack);
    }

    @NotNull
    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        StorageMutator.getMode(stack);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        MutationMode mode = StorageMutator.getMode(stack);
        list.add(Component.translatable("tooltip.expandedstorage.storage_mutator.tool_mode", Component.translatable("tooltip.expandedstorage.storage_mutator." + mode)).withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("tooltip.expandedstorage.storage_mutator.description_" + mode, Utils.ALT_USE).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult es_interactEntity(Level level, Entity entity, Player player, InteractionHand hand, ItemStack stack) {
        MutationMode mode = StorageMutator.getMode(stack);
        if (mode == MutationMode.SWAP_THEME) {
            EntityConversionRecipe<?> recipe = ConversionRecipeManager.INSTANCE.getEntityRecipe(entity, stack);
            if (recipe != null) {
                InteractionResult result = recipe.process(level, player, stack, entity);
                if (result.shouldSwing()) {
                    player.getCooldowns().addCooldown(this, Utils.TOOL_USAGE_DELAY);
                }
                return result;
            }
        }
        return InteractionResult.FAIL;
    }
}
