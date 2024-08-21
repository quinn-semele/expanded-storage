package dev.compasses.expandedstorage.registration;

import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.BarrelBlock;
import dev.compasses.expandedstorage.block.ChestBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final ChestBlock WOODEN_CHEST = register("wooden_chest", new ChestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));

    public static final BarrelBlock WOODEN_BARREL = register("wooden_barrel", new BarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(BuiltInRegistries.BLOCK, Utils.id(name), block);
    }

    public static void registerContent() {
        // NO-OP - Registration is done by class loading.
    }
}
