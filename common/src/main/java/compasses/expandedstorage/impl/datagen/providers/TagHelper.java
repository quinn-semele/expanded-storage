package compasses.expandedstorage.impl.datagen.providers;

import compasses.expandedstorage.impl.datagen.content.ModEntityTypes;
import compasses.expandedstorage.impl.datagen.content.ModTags;
import compasses.expandedstorage.impl.registration.ModBlocks;
import compasses.expandedstorage.impl.registration.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class TagHelper {

    private static <T> ResourceKey<T> resourceKeyOf(Registry<T> registry, T thing) {
        return registry.getResourceKey(thing).orElseThrow();
    }

    private static ResourceKey<Block> block(Block block) {
        return resourceKeyOf(BuiltInRegistries.BLOCK, block);
    }

    private static ResourceKey<Item> item(Item item) {
        return resourceKeyOf(BuiltInRegistries.ITEM, item);
    }

    private static ResourceKey<EntityType<?>> entityType(EntityType<?> entityType) {
        return resourceKeyOf(BuiltInRegistries.ENTITY_TYPE, entityType);
    }

    public static void registerBlockTags(Function<TagKey<Block>, TagsProvider.TagAppender<Block>> tagMaker) {
        tagMaker.apply(BlockTags.MINEABLE_WITH_AXE)
                .add(block(ModBlocks.COPPER_BARREL))
                .add(block(ModBlocks.EXPOSED_COPPER_BARREL))
                .add(block(ModBlocks.WEATHERED_COPPER_BARREL))
                .add(block(ModBlocks.OXIDIZED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_EXPOSED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_WEATHERED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_OXIDIZED_COPPER_BARREL))
                .add(block(ModBlocks.IRON_BARREL))
                .add(block(ModBlocks.GOLD_BARREL))
                .add(block(ModBlocks.DIAMOND_BARREL))
                .add(block(ModBlocks.OBSIDIAN_BARREL))
                .add(block(ModBlocks.NETHERITE_BARREL))
                .add(block(ModBlocks.WOOD_CHEST))
                .add(block(ModBlocks.PUMPKIN_CHEST))
                .add(block(ModBlocks.PRESENT))
                .add(block(ModBlocks.OLD_WOOD_CHEST))
                .add(block(ModBlocks.VANILLA_WOOD_MINI_CHEST))
                .add(block(ModBlocks.WOOD_MINI_CHEST))
                .add(block(ModBlocks.PUMPKIN_MINI_CHEST))
                .add(block(ModBlocks.RED_MINI_PRESENT))
                .add(block(ModBlocks.WHITE_MINI_PRESENT))
                .add(block(ModBlocks.CANDY_CANE_MINI_PRESENT))
                .add(block(ModBlocks.GREEN_MINI_PRESENT))
                .add(block(ModBlocks.LAVENDER_MINI_PRESENT))
                .add(block(ModBlocks.PINK_AMETHYST_MINI_PRESENT))
                .add(block(ModBlocks.MINI_BARREL))
                .add(block(ModBlocks.COPPER_MINI_BARREL))
                .add(block(ModBlocks.EXPOSED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WEATHERED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.OXIDIZED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WAXED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WAXED_EXPOSED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WAXED_WEATHERED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WAXED_OXIDIZED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.IRON_MINI_BARREL))
                .add(block(ModBlocks.GOLD_MINI_BARREL))
                .add(block(ModBlocks.DIAMOND_MINI_BARREL))
                .add(block(ModBlocks.OBSIDIAN_MINI_BARREL))
                .add(block(ModBlocks.NETHERITE_MINI_BARREL));
        tagMaker.apply(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(block(ModBlocks.IRON_CHEST))
                .add(block(ModBlocks.GOLD_CHEST))
                .add(block(ModBlocks.DIAMOND_CHEST))
                .add(block(ModBlocks.OBSIDIAN_CHEST))
                .add(block(ModBlocks.NETHERITE_CHEST))
                .add(block(ModBlocks.OLD_IRON_CHEST))
                .add(block(ModBlocks.OLD_GOLD_CHEST))
                .add(block(ModBlocks.OLD_DIAMOND_CHEST))
                .add(block(ModBlocks.OLD_OBSIDIAN_CHEST))
                .add(block(ModBlocks.OLD_NETHERITE_CHEST))
                .add(block(ModBlocks.IRON_MINI_CHEST))
                .add(block(ModBlocks.GOLD_MINI_CHEST))
                .add(block(ModBlocks.DIAMOND_MINI_CHEST))
                .add(block(ModBlocks.OBSIDIAN_MINI_CHEST))
                .add(block(ModBlocks.NETHERITE_MINI_CHEST));
        tagMaker.apply(BlockTags.MINEABLE_WITH_HOE)
                .add(block(ModBlocks.MOSS_CHEST));
        tagMaker.apply(BlockTags.GUARDED_BY_PIGLINS)
                .add(block(ModBlocks.COPPER_BARREL))
                .add(block(ModBlocks.EXPOSED_COPPER_BARREL))
                .add(block(ModBlocks.WEATHERED_COPPER_BARREL))
                .add(block(ModBlocks.OXIDIZED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_EXPOSED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_WEATHERED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_OXIDIZED_COPPER_BARREL))
                .add(block(ModBlocks.IRON_BARREL))
                .add(block(ModBlocks.GOLD_BARREL))
                .add(block(ModBlocks.DIAMOND_BARREL))
                .add(block(ModBlocks.OBSIDIAN_BARREL))
                .add(block(ModBlocks.NETHERITE_BARREL))
                .add(block(ModBlocks.WOOD_CHEST))
                .add(block(ModBlocks.PUMPKIN_CHEST))
                .add(block(ModBlocks.PRESENT))
                .add(block(ModBlocks.BAMBOO_CHEST))
                .add(block(ModBlocks.MOSS_CHEST))
                .add(block(ModBlocks.IRON_CHEST))
                .add(block(ModBlocks.GOLD_CHEST))
                .add(block(ModBlocks.DIAMOND_CHEST))
                .add(block(ModBlocks.OBSIDIAN_CHEST))
                .add(block(ModBlocks.NETHERITE_CHEST))
                .add(block(ModBlocks.OLD_WOOD_CHEST))
                .add(block(ModBlocks.OLD_IRON_CHEST))
                .add(block(ModBlocks.OLD_GOLD_CHEST))
                .add(block(ModBlocks.OLD_DIAMOND_CHEST))
                .add(block(ModBlocks.OLD_OBSIDIAN_CHEST))
                .add(block(ModBlocks.OLD_NETHERITE_CHEST))
                .add(block(ModBlocks.VANILLA_WOOD_MINI_CHEST))
                .add(block(ModBlocks.WOOD_MINI_CHEST))
                .add(block(ModBlocks.PUMPKIN_MINI_CHEST))
                .add(block(ModBlocks.RED_MINI_PRESENT))
                .add(block(ModBlocks.WHITE_MINI_PRESENT))
                .add(block(ModBlocks.CANDY_CANE_MINI_PRESENT))
                .add(block(ModBlocks.GREEN_MINI_PRESENT))
                .add(block(ModBlocks.LAVENDER_MINI_PRESENT))
                .add(block(ModBlocks.PINK_AMETHYST_MINI_PRESENT))
                .add(block(ModBlocks.IRON_MINI_CHEST))
                .add(block(ModBlocks.GOLD_MINI_CHEST))
                .add(block(ModBlocks.DIAMOND_MINI_CHEST))
                .add(block(ModBlocks.OBSIDIAN_MINI_CHEST))
                .add(block(ModBlocks.NETHERITE_MINI_CHEST))
                .add(block(ModBlocks.MINI_BARREL))
                .add(block(ModBlocks.COPPER_MINI_BARREL))
                .add(block(ModBlocks.EXPOSED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WEATHERED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.OXIDIZED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WAXED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WAXED_EXPOSED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WAXED_WEATHERED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WAXED_OXIDIZED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.IRON_MINI_BARREL))
                .add(block(ModBlocks.GOLD_MINI_BARREL))
                .add(block(ModBlocks.DIAMOND_MINI_BARREL))
                .add(block(ModBlocks.OBSIDIAN_MINI_BARREL))
                .add(block(ModBlocks.NETHERITE_MINI_BARREL));
        tagMaker.apply(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(block(ModBlocks.OBSIDIAN_BARREL))
                .add(block(ModBlocks.NETHERITE_BARREL))
                .add(block(ModBlocks.OBSIDIAN_CHEST))
                .add(block(ModBlocks.NETHERITE_CHEST))
                .add(block(ModBlocks.OLD_OBSIDIAN_CHEST))
                .add(block(ModBlocks.OLD_NETHERITE_CHEST))
                .add(block(ModBlocks.OBSIDIAN_MINI_CHEST))
                .add(block(ModBlocks.NETHERITE_MINI_CHEST))
                .add(block(ModBlocks.OBSIDIAN_MINI_BARREL))
                .add(block(ModBlocks.NETHERITE_MINI_BARREL));
        tagMaker.apply(BlockTags.NEEDS_IRON_TOOL)
                .add(block(ModBlocks.GOLD_BARREL))
                .add(block(ModBlocks.DIAMOND_BARREL))
                .add(block(ModBlocks.GOLD_CHEST))
                .add(block(ModBlocks.DIAMOND_CHEST))
                .add(block(ModBlocks.OLD_GOLD_CHEST))
                .add(block(ModBlocks.OLD_DIAMOND_CHEST))
                .add(block(ModBlocks.GOLD_MINI_CHEST))
                .add(block(ModBlocks.DIAMOND_MINI_CHEST))
                .add(block(ModBlocks.GOLD_MINI_BARREL))
                .add(block(ModBlocks.DIAMOND_MINI_BARREL));
        tagMaker.apply(BlockTags.NEEDS_STONE_TOOL)
                .add(block(ModBlocks.COPPER_BARREL))
                .add(block(ModBlocks.EXPOSED_COPPER_BARREL))
                .add(block(ModBlocks.WEATHERED_COPPER_BARREL))
                .add(block(ModBlocks.OXIDIZED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_EXPOSED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_WEATHERED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_OXIDIZED_COPPER_BARREL))
                .add(block(ModBlocks.IRON_BARREL))
                .add(block(ModBlocks.IRON_CHEST))
                .add(block(ModBlocks.OLD_IRON_CHEST))
                .add(block(ModBlocks.IRON_MINI_CHEST))
                .add(block(ModBlocks.COPPER_MINI_BARREL))
                .add(block(ModBlocks.EXPOSED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WEATHERED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.OXIDIZED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WAXED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WAXED_EXPOSED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WAXED_WEATHERED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.WAXED_OXIDIZED_COPPER_MINI_BARREL))
                .add(block(ModBlocks.IRON_MINI_BARREL));
        tagMaker.apply(ModTags.Blocks.COPPER_BARRELS)
                .add(block(ModBlocks.COPPER_BARREL))
                .add(block(ModBlocks.EXPOSED_COPPER_BARREL))
                .add(block(ModBlocks.WEATHERED_COPPER_BARREL))
                .add(block(ModBlocks.OXIDIZED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_EXPOSED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_WEATHERED_COPPER_BARREL))
                .add(block(ModBlocks.WAXED_OXIDIZED_COPPER_BARREL));
        tagMaker.apply(ModTags.Blocks.ES_WOODEN_CHESTS)
                .add(block(ModBlocks.PUMPKIN_CHEST))
                .add(block(ModBlocks.PRESENT))
                .add(block(ModBlocks.BAMBOO_CHEST))
                .add(block(ModBlocks.MOSS_CHEST));
//        tagMaker.apply(ModTags.Blocks.COPPER_CHESTS)
//                .add(ModBlocks.COPPER_CHEST)
//                .add(ModBlocks.EXPOSED_COPPER_CHEST)
//                .add(ModBlocks.WEATHERED_COPPER_CHEST)
//                .add(ModBlocks.OXIDIZED_COPPER_CHEST)
//                .add(ModBlocks.WAXED_COPPER_CHEST)
//                .add(ModBlocks.WAXED_EXPOSED_COPPER_CHEST)
//                .add(ModBlocks.WAXED_WEATHERED_COPPER_CHEST)
//                .add(ModBlocks.WAXED_OXIDIZED_COPPER_CHEST);
//        tagMaker.apply(ModTags.Blocks.OLD_COPPER_CHESTS)
//                .add(ModBlocks.OLD_COPPER_CHEST)
//                .add(ModBlocks.OLD_EXPOSED_COPPER_CHEST)
//                .add(ModBlocks.OLD_WEATHERED_COPPER_CHEST)
//                .add(ModBlocks.OLD_OXIDIZED_COPPER_CHEST)
//                .add(ModBlocks.WAXED_OLD_COPPER_CHEST)
//                .add(ModBlocks.WAXED_OLD_EXPOSED_COPPER_CHEST)
//                .add(ModBlocks.WAXED_OLD_WEATHERED_COPPER_CHEST)
//                .add(ModBlocks.WAXED_OLD_OXIDIZED_COPPER_CHEST);
    }

    public static void registerItemTags(Function<TagKey<Item>, TagsProvider.TagAppender<Item>> tagMaker) {
        tagMaker.apply(ModTags.Items.ES_WOODEN_CHESTS)
                .add(item(ModItems.PUMPKIN_CHEST))
                .add(item(ModItems.PRESENT))
                .add(item(ModItems.BAMBOO_CHEST))
                .add(item(ModItems.MOSS_CHEST));
        tagMaker.apply(ItemTags.PIGLIN_LOVED)
                .add(item(ModItems.WOOD_TO_GOLD_CONVERSION_KIT))
                .add(item(ModItems.COPPER_TO_GOLD_CONVERSION_KIT))
                .add(item(ModItems.IRON_TO_GOLD_CONVERSION_KIT))
                .add(item(ModItems.GOLD_TO_DIAMOND_CONVERSION_KIT))
                .add(item(ModItems.GOLD_TO_OBSIDIAN_CONVERSION_KIT))
                .add(item(ModItems.GOLD_TO_NETHERITE_CONVERSION_KIT))
                .add(item(ModItems.GOLD_BARREL))
                .add(item(ModItems.GOLD_CHEST))
                .add(item(ModItems.GOLD_CHEST_MINECART))
                .add(item(ModItems.OLD_GOLD_CHEST))
                .add(item(ModItems.GOLD_MINI_CHEST))
                .add(item(ModItems.GOLD_MINI_BARREL));
    }

    public static void registerEntityTypeTags(Function<TagKey<EntityType<?>>, TagsProvider.TagAppender<EntityType<?>>> tagMaker) {
        tagMaker.apply(ModTags.Entities.ES_CHEST_MINECARTS)
                .addTag(ModTags.Entities.ES_WOODEN_CHEST_MINECARTS)
                .add(entityType(ModEntityTypes.IRON_CHEST_MINECART))
                .add(entityType(ModEntityTypes.GOLD_CHEST_MINECART))
                .add(entityType(ModEntityTypes.DIAMOND_CHEST_MINECART))
                .add(entityType(ModEntityTypes.OBSIDIAN_CHEST_MINECART))
                .add(entityType(ModEntityTypes.NETHERITE_CHEST_MINECART));
    }
}
