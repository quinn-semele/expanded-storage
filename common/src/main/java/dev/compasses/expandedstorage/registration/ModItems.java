package dev.compasses.expandedstorage.registration;

import dev.compasses.expandedstorage.Utils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModItems {
    public static final Item OAK_CHEST = register("oak_chest", new BlockItem(ModBlocks.OAK_CHEST, new Item.Properties()));
    public static final Item SPRUCE_CHEST = register("spruce_chest", new BlockItem(ModBlocks.SPRUCE_CHEST, new Item.Properties()));
    public static final Item BIRCH_CHEST = register("birch_chest", new BlockItem(ModBlocks.BIRCH_CHEST, new Item.Properties()));
    public static final Item COPPER_CHEST = register("copper_chest", new BlockItem(ModBlocks.COPPER_CHEST, new Item.Properties()));
    public static final Item EXPOSED_COPPER_CHEST = register("exposed_copper_chest", new BlockItem(ModBlocks.EXPOSED_COPPER_CHEST, new Item.Properties()));
    public static final Item WEATHERED_COPPER_CHEST = register("weathered_copper_chest", new BlockItem(ModBlocks.WEATHERED_COPPER_CHEST, new Item.Properties()));
    public static final Item OXIDIZED_COPPER_CHEST = register("oxidized_copper_chest", new BlockItem(ModBlocks.OXIDIZED_COPPER_CHEST, new Item.Properties()));
    public static final Item IRON_CHEST = register("iron_chest", new BlockItem(ModBlocks.IRON_CHEST, new Item.Properties()));
    public static final Item GOLDEN_CHEST = register("golden_chest", new BlockItem(ModBlocks.GOLDEN_CHEST, new Item.Properties()));
    public static final Item DIAMOND_CHEST = register("diamond_chest", new BlockItem(ModBlocks.DIAMOND_CHEST, new Item.Properties()));
    public static final Item OBSIDIAN_CHEST = register("obsidian_chest", new BlockItem(ModBlocks.OBSIDIAN_CHEST, new Item.Properties()));
    public static final Item NETHERITE_CHEST = register("netherite_chest", new BlockItem(ModBlocks.NETHERITE_CHEST, new Item.Properties().fireResistant()));

    public static final Item COMMON_BARREL = register("barrel", new BlockItem(ModBlocks.COMMON_BARREL, new Item.Properties()));
    public static final Item COPPER_BARREL = register("copper_barrel", new BlockItem(ModBlocks.COPPER_BARREL, new Item.Properties()));
    public static final Item EXPOSED_COPPER_BARREL = register("exposed_copper_barrel", new BlockItem(ModBlocks.EXPOSED_COPPER_BARREL, new Item.Properties()));
    public static final Item WEATHERED_COPPER_BARREL = register("weathered_copper_barrel", new BlockItem(ModBlocks.WEATHERED_COPPER_BARREL, new Item.Properties()));
    public static final Item OXIDIZED_COPPER_BARREL = register("oxidized_copper_barrel", new BlockItem(ModBlocks.OXIDIZED_COPPER_BARREL, new Item.Properties()));
    public static final Item IRON_BARREL = register("iron_barrel", new BlockItem(ModBlocks.IRON_BARREL, new Item.Properties()));
    public static final Item GOLDEN_BARREL = register("golden_barrel", new BlockItem(ModBlocks.GOLDEN_BARREL, new Item.Properties()));
    public static final Item DIAMOND_BARREL = register("diamond_barrel", new BlockItem(ModBlocks.DIAMOND_BARREL, new Item.Properties()));
    public static final Item OBSIDIAN_BARREL = register("obsidian_barrel", new BlockItem(ModBlocks.OBSIDIAN_BARREL, new Item.Properties()));
    public static final Item NETHERITE_BARREL = register("netherite_barrel", new BlockItem(ModBlocks.NETHERITE_BARREL, new Item.Properties().fireResistant()));

    public static final Item[] SHULKER_BOXES = Arrays.stream(ModBlocks.SHULKER_BOXES)
            .map(block -> register(block.color().prefix() + "shulker_box", new BlockItem(block, new Item.Properties())))
            .toArray(Item[]::new);

    public static final Item[] UPGRADES = registerUpgrades(
            List.of(
                    Map.entry("common", properties -> {}),
                    Map.entry("copper", properties -> {}),
                    Map.entry("iron", properties -> {}),
                    Map.entry("golden", properties -> {}),
                    Map.entry("diamond", properties -> {}),
                    Map.entry("obsidian", properties -> {}),
                    Map.entry("netherite", Item.Properties::fireResistant)
            )
    );

    private static Item[] registerUpgrades(List<Map.Entry<String, Consumer<Item.Properties>>> tiers) {
        Item[] upgrades = new Item[(tiers.size() * (tiers.size() - 1)) / 2];
        int insertionIndex= 0 ;

        for (int from = 0; from < tiers.size() - 1; from++) {
            var fromTier = tiers.get(from);
            for (int to = from + 1; to < tiers.size(); to++) {
                var toTier = tiers.get(to);

                Item.Properties properties = new Item.Properties();
                fromTier.getValue().accept(properties);
                toTier.getValue().accept(properties);

                upgrades[insertionIndex++] = register(fromTier.getKey() + "_to_" + toTier.getKey() + "_upgrade", new Item(properties));
            }
        }

        return upgrades;
    }

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(BuiltInRegistries.ITEM, Utils.id(name), item);
    }

    public static void registerContent() {
        // NO-OP - Registration is done by class loading.
    }

    private static void addContentToCreativeTab(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        Arrays.stream(UPGRADES).forEach(output::accept);

        Arrays.stream(ModBlocks.CHESTS).forEach(output::accept);

        Arrays.stream(ModBlocks.BARRELS).forEach(output::accept);

        Arrays.stream(SHULKER_BOXES).forEach(output::accept);
    }

    public static void registerCreativeTab(CreativeModeTab.Builder builder) {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Utils.id("tab"), builder
                .icon(ModItems.OXIDIZED_COPPER_CHEST::getDefaultInstance)
                .title(Component.translatable("itemGroup.expandedstorage.tab"))
                .displayItems(ModItems::addContentToCreativeTab)
                .build());
    }
}
