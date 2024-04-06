package compasses.expandedstorage.impl.item;

import compasses.expandedstorage.impl.CommonMain;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.recipe.BlockConversionRecipe;
import compasses.expandedstorage.impl.recipe.ConversionRecipeManager;
import compasses.expandedstorage.impl.recipe.EntityConversionRecipe;
import compasses.expandedstorage.impl.registration.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class StorageConversionKit extends Item implements EntityInteractableItem {
    private static final TagKey<EntityType<?>> ES_WOODEN_CHEST_MINECARTS = TagKey.create(Registries.ENTITY_TYPE, Utils.id("wooden_chest_minecarts"));
    public static final ToolUsageResult NOT_ENOUGH_UPGRADES = ToolUsageResult.fail();
    private final Component instructionsFirst;
    private final Component instructionsSecond;

    public StorageConversionKit(Properties settings, ResourceLocation fromTier, ResourceLocation toTier, boolean manuallyWrapTooltips) {
        super(settings);
        if (manuallyWrapTooltips) {
            this.instructionsFirst = Component.translatable("tooltip.expandedstorage.conversion_kit_" + fromTier.getPath() + "_" + toTier.getPath() + "_1", Utils.ALT_USE).withStyle(ChatFormatting.GRAY);
            this.instructionsSecond = Component.translatable("tooltip.expandedstorage.conversion_kit_" + fromTier.getPath() + "_" + toTier.getPath() + "_2", Utils.ALT_USE).withStyle(ChatFormatting.GRAY);
        } else {
            this.instructionsFirst = Component.translatable("tooltip.expandedstorage.conversion_kit_" + fromTier.getPath() + "_" + toTier.getPath() + "_1", Utils.ALT_USE).withStyle(ChatFormatting.GRAY).append(Component.translatable("tooltip.expandedstorage.conversion_kit_" + fromTier.getPath() + "_" + toTier.getPath() + "_2", Utils.ALT_USE).withStyle(ChatFormatting.GRAY));
            this.instructionsSecond = Component.literal("");
        }
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player != null) {
            if (player.isShiftKeyDown()) {
                ItemStack tool = context.getItemInHand();
                BlockPos pos = context.getClickedPos();
                BlockState state = level.getBlockState(pos);
                BlockConversionRecipe<?> recipe = ConversionRecipeManager.INSTANCE.getBlockRecipe(state, tool);
                if (recipe != null) {
                    if (level.isClientSide()) {
                        return InteractionResult.CONSUME;
                    }
                    ToolUsageResult result = recipe.process(level, player, tool, state, pos);
                    if (result == NOT_ENOUGH_UPGRADES) {
                        player.displayClientMessage(Component.translatable("tooltip.expandedstorage.conversion_kit.need_x_upgrades", 1), true);
                    } else if (result.getResult().shouldSwing()) {
                        player.getCooldowns().addCooldown(this, Utils.TOOL_USAGE_DELAY);
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    if (!level.isClientSide()) {
                        if (this == ModItems.WOOD_TO_COPPER_CONVERSION_KIT && CommonMain.platformHelper().isWoodenChest(state)) {
                            player.displayClientMessage(Component.translatable("tooltip.expandedstorage.conversion_kit.copper_chests_not_implemented"), true);
                        } else {
                            player.displayClientMessage(Component.translatable("tooltip.expandedstorage.conversion_kit.not_work_on_block"), true);
                        }
                    }
                }
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag context) {
        list.add(instructionsFirst);
        if (!instructionsSecond.getString().isEmpty()) {
            list.add(instructionsSecond);
        }
    }

    @Override
    public InteractionResult es_interactEntity(Level level, Entity entity, Player player, InteractionHand hand, ItemStack stack) {
        EntityConversionRecipe<?> recipe = ConversionRecipeManager.INSTANCE.getEntityRecipe(entity, stack);
        if (recipe != null) {
            if (recipe.process(level, player, stack, entity).shouldSwing()) {
                player.getCooldowns().addCooldown(this, Utils.TOOL_USAGE_DELAY);
                return InteractionResult.SUCCESS;
            }
        } else {
            if (!level.isClientSide()) {
                if (this == ModItems.WOOD_TO_COPPER_CONVERSION_KIT && entity.getType().is(ES_WOODEN_CHEST_MINECARTS)) {
                    player.displayClientMessage(Component.translatable("tooltip.expandedstorage.conversion_kit.copper_chests_not_implemented"), true);
                } else {
                    player.displayClientMessage(Component.translatable("tooltip.expandedstorage.conversion_kit.not_work_on_entity"), true);
                }
            }
        }
        return InteractionResult.FAIL;
    }
}
