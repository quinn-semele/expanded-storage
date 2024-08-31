package dev.compasses.expandedstorage.registration;

import dev.compasses.expandedstorage.Utils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Arrays;

public class ModItems {
    public static final Item WOODEN_CHEST = register("wooden_chest", new BlockItem(ModBlocks.WOODEN_CHEST, new Item.Properties()));

    public static final Item WOODEN_BARREL = register("wooden_barrel", new BlockItem(ModBlocks.WOODEN_BARREL, new Item.Properties()));

    public static final Item[] SHULKER_BOXES = Arrays.stream(ModBlocks.SHULKER_BOXES)
            .map(block -> register(block.color().prefix() + "shulker_box", new BlockItem(block, new Item.Properties())))
            .toArray(Item[]::new);

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(BuiltInRegistries.ITEM, Utils.id(name), item);
    }

    public static void registerContent() {
        // NO-OP - Registration is done by class loading.
    }

    private static void addContentToCreativeTab(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.accept(WOODEN_CHEST);
        output.accept(WOODEN_BARREL);
        Arrays.stream(SHULKER_BOXES).forEach(output::accept);
    }

    public static void registerCreativeTab(CreativeModeTab.Builder builder) {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Utils.id("tab"), builder
                .icon(() -> ModItems.SHULKER_BOXES[0].getDefaultInstance())
                .title(Component.translatable("itemGroup.expandedstorage.tab"))
                .displayItems(ModItems::addContentToCreativeTab)
                .build());
    }
}
