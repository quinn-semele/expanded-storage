package dev.compasses.expandedstorage.registration;

import dev.compasses.expandedstorage.Utils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final Item WOODEN_CHEST = register("wooden_chest", new BlockItem(ModBlocks.WOODEN_CHEST, new Item.Properties()));

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(BuiltInRegistries.ITEM, Utils.id(name), item);
    }

    public static void registerContent() {
        // NO-OP - Registration is done by class loading.
    }
}
