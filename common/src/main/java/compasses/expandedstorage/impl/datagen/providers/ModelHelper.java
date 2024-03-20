package compasses.expandedstorage.impl.datagen.providers;

import compasses.expandedstorage.impl.registration.ModItems;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class ModelHelper {
    public static void registerItemModels(Consumer<Item> consumer) {
        consumer.accept(ModItems.WOOD_TO_COPPER_CONVERSION_KIT);
        consumer.accept(ModItems.WOOD_TO_IRON_CONVERSION_KIT);
        consumer.accept(ModItems.WOOD_TO_GOLD_CONVERSION_KIT);
        consumer.accept(ModItems.WOOD_TO_DIAMOND_CONVERSION_KIT);
        consumer.accept(ModItems.WOOD_TO_OBSIDIAN_CONVERSION_KIT);
        consumer.accept(ModItems.WOOD_TO_NETHERITE_CONVERSION_KIT);
        consumer.accept(ModItems.COPPER_TO_IRON_CONVERSION_KIT);
        consumer.accept(ModItems.COPPER_TO_GOLD_CONVERSION_KIT);
        consumer.accept(ModItems.COPPER_TO_DIAMOND_CONVERSION_KIT);
        consumer.accept(ModItems.COPPER_TO_OBSIDIAN_CONVERSION_KIT);
        consumer.accept(ModItems.COPPER_TO_NETHERITE_CONVERSION_KIT);
        consumer.accept(ModItems.IRON_TO_GOLD_CONVERSION_KIT);
        consumer.accept(ModItems.IRON_TO_DIAMOND_CONVERSION_KIT);
        consumer.accept(ModItems.IRON_TO_OBSIDIAN_CONVERSION_KIT);
        consumer.accept(ModItems.IRON_TO_NETHERITE_CONVERSION_KIT);
        consumer.accept(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT);
        consumer.accept(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT);
        consumer.accept(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT);
        consumer.accept(ModItems.DIAMOND_TO_OBSIDIAN_CONVERSION_KIT);
        consumer.accept(ModItems.DIAMOND_TO_NETHERITE_CONVERSION_KIT);
        consumer.accept(ModItems.OBSIDIAN_TO_NETHERITE_CONVERSION_KIT);
    }
}
