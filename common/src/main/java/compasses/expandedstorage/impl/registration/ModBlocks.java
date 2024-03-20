package compasses.expandedstorage.impl.registration;

import compasses.expandedstorage.impl.block.AbstractChestBlock;
import compasses.expandedstorage.impl.block.BarrelBlock;
import compasses.expandedstorage.impl.block.ChestBlock;
import compasses.expandedstorage.impl.block.CopperBarrelBlock;
import compasses.expandedstorage.impl.block.CopperMiniStorageBlock;
import compasses.expandedstorage.impl.block.MiniStorageBlock;
import compasses.expandedstorage.impl.block.OpenableBlock;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class ModBlocks {
    public static final ChestBlock WOOD_CHEST = block(Utils.id("wood_chest"));
    public static final ChestBlock PUMPKIN_CHEST = block(Utils.id("pumpkin_chest"));
    public static final ChestBlock PRESENT = block(Utils.id("present"));
    public static final ChestBlock BAMBOO_CHEST = block(Utils.id("bamboo_chest"));
    public static final ChestBlock MOSS_CHEST = block(Utils.id("moss_chest"));
    public static final ChestBlock IRON_CHEST = block(Utils.id("iron_chest"));
    public static final ChestBlock GOLD_CHEST = block(Utils.id("gold_chest"));
    public static final ChestBlock DIAMOND_CHEST = block(Utils.id("diamond_chest"));
    public static final ChestBlock OBSIDIAN_CHEST = block(Utils.id("obsidian_chest"));
    public static final ChestBlock NETHERITE_CHEST = block(Utils.id("netherite_chest"));

    public static final AbstractChestBlock OLD_WOOD_CHEST = block(Utils.id("old_wood_chest"));
    public static final AbstractChestBlock OLD_IRON_CHEST = block(Utils.id("old_iron_chest"));
    public static final AbstractChestBlock OLD_GOLD_CHEST = block(Utils.id("old_gold_chest"));
    public static final AbstractChestBlock OLD_DIAMOND_CHEST = block(Utils.id("old_diamond_chest"));
    public static final AbstractChestBlock OLD_OBSIDIAN_CHEST = block(Utils.id("old_obsidian_chest"));
    public static final AbstractChestBlock OLD_NETHERITE_CHEST = block(Utils.id("old_netherite_chest"));

    public static final CopperBarrelBlock COPPER_BARREL = block(Utils.id("copper_barrel"));
    public static final CopperBarrelBlock EXPOSED_COPPER_BARREL = block(Utils.id("exposed_copper_barrel"));
    public static final CopperBarrelBlock WEATHERED_COPPER_BARREL = block(Utils.id("weathered_copper_barrel"));
    public static final CopperBarrelBlock OXIDIZED_COPPER_BARREL = block(Utils.id("oxidized_copper_barrel"));
    public static final BarrelBlock WAXED_COPPER_BARREL = block(Utils.id("waxed_copper_barrel"));
    public static final BarrelBlock WAXED_EXPOSED_COPPER_BARREL = block(Utils.id("waxed_exposed_copper_barrel"));
    public static final BarrelBlock WAXED_WEATHERED_COPPER_BARREL = block(Utils.id("waxed_weathered_copper_barrel"));
    public static final BarrelBlock WAXED_OXIDIZED_COPPER_BARREL = block(Utils.id("waxed_oxidized_copper_barrel"));
    public static final BarrelBlock IRON_BARREL = block(Utils.id("iron_barrel"));
    public static final BarrelBlock GOLD_BARREL = block(Utils.id("gold_barrel"));
    public static final BarrelBlock DIAMOND_BARREL = block(Utils.id("diamond_barrel"));
    public static final BarrelBlock OBSIDIAN_BARREL = block(Utils.id("obsidian_barrel"));
    public static final BarrelBlock NETHERITE_BARREL = block(Utils.id("netherite_barrel"));

    public static final MiniStorageBlock VANILLA_WOOD_MINI_CHEST = block(Utils.id("vanilla_wood_mini_chest"));
    public static final MiniStorageBlock WOOD_MINI_CHEST = block(Utils.id("wood_mini_chest"));
    public static final MiniStorageBlock PUMPKIN_MINI_CHEST = block(Utils.id("pumpkin_mini_chest"));
    public static final MiniStorageBlock RED_MINI_PRESENT = block(Utils.id("red_mini_present"));
    public static final MiniStorageBlock WHITE_MINI_PRESENT = block(Utils.id("white_mini_present"));
    public static final MiniStorageBlock CANDY_CANE_MINI_PRESENT = block(Utils.id("candy_cane_mini_present"));
    public static final MiniStorageBlock GREEN_MINI_PRESENT = block(Utils.id("green_mini_present"));
    public static final MiniStorageBlock LAVENDER_MINI_PRESENT = block(Utils.id("lavender_mini_present"));
    public static final MiniStorageBlock PINK_AMETHYST_MINI_PRESENT = block(Utils.id("pink_amethyst_mini_present"));
    public static final MiniStorageBlock IRON_MINI_CHEST = block(Utils.id("iron_mini_chest"));
    public static final MiniStorageBlock GOLD_MINI_CHEST = block(Utils.id("gold_mini_chest"));
    public static final MiniStorageBlock DIAMOND_MINI_CHEST = block(Utils.id("diamond_mini_chest"));
    public static final MiniStorageBlock OBSIDIAN_MINI_CHEST = block(Utils.id("obsidian_mini_chest"));
    public static final MiniStorageBlock NETHERITE_MINI_CHEST = block(Utils.id("netherite_mini_chest"));
    public static final MiniStorageBlock MINI_BARREL = block(Utils.id("mini_barrel"));
    public static final CopperMiniStorageBlock COPPER_MINI_BARREL = block(Utils.id("copper_mini_barrel"));
    public static final CopperMiniStorageBlock EXPOSED_COPPER_MINI_BARREL = block(Utils.id("exposed_copper_mini_barrel"));
    public static final CopperMiniStorageBlock WEATHERED_COPPER_MINI_BARREL = block(Utils.id("weathered_copper_mini_barrel"));
    public static final CopperMiniStorageBlock OXIDIZED_COPPER_MINI_BARREL = block(Utils.id("oxidized_copper_mini_barrel"));
    public static final MiniStorageBlock WAXED_COPPER_MINI_BARREL = block(Utils.id("waxed_copper_mini_barrel"));
    public static final MiniStorageBlock WAXED_EXPOSED_COPPER_MINI_BARREL = block(Utils.id("waxed_exposed_copper_mini_barrel"));
    public static final MiniStorageBlock WAXED_WEATHERED_COPPER_MINI_BARREL = block(Utils.id("waxed_weathered_copper_mini_barrel"));
    public static final MiniStorageBlock WAXED_OXIDIZED_COPPER_MINI_BARREL = block(Utils.id("waxed_oxidized_copper_mini_barrel"));
    public static final MiniStorageBlock IRON_MINI_BARREL = block(Utils.id("iron_mini_barrel"));
    public static final MiniStorageBlock GOLD_MINI_BARREL = block(Utils.id("gold_mini_barrel"));
    public static final MiniStorageBlock DIAMOND_MINI_BARREL = block(Utils.id("diamond_mini_barrel"));
    public static final MiniStorageBlock OBSIDIAN_MINI_BARREL = block(Utils.id("obsidian_mini_barrel"));
    public static final MiniStorageBlock NETHERITE_MINI_BARREL = block(Utils.id("netherite_mini_barrel"));

    private static <T extends Block> T block(ResourceLocation id) {
        //noinspection unchecked
        return (T) BuiltInRegistries.BLOCK.get(id);
    }

    public static List<OpenableBlock> all() {
        return Arrays.stream(ModBlocks.class.getFields())
                     .filter(it -> OpenableBlock.class.isAssignableFrom(it.getType()))
                     .map(it -> {
                         try {
                             return (OpenableBlock) it.get(null);
                         } catch (IllegalAccessException e) {
                             return null;
                         }
                     })
                     .filter(Objects::nonNull)
                     .toList();
    }
}
