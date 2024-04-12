package compasses.expandedstorage.impl;

import com.google.common.collect.ImmutableSet;
import compasses.expandedstorage.api.EsChestType;
import compasses.expandedstorage.impl.block.AbstractChestBlock;
import compasses.expandedstorage.impl.block.BarrelBlock;
import compasses.expandedstorage.impl.block.ChestBlock;
import compasses.expandedstorage.impl.block.CopperBarrelBlock;
import compasses.expandedstorage.impl.block.CopperMiniStorageBlock;
import compasses.expandedstorage.impl.block.MiniStorageBlock;
import compasses.expandedstorage.impl.block.MossChestBlock;
import compasses.expandedstorage.impl.block.OpenableBlock;
import compasses.expandedstorage.impl.block.entity.BarrelBlockEntity;
import compasses.expandedstorage.impl.block.entity.ChestBlockEntity;
import compasses.expandedstorage.impl.block.entity.MiniStorageBlockEntity;
import compasses.expandedstorage.impl.block.entity.OldChestBlockEntity;
import compasses.expandedstorage.impl.block.entity.extendable.OpenableBlockEntity;
import compasses.expandedstorage.impl.block.misc.DoubleItemAccess;
import compasses.expandedstorage.impl.block.strategies.ItemAccess;
import compasses.expandedstorage.impl.block.strategies.Lockable;
import compasses.expandedstorage.impl.entity.ChestMinecart;
import compasses.expandedstorage.impl.item.BlockMutatorBehaviour;
import compasses.expandedstorage.impl.item.ChestMinecartItem;
import compasses.expandedstorage.impl.item.EntityInteractableItem;
import compasses.expandedstorage.impl.item.MutationMode;
import compasses.expandedstorage.impl.item.StorageConversionKit;
import compasses.expandedstorage.impl.item.StorageMutator;
import compasses.expandedstorage.impl.item.ToolUsageResult;
import compasses.expandedstorage.impl.misc.CommonPlatformHelper;
import compasses.expandedstorage.impl.misc.ESDataComponents;
import compasses.expandedstorage.impl.misc.Tier;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.recipe.BlockConversionRecipe;
import compasses.expandedstorage.impl.recipe.ConversionRecipeManager;
import compasses.expandedstorage.impl.registration.Content;
import compasses.expandedstorage.impl.registration.ContentConsumer;
import compasses.expandedstorage.impl.registration.ModItems;
import compasses.expandedstorage.impl.registration.NamedValue;
import compasses.expandedstorage.impl.registration.ObjectConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class CommonMain {
    public static final ResourceLocation BARREL_OBJECT_TYPE = Utils.id("barrel");
    public static final ResourceLocation CHEST_OBJECT_TYPE = Utils.id("chest");
    public static final ResourceLocation OLD_CHEST_OBJECT_TYPE = Utils.id("old_chest");
    public static final ResourceLocation MINI_STORAGE_OBJECT_TYPE = Utils.id("mini_chest");

    private static final Map<Map.Entry<Predicate<Block>, MutationMode>, BlockMutatorBehaviour> BLOCK_MUTATOR_BEHAVIOURS = new HashMap<>();
    private static final Map<ResourceLocation, ResourceLocation[]> CHEST_TEXTURES = new HashMap<>();

    private static NamedValue<BlockEntityType<ChestBlockEntity>> chestBlockEntityType;
    private static NamedValue<BlockEntityType<OldChestBlockEntity>> oldChestBlockEntityType;
    private static NamedValue<BlockEntityType<BarrelBlockEntity>> barrelBlockEntityType;
    private static NamedValue<BlockEntityType<MiniStorageBlockEntity>> miniStorageBlockEntityType;
    private static CommonPlatformHelper platformHelper;

    public static BlockEntityType<ChestBlockEntity> getChestBlockEntityType() {
        return chestBlockEntityType.getValue();
    }

    public static BlockEntityType<OldChestBlockEntity> getOldChestBlockEntityType() {
        return oldChestBlockEntityType.getValue();
    }

    public static BlockEntityType<BarrelBlockEntity> getBarrelBlockEntityType() {
        return barrelBlockEntityType.getValue();
    }

    public static BlockEntityType<MiniStorageBlockEntity> getMiniStorageBlockEntityType() {
        return miniStorageBlockEntityType.getValue();
    }

    private static void defineTierUpgradePath(List<NamedValue<Item>> items, boolean wrapTooltipManually, Tier... tiers) {
        int numTiers = tiers.length;
        for (int fromIndex = 0; fromIndex < numTiers - 1; fromIndex++) {
            Tier fromTier = tiers[fromIndex];
            for (int toIndex = fromIndex + 1; toIndex < numTiers; toIndex++) {
                Tier toTier = tiers[toIndex];
                ResourceLocation itemId = Utils.id(fromTier.getId().getPath() + "_to_" + toTier.getId().getPath() + "_conversion_kit");
                Item.Properties settings = fromTier.getItemSettings()
                                                   .andThen(toTier.getItemSettings())
                                                   .apply(new Item.Properties().stacksTo(16));
                items.add(new NamedValue<>(itemId, () -> new StorageConversionKit(settings, fromTier.getId(), toTier.getId(), wrapTooltipManually)));
            }
        }
    }

    public static void declareChestTextures(ResourceLocation block, ResourceLocation singleTexture, ResourceLocation leftTexture, ResourceLocation rightTexture, ResourceLocation topTexture, ResourceLocation bottomTexture, ResourceLocation frontTexture, ResourceLocation backTexture) {
        if (!CommonMain.CHEST_TEXTURES.containsKey(block)) {
            ResourceLocation[] collection = {topTexture, bottomTexture, frontTexture, backTexture, leftTexture, rightTexture, singleTexture};
            CommonMain.CHEST_TEXTURES.put(block, collection);
        } else {
            throw new IllegalArgumentException("Tried registering chest textures for \"" + block + "\" which already has textures.");
        }
    }

    public static ResourceLocation getChestTexture(ResourceLocation block, EsChestType chestType) {
        if (CommonMain.CHEST_TEXTURES.containsKey(block)) {
            return CommonMain.CHEST_TEXTURES.get(block)[chestType.ordinal()];
        }
        return MissingTextureAtlasSprite.getLocation();
    }

    private static void registerMutationBehaviour(Predicate<Block> predicate, MutationMode mode, BlockMutatorBehaviour behaviour) {
        CommonMain.BLOCK_MUTATOR_BEHAVIOURS.put(Map.entry(predicate, mode), behaviour);
    }

    public static BlockMutatorBehaviour getBlockMutatorBehaviour(Block block, MutationMode mode) {
        for (Map.Entry<Map.Entry<Predicate<Block>, MutationMode>, BlockMutatorBehaviour> entry : CommonMain.BLOCK_MUTATOR_BEHAVIOURS.entrySet()) {
            Map.Entry<Predicate<Block>, MutationMode> pair = entry.getKey();
            if (pair.getValue() == mode && pair.getKey().test(block)) return entry.getValue();
        }
        return null;
    }

    public static void constructContent(CommonPlatformHelper helper, Function<OpenableBlockEntity, ItemAccess> itemAccess, Supplier<Lockable> lockable,
                                        boolean isClient, ContentConsumer contentRegistrationConsumer,
            /*Base*/ boolean manuallyWrapTooltips,
            /*Chest*/ BiFunction<ChestBlock, Item.Properties, BlockItem> chestItemMaker, Function<OpenableBlockEntity, ItemAccess> chestAccessMaker,
            /*Minecart Chest*/ BiFunction<Item.Properties, ResourceLocation, ChestMinecartItem> chestMinecartItemMaker,
            /*Old Chest*/
            /*Barrel*/ TagKey<Block> barrelTag,
            /*Mini Storage*/ BiFunction<MiniStorageBlock, Item.Properties, BlockItem> miniChestItemMaker) {
        platformHelper = helper;
        final Tier woodTier = new Tier(Utils.WOOD_TIER_ID, Utils.WOOD_STACK_COUNT, UnaryOperator.identity(), UnaryOperator.identity());
        final Tier copperTier = new Tier(Utils.COPPER_TIER_ID, 45, Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
        final Tier ironTier = new Tier(Utils.id("iron"), 54, Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
        final Tier goldTier = new Tier(Utils.id("gold"), 81, Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
        final Tier diamondTier = new Tier(Utils.id("diamond"), 108, Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
        final Tier obsidianTier = new Tier(Utils.id("obsidian"), 108, Properties::requiresCorrectToolForDrops, UnaryOperator.identity());
        final Tier netheriteTier = new Tier(Utils.id("netherite"), 135, Properties::requiresCorrectToolForDrops, Item.Properties::fireResistant);
        final Properties woodSettings = Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5f).sound(SoundType.WOOD).ignitedByLava();
        final Properties pumpkinSettings = Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.DIDGERIDOO).strength(1.0F).sound(SoundType.WOOD);
        final Properties bambooSettings = Properties.of().mapColor(MapColor.PLANT).strength(1).sound(SoundType.BAMBOO).ignitedByLava();
        final Properties mossSettings = Properties.of().mapColor(MapColor.COLOR_GREEN).strength(0.1F).sound(SoundType.MOSS);
//        final Properties copperSettings = Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).strength(3.0F, 6.0F).sound(SoundType.COPPER);
        final Properties ironSettings = Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5, 6).sound(SoundType.METAL);
        final Properties goldSettings = Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BELL).strength(3, 6).sound(SoundType.METAL);
        final Properties diamondSettings = Properties.of().mapColor(MapColor.DIAMOND).strength(5, 6).sound(SoundType.METAL);
        final Properties obsidianSettings = Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).strength(50, 1200);
        final Properties netheriteSettings = Properties.of().mapColor(MapColor.COLOR_BLACK).strength(50, 1200).sound(SoundType.NETHERITE_BLOCK);
        List<ResourceLocation> stats = new ArrayList<>();
        Function<String, ResourceLocation> statMaker = (id) -> {
            ResourceLocation statId = Utils.id(id);
            stats.add(statId);
            return statId;
        };

        List<NamedValue<Item>> baseItems = new ArrayList<>(22);
        /*Base*/
        {
            baseItems.add(new NamedValue<>(Utils.id("storage_mutator"), () -> new StorageMutator(new Item.Properties().stacksTo(1))));
            CommonMain.defineTierUpgradePath(baseItems, manuallyWrapTooltips, woodTier, copperTier, ironTier, goldTier, diamondTier, obsidianTier, netheriteTier);
        }

        List<NamedValue<ChestBlock>> chestBlocks = new ArrayList<>(6 + 3);
        List<NamedValue<BlockItem>> chestItems = new ArrayList<>(6 + 3);
        List<NamedValue<EntityType<ChestMinecart>>> chestMinecartEntityTypes = new ArrayList<>(6 + 3);
        List<NamedValue<ChestMinecartItem>> chestMinecartItems = new ArrayList<>(6 + 3);
        /*Chest*/
        {
            final ResourceLocation woodStat = statMaker.apply("open_wood_chest");
            final ResourceLocation pumpkinStat = statMaker.apply("open_pumpkin_chest");
            final ResourceLocation presentStat = statMaker.apply("open_present");
            final ResourceLocation bambooStat = statMaker.apply("open_bamboo_chest");
            final ResourceLocation mossStat = statMaker.apply("open_moss_chest");
            final ResourceLocation ironStat = statMaker.apply("open_iron_chest");
            final ResourceLocation goldStat = statMaker.apply("open_gold_chest");
            final ResourceLocation diamondStat = statMaker.apply("open_diamond_chest");
            final ResourceLocation obsidianStat = statMaker.apply("open_obsidian_chest");
            final ResourceLocation netheriteStat = statMaker.apply("open_netherite_chest");

            final Properties presentSettings = Block.Properties.of().mapColor(state -> {
                EsChestType type = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
                if (type == EsChestType.SINGLE) return MapColor.COLOR_RED;
                else if (type == EsChestType.FRONT || type == EsChestType.BACK) return MapColor.PLANT;
                return MapColor.SNOW;
            }).strength(2.5f).sound(SoundType.WOOD);

            ObjectConsumer chestMaker = (id, stat, tier, settings) -> {
                NamedValue<ChestBlock> block = new NamedValue<>(id, () -> new ChestBlock(tier.getBlockSettings().apply(settings), stat, tier.getSlotCount()));
                NamedValue<BlockItem> item = new NamedValue<>(id, () -> chestItemMaker.apply(block.getValue(), tier.getItemSettings().apply(new Item.Properties())));
                ResourceLocation cartId = new ResourceLocation(id.getNamespace(), id.getPath() + "_minecart");
                NamedValue<ChestMinecartItem> cartItem = new NamedValue<>(cartId, () -> chestMinecartItemMaker.apply(new Item.Properties(), cartId));
                NamedValue<EntityType<ChestMinecart>> cartEntityType = new NamedValue<>(cartId, () -> new EntityType<>((type, level) -> {
                    return new ChestMinecart(type, level, cartItem.getValue(), block.getValue());
                }, MobCategory.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.scalable(0.98F, 0.7F), 1.0f, 8, 3, FeatureFlagSet.of()));
                chestBlocks.add(block);
                chestItems.add(item);
                chestMinecartEntityTypes.add(cartEntityType);
                chestMinecartItems.add(cartItem);
            };

            ObjectConsumer mossChestMaker = (id, stat, tier, settings) -> {
                NamedValue<ChestBlock> block = new NamedValue<>(id, () -> new MossChestBlock(tier.getBlockSettings().apply(settings), stat, tier.getSlotCount()));
                NamedValue<BlockItem> item = new NamedValue<>(id, () -> chestItemMaker.apply(block.getValue(), tier.getItemSettings().apply(new Item.Properties())));
                ResourceLocation cartId = new ResourceLocation(id.getNamespace(), id.getPath() + "_minecart");
                NamedValue<ChestMinecartItem> cartItem = new NamedValue<>(cartId, () -> chestMinecartItemMaker.apply(new Item.Properties(), cartId));
                NamedValue<EntityType<ChestMinecart>> cartEntityType = new NamedValue<>(cartId, () -> new EntityType<>((type, level) -> {
                    return new ChestMinecart(type, level, cartItem.getValue(), block.getValue());
                }, MobCategory.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.scalable(0.98F, 0.7F), 1.0f, 8, 3, FeatureFlagSet.of()));
                chestBlocks.add(block);
                chestItems.add(item);
                chestMinecartEntityTypes.add(cartEntityType);
                chestMinecartItems.add(cartItem);
            };

            chestMaker.apply(Utils.id("wood_chest"), woodStat, woodTier, woodSettings);
            chestMaker.apply(Utils.id("pumpkin_chest"), pumpkinStat, woodTier, pumpkinSettings);
            chestMaker.apply(Utils.id("present"), presentStat, woodTier, presentSettings);
            chestMaker.apply(Utils.id("bamboo_chest"), bambooStat, woodTier, bambooSettings);
            mossChestMaker.apply(Utils.id("moss_chest"), mossStat, woodTier, mossSettings);
            chestMaker.apply(Utils.id("iron_chest"), ironStat, ironTier, ironSettings);
            chestMaker.apply(Utils.id("gold_chest"), goldStat, goldTier, goldSettings);
            chestMaker.apply(Utils.id("diamond_chest"), diamondStat, diamondTier, diamondSettings);
            chestMaker.apply(Utils.id("obsidian_chest"), obsidianStat, obsidianTier, obsidianSettings);
            chestMaker.apply(Utils.id("netherite_chest"), netheriteStat, netheriteTier, netheriteSettings);

            if (isClient) {
                chestBlocks.forEach(block -> {
                    String blockId = block.getName().getPath();
                    CommonMain.declareChestTextures(block.getName(),
                            Utils.id("entity/chest/" + blockId + "_single"),
                            Utils.id("entity/chest/" + blockId + "_left"),
                            Utils.id("entity/chest/" + blockId + "_right"),
                            Utils.id("entity/chest/" + blockId + "_top"),
                            Utils.id("entity/chest/" + blockId + "_bottom"),
                            Utils.id("entity/chest/" + blockId + "_front"),
                            Utils.id("entity/chest/" + blockId + "_back")
                    );
                });
            }

            CommonMain.chestBlockEntityType = new NamedValue<>(CommonMain.CHEST_OBJECT_TYPE, () -> BlockEntityType.Builder.of((pos, state) -> new ChestBlockEntity(CommonMain.getChestBlockEntityType(), pos, state, ((OpenableBlock) state.getBlock()).getBlockId(), chestAccessMaker, lockable), chestBlocks.stream().map(NamedValue::getValue).toArray(ChestBlock[]::new)).build(Util.fetchChoiceType(References.BLOCK_ENTITY, CommonMain.CHEST_OBJECT_TYPE.toString())));
        }

        List<NamedValue<AbstractChestBlock>> oldChestBlocks = new ArrayList<>(6);
        List<NamedValue<BlockItem>> oldChestItems = new ArrayList<>(6);
        /*Old Chest*/
        {
            final ResourceLocation woodStat = statMaker.apply("open_old_wood_chest");
//            final ResourceLocation copperStat = statMaker.apply("open_old_copper_chest");
            final ResourceLocation ironStat = statMaker.apply("open_old_iron_chest");
            final ResourceLocation goldStat = statMaker.apply("open_old_gold_chest");
            final ResourceLocation diamondStat = statMaker.apply("open_old_diamond_chest");
            final ResourceLocation obsidianStat = statMaker.apply("open_old_obsidian_chest");
            final ResourceLocation netheriteStat = statMaker.apply("open_old_netherite_chest");
            ObjectConsumer chestMaker = (id, stat, tier, settings) -> {
                NamedValue<AbstractChestBlock> block = new NamedValue<>(id, () -> new AbstractChestBlock(tier.getBlockSettings().apply(settings), stat, tier.getSlotCount()));
                NamedValue<BlockItem> item = new NamedValue<>(id, () -> new BlockItem(block.getValue(), tier.getItemSettings().apply(new Item.Properties())));
                oldChestBlocks.add(block);
                oldChestItems.add(item);
            };

            chestMaker.apply(Utils.id("old_wood_chest"), woodStat, woodTier, woodSettings);
            chestMaker.apply(Utils.id("old_iron_chest"), ironStat, ironTier, ironSettings);
            chestMaker.apply(Utils.id("old_gold_chest"), goldStat, goldTier, goldSettings);
            chestMaker.apply(Utils.id("old_diamond_chest"), diamondStat, diamondTier, diamondSettings);
            chestMaker.apply(Utils.id("old_obsidian_chest"), obsidianStat, obsidianTier, obsidianSettings);
            chestMaker.apply(Utils.id("old_netherite_chest"), netheriteStat, netheriteTier, netheriteSettings);

            CommonMain.oldChestBlockEntityType = new NamedValue<>(CommonMain.OLD_CHEST_OBJECT_TYPE, () -> BlockEntityType.Builder.of((pos, state) -> new OldChestBlockEntity(CommonMain.getOldChestBlockEntityType(), pos, state, ((OpenableBlock) state.getBlock()).getBlockId(), chestAccessMaker, lockable), oldChestBlocks.stream().map(NamedValue::getValue).toArray(AbstractChestBlock[]::new)).build(Util.fetchChoiceType(References.BLOCK_ENTITY, CommonMain.OLD_CHEST_OBJECT_TYPE.toString())));
        }

        /*Both Chests*/
        {
            Predicate<Block> isChestBlock = b -> b instanceof AbstractChestBlock;
            CommonMain.registerMutationBehaviour(isChestBlock, MutationMode.MERGE, (context, level, state, pos, stack) -> {
                Player player = context.getPlayer();
                if (player == null) {
                    return ToolUsageResult.fail();
                }
                if (state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == EsChestType.SINGLE) {
                    if (stack.has(ESDataComponents.STORED_LOCATION)) {
                        BlockPos otherPos = stack.get(ESDataComponents.STORED_LOCATION);

                        BlockState otherState = level.getBlockState(otherPos);
                        BlockPos delta = otherPos.subtract(pos);
                        Direction direction = Direction.fromDelta(delta.getX(), delta.getY(), delta.getZ());

                        if (direction != null) {
                            if (state.getBlock() == otherState.getBlock()) {
                                if (otherState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) == EsChestType.SINGLE) {
                                    if (state.getValue(BlockStateProperties.HORIZONTAL_FACING) == otherState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                                        boolean firstIsDinnerbone = level.getBlockEntity(pos) instanceof OpenableBlockEntity blockEntity && blockEntity.isDinnerbone();
                                        boolean secondIsDinnerbone = level.getBlockEntity(otherPos) instanceof OpenableBlockEntity blockEntity && blockEntity.isDinnerbone();
                                        if (firstIsDinnerbone == secondIsDinnerbone) {
                                            if (!level.isClientSide()) {
                                                EsChestType chestType = AbstractChestBlock.getChestType(state.getValue(BlockStateProperties.HORIZONTAL_FACING), direction);
                                                level.setBlockAndUpdate(pos, state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, chestType));
                                                // note: other state is updated via neighbour update
                                                stack.remove(ESDataComponents.STORED_LOCATION);
                                                //noinspection ConstantConditions
                                                player.displayClientMessage(Component.translatable("tooltip.expandedstorage.storage_mutator.merge_end"), true);
                                            }
                                            return ToolUsageResult.slowSuccess();
                                        }
                                        player.displayClientMessage(Component.translatable("tooltip.expandedstorage.storage_mutator.merge_wrong_block"), true);
                                    } else {
                                        //noinspection ConstantConditions
                                        player.displayClientMessage(Component.translatable("tooltip.expandedstorage.storage_mutator.merge_wrong_facing"), true);
                                    }
                                } else {
                                    //noinspection ConstantConditions
                                    player.displayClientMessage(Component.translatable("tooltip.expandedstorage.storage_mutator.merge_already_double_chest"), true);
                                }
                            } else {
                                //noinspection ConstantConditions
                                player.displayClientMessage(Component.translatable("tooltip.expandedstorage.storage_mutator.merge_wrong_block"), true);
                            }
                        } else {
                            //noinspection ConstantConditions
                            player.displayClientMessage(Component.translatable("tooltip.expandedstorage.storage_mutator.merge_not_adjacent"), true);
                        }
                        stack.remove(ESDataComponents.STORED_LOCATION);
                    } else {
                        if (!level.isClientSide()) {
                            stack.set(ESDataComponents.STORED_LOCATION, pos);
                            //noinspection ConstantConditions
                            player.displayClientMessage(Component.translatable("tooltip.expandedstorage.storage_mutator.merge_start", Utils.ALT_USE), true);
                        }
                        return ToolUsageResult.fastSuccess();
                    }
                }
                return ToolUsageResult.fail();
            });
            CommonMain.registerMutationBehaviour(isChestBlock, MutationMode.SPLIT, (context, level, state, pos, stack) -> {
                if (state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE) != EsChestType.SINGLE) {
                    if (!level.isClientSide()) {
                        BlockPos otherPos = pos.relative(AbstractChestBlock.getDirectionToAttached(state));
                        level.setBlockAndUpdate(pos, state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, EsChestType.SINGLE));
                        level.setBlockAndUpdate(otherPos, state.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, EsChestType.SINGLE));
                    }
                    return ToolUsageResult.slowSuccess();
                }
                return ToolUsageResult.fail();
            });
            CommonMain.registerMutationBehaviour(isChestBlock, MutationMode.ROTATE, (context, level, state, pos, stack) -> {
                if (!level.isClientSide()) {
                    EsChestType chestType = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
                    if (chestType == EsChestType.SINGLE) {
                        level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise()));
                    } else {
                        BlockPos otherPos = pos.relative(AbstractChestBlock.getDirectionToAttached(state));
                        BlockState otherState = level.getBlockState(otherPos);
                        if (chestType == EsChestType.TOP || chestType == EsChestType.BOTTOM) {
                            level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise()));
                            level.setBlockAndUpdate(otherPos, otherState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise()));
                        } else {
                            level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite()).setValue(AbstractChestBlock.CURSED_CHEST_TYPE, state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE).getOpposite()));
                            level.setBlockAndUpdate(otherPos, otherState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite()).setValue(AbstractChestBlock.CURSED_CHEST_TYPE, otherState.getValue(AbstractChestBlock.CURSED_CHEST_TYPE).getOpposite()));
                        }
                    }
                }
                return ToolUsageResult.slowSuccess();
            });
        }

        List<NamedValue<BarrelBlock>> barrelBlocks = new ArrayList<>(6 - 1);
        List<NamedValue<BlockItem>> barrelItems = new ArrayList<>(6 - 1);
        /*Barrel*/
        {
            final ResourceLocation copperStat = statMaker.apply("open_copper_barrel");
            final ResourceLocation ironStat = statMaker.apply("open_iron_barrel");
            final ResourceLocation goldStat = statMaker.apply("open_gold_barrel");
            final ResourceLocation diamondStat = statMaker.apply("open_diamond_barrel");
            final ResourceLocation obsidianStat = statMaker.apply("open_obsidian_barrel");
            final ResourceLocation netheriteStat = statMaker.apply("open_netherite_barrel");

            final Properties copperBarrelSettings = Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(3, 6).sound(SoundType.WOOD).ignitedByLava();
            final Properties ironBarrelSettings = Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(5, 6).sound(SoundType.WOOD).ignitedByLava();
            final Properties goldBarrelSettings = Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(3, 6).sound(SoundType.WOOD).ignitedByLava();
            final Properties diamondBarrelSettings = Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(5, 6).sound(SoundType.WOOD).ignitedByLava();
            final Properties obsidianBarrelSettings = Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(50, 1200).sound(SoundType.WOOD).ignitedByLava();
            final Properties netheriteBarrelSettings = Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(50, 1200).sound(SoundType.WOOD);

            ObjectConsumer barrelMaker = (id, stat, tier, settings) -> {
                NamedValue<BarrelBlock> block = new NamedValue<>(id, () -> new BarrelBlock(tier.getBlockSettings().apply(settings), stat, tier.getSlotCount()));
                NamedValue<BlockItem> item = new NamedValue<>(id, () -> new BlockItem(block.getValue(), tier.getItemSettings().apply(new Item.Properties())));
                barrelBlocks.add(block);
                barrelItems.add(item);
            };

            BiConsumer<ResourceLocation, WeatheringCopper.WeatherState> copperBarrelMaker = (id, weatherState) -> {
                NamedValue<BarrelBlock> block = new NamedValue<>(id, () -> new CopperBarrelBlock(copperTier.getBlockSettings().apply(copperBarrelSettings), copperStat, copperTier.getSlotCount(), weatherState));
                NamedValue<BlockItem> item = new NamedValue<>(id, () -> new BlockItem(block.getValue(), copperTier.getItemSettings().apply(new Item.Properties())));
                barrelBlocks.add(block);
                barrelItems.add(item);
            };

            copperBarrelMaker.accept(Utils.id("copper_barrel"), WeatheringCopper.WeatherState.UNAFFECTED);
            copperBarrelMaker.accept(Utils.id("exposed_copper_barrel"), WeatheringCopper.WeatherState.EXPOSED);
            copperBarrelMaker.accept(Utils.id("weathered_copper_barrel"), WeatheringCopper.WeatherState.WEATHERED);
            copperBarrelMaker.accept(Utils.id("oxidized_copper_barrel"), WeatheringCopper.WeatherState.OXIDIZED);
            barrelMaker.apply(Utils.id("waxed_copper_barrel"), copperStat, copperTier, copperBarrelSettings);
            barrelMaker.apply(Utils.id("waxed_exposed_copper_barrel"), copperStat, copperTier, copperBarrelSettings);
            barrelMaker.apply(Utils.id("waxed_weathered_copper_barrel"), copperStat, copperTier, copperBarrelSettings);
            barrelMaker.apply(Utils.id("waxed_oxidized_copper_barrel"), copperStat, copperTier, copperBarrelSettings);
            barrelMaker.apply(Utils.id("iron_barrel"), ironStat, ironTier, ironBarrelSettings);
            barrelMaker.apply(Utils.id("gold_barrel"), goldStat, goldTier, goldBarrelSettings);
            barrelMaker.apply(Utils.id("diamond_barrel"), diamondStat, diamondTier, diamondBarrelSettings);
            barrelMaker.apply(Utils.id("obsidian_barrel"), obsidianStat, obsidianTier, obsidianBarrelSettings);
            barrelMaker.apply(Utils.id("netherite_barrel"), netheriteStat, netheriteTier, netheriteBarrelSettings);

            CommonMain.barrelBlockEntityType = new NamedValue<>(CommonMain.BARREL_OBJECT_TYPE, () -> BlockEntityType.Builder.of((pos, state) -> new BarrelBlockEntity(CommonMain.getBarrelBlockEntityType(), pos, state, ((OpenableBlock) state.getBlock()).getBlockId(), itemAccess, lockable), barrelBlocks.stream().map(NamedValue::getValue).toArray(BarrelBlock[]::new)).build(Util.fetchChoiceType(References.BLOCK_ENTITY, CommonMain.BARREL_OBJECT_TYPE.toString())));

            Predicate<Block> isUpgradableBarrelBlock = (block) -> block instanceof BarrelBlock || block instanceof net.minecraft.world.level.block.BarrelBlock || block.defaultBlockState().is(barrelTag);

            CommonMain.registerMutationBehaviour(isUpgradableBarrelBlock, MutationMode.ROTATE, (context, level, state, pos, stack) -> {
                if (state.hasProperty(BlockStateProperties.FACING)) {
                    if (!level.isClientSide()) {
                        level.setBlockAndUpdate(pos, state.cycle(BlockStateProperties.FACING));
                    }
                    return ToolUsageResult.slowSuccess();
                }
                return ToolUsageResult.fail();
            });
        }

        List<NamedValue<MiniStorageBlock>> miniStorageBlocks = new ArrayList<>();
        List<NamedValue<BlockItem>> miniStorageItems = new ArrayList<>();
        /*Mini Storage Blocks*/
        {
            final ResourceLocation woodChestStat = statMaker.apply("open_wood_mini_chest");
            final ResourceLocation pumpkinChestStat = statMaker.apply("open_pumpkin_mini_chest");
            final ResourceLocation redPresentStat = statMaker.apply("open_red_mini_present");
            final ResourceLocation whitePresentStat = statMaker.apply("open_white_mini_present");
            final ResourceLocation candyCanePresentStat = statMaker.apply("open_candy_cane_mini_present");
            final ResourceLocation greenPresentStat = statMaker.apply("open_green_mini_present");
            final ResourceLocation lavenderPresentStat = statMaker.apply("open_lavender_mini_present");
            final ResourceLocation pinkAmethystPresentStat = statMaker.apply("open_pink_amethyst_mini_present");
            final ResourceLocation ironChestStat = statMaker.apply("open_iron_mini_chest");
            final ResourceLocation goldChestStat = statMaker.apply("open_gold_mini_chest");
            final ResourceLocation diamondChestStat = statMaker.apply("open_diamond_mini_chest");
            final ResourceLocation obsidianChestStat = statMaker.apply("open_obsidian_mini_chest");
            final ResourceLocation netheriteChestStat = statMaker.apply("open_netherite_mini_chest");
            final ResourceLocation barrelStat = statMaker.apply("open_mini_barrel");
            final ResourceLocation copperBarrelStat = statMaker.apply("open_copper_mini_barrel");
            final ResourceLocation ironBarrelStat = statMaker.apply("open_iron_mini_barrel");
            final ResourceLocation goldBarrelStat = statMaker.apply("open_gold_mini_barrel");
            final ResourceLocation diamondBarrelStat = statMaker.apply("open_diamond_mini_barrel");
            final ResourceLocation obsidianBarrelStat = statMaker.apply("open_obsidian_mini_barrel");
            final ResourceLocation netheriteBarrelStat = statMaker.apply("open_netherite_mini_barrel");

            // Init block settings
            final Properties redPresentSettings = Properties.of().mapColor(MapColor.COLOR_RED).strength(2.5f).sound(SoundType.WOOD);
            final Properties whitePresentSettings = Properties.of().mapColor(MapColor.SNOW).strength(2.5f).sound(SoundType.WOOD);
            final Properties candyCanePresentSettings = Properties.of().mapColor(MapColor.SNOW).strength(2.5f).sound(SoundType.WOOD);
            final Properties greenPresentSettings = Properties.of().mapColor(MapColor.PLANT).strength(2.5f).sound(SoundType.WOOD);
            final Properties lavenderPresentSettings = Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.5f).sound(SoundType.WOOD);
            final Properties pinkAmethystPresentSettings = Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.5f).sound(SoundType.WOOD);
            final Properties woodBarrelSettings = Properties.of().mapColor(MapColor.WOOD).strength(2.5F).sound(SoundType.WOOD);
            final Properties copperBarrelSettings = Properties.of().mapColor(MapColor.WOOD).strength(3, 6).sound(SoundType.WOOD);
            final Block.Properties ironBarrelSettings = Properties.of().mapColor(MapColor.WOOD).strength(5, 6).sound(SoundType.WOOD);
            final Block.Properties goldBarrelSettings = Properties.of().mapColor(MapColor.WOOD).strength(3, 6).sound(SoundType.WOOD);
            final Block.Properties diamondBarrelSettings = Properties.of().mapColor(MapColor.WOOD).strength(5, 6).sound(SoundType.WOOD);
            final Block.Properties obsidianBarrelSettings = Properties.of().mapColor(MapColor.WOOD).strength(50, 1200).sound(SoundType.WOOD);
            final Block.Properties netheriteBarrelSettings = Properties.of().mapColor(MapColor.WOOD).strength(50, 1200).sound(SoundType.WOOD);

            Function<Boolean, ObjectConsumer> miniStorageMaker = (hasRibbon) -> (id, stat, tier, settings) -> {
                NamedValue<MiniStorageBlock> block = new NamedValue<>(id, () -> new MiniStorageBlock(tier.getBlockSettings().apply(settings), stat, hasRibbon));
                NamedValue<BlockItem> item = new NamedValue<>(id, () -> miniChestItemMaker.apply(block.getValue(), tier.getItemSettings().apply(new Item.Properties())));
                miniStorageBlocks.add(block);
                miniStorageItems.add(item);
            };

            ObjectConsumer miniStorageMakerNoRibbon = miniStorageMaker.apply(false);
            ObjectConsumer miniStorageMakerRibbon = miniStorageMaker.apply(true);

            BiConsumer<ResourceLocation, WeatheringCopper.WeatherState> copperMiniBarrelMaker = (id, weatherState) -> {
                NamedValue<MiniStorageBlock> block = new NamedValue<>(id, () -> new CopperMiniStorageBlock(copperTier.getBlockSettings().apply(copperBarrelSettings), copperBarrelStat, weatherState));
                NamedValue<BlockItem> item = new NamedValue<>(id, () -> miniChestItemMaker.apply(block.getValue(), copperTier.getItemSettings().apply(new Item.Properties())));
                miniStorageBlocks.add(block);
                miniStorageItems.add(item);
            };

            miniStorageMakerNoRibbon.apply(Utils.id("vanilla_wood_mini_chest"), woodChestStat, woodTier, woodSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("wood_mini_chest"), woodChestStat, woodTier, woodSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("pumpkin_mini_chest"), pumpkinChestStat, woodTier, pumpkinSettings);
            miniStorageMakerRibbon.apply(Utils.id("red_mini_present"), redPresentStat, woodTier, redPresentSettings);
            miniStorageMakerRibbon.apply(Utils.id("white_mini_present"), whitePresentStat, woodTier, whitePresentSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("candy_cane_mini_present"), candyCanePresentStat, woodTier, candyCanePresentSettings);
            miniStorageMakerRibbon.apply(Utils.id("green_mini_present"), greenPresentStat, woodTier, greenPresentSettings);
            miniStorageMakerRibbon.apply(Utils.id("lavender_mini_present"), lavenderPresentStat, woodTier, lavenderPresentSettings);
            miniStorageMakerRibbon.apply(Utils.id("pink_amethyst_mini_present"), pinkAmethystPresentStat, woodTier, pinkAmethystPresentSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("iron_mini_chest"), ironChestStat, ironTier, ironSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("gold_mini_chest"), goldChestStat, goldTier, goldSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("diamond_mini_chest"), diamondChestStat, diamondTier, diamondSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("obsidian_mini_chest"), obsidianChestStat, obsidianTier, obsidianSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("netherite_mini_chest"), netheriteChestStat, netheriteTier, netheriteSettings);

            miniStorageMakerNoRibbon.apply(Utils.id("mini_barrel"), barrelStat, woodTier, woodBarrelSettings);
            copperMiniBarrelMaker.accept(Utils.id("copper_mini_barrel"), WeatheringCopper.WeatherState.UNAFFECTED);
            copperMiniBarrelMaker.accept(Utils.id("exposed_copper_mini_barrel"), WeatheringCopper.WeatherState.EXPOSED);
            copperMiniBarrelMaker.accept(Utils.id("weathered_copper_mini_barrel"), WeatheringCopper.WeatherState.WEATHERED);
            copperMiniBarrelMaker.accept(Utils.id("oxidized_copper_mini_barrel"), WeatheringCopper.WeatherState.OXIDIZED);
            miniStorageMakerNoRibbon.apply(Utils.id("waxed_copper_mini_barrel"), copperBarrelStat, copperTier, copperBarrelSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("waxed_exposed_copper_mini_barrel"), copperBarrelStat, copperTier, copperBarrelSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("waxed_weathered_copper_mini_barrel"), copperBarrelStat, copperTier, copperBarrelSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("waxed_oxidized_copper_mini_barrel"), copperBarrelStat, copperTier, copperBarrelSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("iron_mini_barrel"), ironBarrelStat, ironTier, ironBarrelSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("gold_mini_barrel"), goldBarrelStat, goldTier, goldBarrelSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("diamond_mini_barrel"), diamondBarrelStat, diamondTier, diamondBarrelSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("obsidian_mini_barrel"), obsidianBarrelStat, obsidianTier, obsidianBarrelSettings);
            miniStorageMakerNoRibbon.apply(Utils.id("netherite_mini_barrel"), netheriteBarrelStat, netheriteTier, netheriteBarrelSettings);

            CommonMain.miniStorageBlockEntityType = new NamedValue<>(CommonMain.MINI_STORAGE_OBJECT_TYPE, () -> BlockEntityType.Builder.of((pos, state) -> new MiniStorageBlockEntity(CommonMain.getMiniStorageBlockEntityType(), pos, state, ((OpenableBlock) state.getBlock()).getBlockId(), itemAccess, lockable), miniStorageBlocks.stream().map(NamedValue::getValue).toArray(MiniStorageBlock[]::new)).build(Util.fetchChoiceType(References.BLOCK_ENTITY, CommonMain.MINI_STORAGE_OBJECT_TYPE.toString())));

            Predicate<Block> isMiniStorage = b -> b instanceof MiniStorageBlock;
            CommonMain.registerMutationBehaviour(isMiniStorage, MutationMode.ROTATE, (context, level, state, pos, stack) -> {
                if (!level.isClientSide()) {
                    level.setBlockAndUpdate(pos, state.rotate(Rotation.CLOCKWISE_90));
                }
                return ToolUsageResult.slowSuccess();
            });
        }

        CommonMain.registerMutationBehaviour(b -> true, MutationMode.SWAP_THEME, (context, level, state, pos, stack) -> {
            BlockConversionRecipe<?> recipe = ConversionRecipeManager.INSTANCE.getBlockRecipe(state, stack);
            if (recipe != null) {
                return recipe.process(level, context.getPlayer(), stack, state, pos);
            }
            return ToolUsageResult.fail();
        });

        contentRegistrationConsumer.accept(new Content(
                stats,
                baseItems,

                chestBlocks,
                chestItems,
                chestMinecartEntityTypes,
                chestMinecartItems,
                chestBlockEntityType,

                oldChestBlocks,
                oldChestItems,
                oldChestBlockEntityType,

                barrelBlocks,
                barrelItems,
                barrelBlockEntityType,

                miniStorageBlocks,
                miniStorageItems,
                miniStorageBlockEntityType
        ));
    }

    public static <T> void iterateNamedList(List<NamedValue<? extends T>> list, BiConsumer<ResourceLocation, T> consumer) {
        list.forEach(it -> consumer.accept(it.getName(), it.getValue()));
    }

    public static <T> Optional<ItemAccess<T>> getItemAccess(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (blockEntity instanceof OldChestBlockEntity entity) {
            DoubleItemAccess<T> access = (DoubleItemAccess<T>) entity.getItemAccess();
            EsChestType type = state.getValue(AbstractChestBlock.CURSED_CHEST_TYPE);
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (access.hasCachedAccess() || type == EsChestType.SINGLE) {
                return Optional.of(access);
            }
            if (level.getBlockEntity(pos.relative(AbstractChestBlock.getDirectionToAttached(type, facing))) instanceof OldChestBlockEntity otherEntity) {
                DoubleItemAccess<T> otherAccess = (DoubleItemAccess<T>) otherEntity.getItemAccess();
                if (otherAccess.hasCachedAccess()) {
                    return Optional.of(otherAccess);
                }
                DoubleItemAccess<T> first, second;
                if (AbstractChestBlock.getBlockType(type) == DoubleBlockCombiner.BlockType.FIRST) {
                    first = access;
                    second = otherAccess;
                } else {
                    first = otherAccess;
                    second = access;
                }
                first.setOther(second);
                return Optional.of(first);
            }

        } else if (blockEntity instanceof OpenableBlockEntity entity) {
            return Optional.of((ItemAccess<T>) entity.getItemAccess());
        }
        return Optional.empty();
    }

    public static InteractionResult interactWithEntity(Level level, Player player, InteractionHand hand, Entity entity) {
        if (player.isSpectator() || !player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
        ItemStack handStack = player.getItemInHand(hand);
        if (handStack.getItem() instanceof EntityInteractableItem item) {
            if (player.getCooldowns().isOnCooldown(handStack.getItem())) {
                return InteractionResult.CONSUME;
            }
            InteractionResult result = item.es_interactEntity(level, entity, player, hand, handStack);
            if (result == InteractionResult.FAIL) {
                result = InteractionResult.CONSUME;
            }
            return result;
        }
        return InteractionResult.PASS;
    }

    @SuppressWarnings("unused")
    public static void generateDisplayItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, Consumer<ItemStack> output) {
        Consumer<Item> wrap = item -> output.accept(item.getDefaultInstance());
        Consumer<Item> sparrowWrap = item -> {
            wrap.accept(item);

            ItemStack stack = new ItemStack(item);
            stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(MiniStorageBlock.SPARROW, Boolean.TRUE));
            output.accept(stack);
        };

        for (MutationMode mode : MutationMode.values()) {
            ItemStack stack = new ItemStack(ModItems.STORAGE_MUTATOR);
            stack.set(ESDataComponents.MUTATOR_MODE, mode);
            output.accept(stack);
        }

        {
            ItemStack sparrowMutator = new ItemStack(ModItems.STORAGE_MUTATOR);
            sparrowMutator.set(ESDataComponents.MUTATOR_MODE, MutationMode.SWAP_THEME);
            sparrowMutator.set(DataComponents.CUSTOM_NAME, Component.literal("Sparrow").withStyle(ChatFormatting.ITALIC));
            output.accept(sparrowMutator);
        }

        // todo: add lock stuff when finished and ported.
        wrap.accept(ModItems.WOOD_TO_COPPER_CONVERSION_KIT);
        wrap.accept(ModItems.WOOD_TO_IRON_CONVERSION_KIT);
        wrap.accept(ModItems.WOOD_TO_GOLD_CONVERSION_KIT);
        wrap.accept(ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT);
        wrap.accept(ModItems.WOOD_TO_OBSIDIAN_CONVERSION_KIT);
        wrap.accept(ModItems.WOOD_TO_NETHERITE_CONVERSION_KIT);
        wrap.accept(ModItems.COPPER_TO_IRON_CONVERSION_KIT);
        wrap.accept(ModItems.COPPER_TO_GOLD_CONVERSION_KIT);
        wrap.accept(ModItems.COPPER_TO_DIAMOND_CONVERSION_KIT);
        wrap.accept(ModItems.COPPER_TO_OBSIDIAN_CONVERSION_KIT);
        wrap.accept(ModItems.COPPER_TO_NETHERITE_CONVERSION_KIT);
        wrap.accept(ModItems.IRON_TO_GOLD_CONVERSION_KIT);
        wrap.accept(ModItems.IRON_TO_DIAMOND_CONVERSION_KIT);
        wrap.accept(ModItems.IRON_TO_OBSIDIAN_CONVERSION_KIT);
        wrap.accept(ModItems.IRON_TO_NETHERITE_CONVERSION_KIT);
        wrap.accept(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT);
        wrap.accept(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT);
        wrap.accept(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT);
        wrap.accept(ModItems.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT);
        wrap.accept(ModItems.DIAMOND_TO_NETHERITE_CONVERSION_KIT);
        wrap.accept(ModItems.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT);
        wrap.accept(ModItems.WOOD_CHEST);
        wrap.accept(ModItems.PUMPKIN_CHEST);
        wrap.accept(ModItems.PRESENT);
        wrap.accept(ModItems.BAMBOO_CHEST);
        wrap.accept(ModItems.MOSS_CHEST);
        wrap.accept(ModItems.IRON_CHEST);
        wrap.accept(ModItems.GOLD_CHEST);
        wrap.accept(ModItems.DIAMOND_CHEST);
        wrap.accept(ModItems.OBSIDIAN_CHEST);
        wrap.accept(ModItems.NETHERITE_CHEST);
        wrap.accept(ModItems.WOOD_CHEST_MINECART);
        wrap.accept(ModItems.PUMPKIN_CHEST_MINECART);
        wrap.accept(ModItems.PRESENT_MINECART);
        wrap.accept(ModItems.BAMBOO_CHEST_MINECART);
        wrap.accept(ModItems.MOSS_CHEST_MINECART);
        wrap.accept(ModItems.IRON_CHEST_MINECART);
        wrap.accept(ModItems.GOLD_CHEST_MINECART);
        wrap.accept(ModItems.DIAMOND_CHEST_MINECART);
        wrap.accept(ModItems.OBSIDIAN_CHEST_MINECART);
        wrap.accept(ModItems.NETHERITE_CHEST_MINECART);
        wrap.accept(ModItems.OLD_WOOD_CHEST);
        wrap.accept(ModItems.OLD_IRON_CHEST);
        wrap.accept(ModItems.OLD_GOLD_CHEST);
        wrap.accept(ModItems.OLD_DIAMOND_CHEST);
        wrap.accept(ModItems.OLD_OBSIDIAN_CHEST);
        wrap.accept(ModItems.OLD_NETHERITE_CHEST);
        wrap.accept(ModItems.COPPER_BARREL);
        wrap.accept(ModItems.EXPOSED_COPPER_BARREL);
        wrap.accept(ModItems.WEATHERED_COPPER_BARREL);
        wrap.accept(ModItems.OXIDIZED_COPPER_BARREL);
        wrap.accept(ModItems.WAXED_COPPER_BARREL);
        wrap.accept(ModItems.WAXED_EXPOSED_COPPER_BARREL);
        wrap.accept(ModItems.WAXED_WEATHERED_COPPER_BARREL);
        wrap.accept(ModItems.WAXED_OXIDIZED_COPPER_BARREL);
        wrap.accept(ModItems.IRON_BARREL);
        wrap.accept(ModItems.GOLD_BARREL);
        wrap.accept(ModItems.DIAMOND_BARREL);
        wrap.accept(ModItems.OBSIDIAN_BARREL);
        wrap.accept(ModItems.NETHERITE_BARREL);
        sparrowWrap.accept(ModItems.VANILLA_WOOD_MINI_CHEST);
        sparrowWrap.accept(ModItems.WOOD_MINI_CHEST);
        sparrowWrap.accept(ModItems.PUMPKIN_MINI_CHEST);
        sparrowWrap.accept(ModItems.RED_MINI_PRESENT);
        sparrowWrap.accept(ModItems.WHITE_MINI_PRESENT);
        sparrowWrap.accept(ModItems.CANDY_CANE_MINI_PRESENT);
        sparrowWrap.accept(ModItems.GREEN_MINI_PRESENT);
        sparrowWrap.accept(ModItems.LAVENDER_MINI_PRESENT);
        sparrowWrap.accept(ModItems.PINK_AMETHYST_MINI_PRESENT);
        sparrowWrap.accept(ModItems.IRON_MINI_CHEST);
        sparrowWrap.accept(ModItems.GOLD_MINI_CHEST);
        sparrowWrap.accept(ModItems.DIAMOND_MINI_CHEST);
        sparrowWrap.accept(ModItems.OBSIDIAN_MINI_CHEST);
        sparrowWrap.accept(ModItems.NETHERITE_MINI_CHEST);
        sparrowWrap.accept(ModItems.MINI_BARREL);
        sparrowWrap.accept(ModItems.COPPER_MINI_BARREL);
        sparrowWrap.accept(ModItems.EXPOSED_COPPER_MINI_BARREL);
        sparrowWrap.accept(ModItems.WEATHERED_COPPER_MINI_BARREL);
        sparrowWrap.accept(ModItems.OXIDIZED_COPPER_MINI_BARREL);
        sparrowWrap.accept(ModItems.WAXED_COPPER_MINI_BARREL);
        sparrowWrap.accept(ModItems.WAXED_EXPOSED_COPPER_MINI_BARREL);
        sparrowWrap.accept(ModItems.WAXED_WEATHERED_COPPER_MINI_BARREL);
        sparrowWrap.accept(ModItems.WAXED_OXIDIZED_COPPER_MINI_BARREL);
        sparrowWrap.accept(ModItems.IRON_MINI_BARREL);
        sparrowWrap.accept(ModItems.GOLD_MINI_BARREL);
        sparrowWrap.accept(ModItems.DIAMOND_MINI_BARREL);
        sparrowWrap.accept(ModItems.OBSIDIAN_MINI_BARREL);
        sparrowWrap.accept(ModItems.NETHERITE_MINI_BARREL);
    }

    public static CommonPlatformHelper platformHelper() {
        return platformHelper;
    }
}
