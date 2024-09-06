package dev.compasses.expandedstorage.registration;

import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.BarrelBlock;
import dev.compasses.expandedstorage.block.ChestBlock;
import dev.compasses.expandedstorage.block.ShulkerBoxBlock;
import dev.compasses.expandedstorage.block.misc.BlockColor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import java.util.Arrays;

public class ModBlocks {
    public static final ChestBlock OAK_CHEST = register("oak_chest", new ChestBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final ChestBlock SPRUCE_CHEST = register("spruce_chest", new ChestBlock(Properties.ofFullCopy(Blocks.SPRUCE_PLANKS)));
    public static final ChestBlock COPPER_CHEST = register("copper_chest", new ChestBlock(Properties.ofFullCopy(Blocks.COPPER_BLOCK)));
    public static final ChestBlock EXPOSED_COPPER_CHEST = register("exposed_copper_chest", new ChestBlock(Properties.ofFullCopy(Blocks.EXPOSED_COPPER)));
    public static final ChestBlock WEATHERED_COPPER_CHEST = register("weathered_copper_chest", new ChestBlock(Properties.ofFullCopy(Blocks.WEATHERED_COPPER)));
    public static final ChestBlock OXIDIZED_COPPER_CHEST = register("oxidized_copper_chest", new ChestBlock(Properties.ofFullCopy(Blocks.OXIDIZED_COPPER)));
    public static final ChestBlock IRON_CHEST = register("iron_chest", new ChestBlock(Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final ChestBlock GOLDEN_CHEST = register("golden_chest", new ChestBlock(Properties.ofFullCopy(Blocks.GOLD_BLOCK)));
    public static final ChestBlock DIAMOND_CHEST = register("diamond_chest", new ChestBlock(Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)));
    public static final ChestBlock OBSIDIAN_CHEST = register("obsidian_chest", new ChestBlock(Properties.ofFullCopy(Blocks.OBSIDIAN)));
    public static final ChestBlock NETHERITE_CHEST = register("netherite_chest", new ChestBlock(Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)));

    public static final ChestBlock[] CHESTS = {
            OAK_CHEST,
            SPRUCE_CHEST,
            COPPER_CHEST,
            EXPOSED_COPPER_CHEST,
            WEATHERED_COPPER_CHEST,
            OXIDIZED_COPPER_CHEST,
            IRON_CHEST,
            GOLDEN_CHEST,
            DIAMOND_CHEST,
            OBSIDIAN_CHEST,
            NETHERITE_CHEST
    };

    public static final BarrelBlock COMMON_BARREL = register("barrel", new BarrelBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final BarrelBlock COPPER_BARREL = register("copper_barrel", new BarrelBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final BarrelBlock EXPOSED_COPPER_BARREL = register("exposed_copper_barrel", new BarrelBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final BarrelBlock WEATHERED_COPPER_BARREL = register("weathered_copper_barrel", new BarrelBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final BarrelBlock OXIDIZED_COPPER_BARREL = register("oxidized_copper_barrel", new BarrelBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final BarrelBlock IRON_BARREL = register("iron_barrel", new BarrelBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final BarrelBlock GOLDEN_BARREL = register("golden_barrel", new BarrelBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final BarrelBlock DIAMOND_BARREL = register("diamond_barrel", new BarrelBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final BarrelBlock OBSIDIAN_BARREL = register("obsidian_barrel", new BarrelBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final BarrelBlock NETHERITE_BARREL = register("netherite_barrel", new BarrelBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)));

    public static final BarrelBlock[] BARRELS = {
            COMMON_BARREL,
            COPPER_BARREL,
            EXPOSED_COPPER_BARREL,
            WEATHERED_COPPER_BARREL,
            OXIDIZED_COPPER_BARREL,
            IRON_BARREL,
            GOLDEN_BARREL,
            DIAMOND_BARREL,
            OBSIDIAN_BARREL,
            NETHERITE_BARREL
    };

    public static final ShulkerBoxBlock[] SHULKER_BOXES = Arrays.stream(BlockColor.values())
            .map(color -> register(color.prefix() + "shulker_box", new ShulkerBoxBlock(Properties.ofFullCopy(Blocks.SHULKER_BOX), color)))
            .toArray(ShulkerBoxBlock[]::new);

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(BuiltInRegistries.BLOCK, Utils.id(name), block);
    }

    public static void registerContent() {
        // NO-OP - Registration is done by class loading.
    }
}
