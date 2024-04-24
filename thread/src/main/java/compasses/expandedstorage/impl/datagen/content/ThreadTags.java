package compasses.expandedstorage.impl.datagen.content;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ThreadTags {
    private static ResourceLocation commonId(String path) {
        return new ResourceLocation("c", path);
    }

    public static class Items {
        public static final TagKey<Item> WOODEN_CHESTS = ConventionalItemTags.WOODEN_CHESTS;
        public static final TagKey<Item> WOODEN_BARRELS = ConventionalItemTags.WOODEN_BARRELS;
        public static final TagKey<Item> GLASS_BLOCKS = ConventionalItemTags.GLASS_BLOCKS;
        public static final TagKey<Item> DIAMONDS = ConventionalItemTags.DIAMOND_GEMS;
        public static final TagKey<Item> COPPER_INGOTS = ConventionalItemTags.COPPER_INGOTS;
        public static final TagKey<Item> IRON_NUGGETS = tag(commonId("nuggets/iron"));
        public static final TagKey<Item> IRON_INGOTS = ConventionalItemTags.IRON_INGOTS;
        public static final TagKey<Item> GOLD_INGOTS = ConventionalItemTags.GOLD_INGOTS;
        public static final TagKey<Item> NETHERITE_INGOTS = ConventionalItemTags.NETHERITE_INGOTS;
        public static final TagKey<Item> RED_DYES = ConventionalItemTags.RED_DYES;
        public static final TagKey<Item> WHITE_DYES = ConventionalItemTags.WHITE_DYES;
        public static final TagKey<Item> OBSIDIAN = tag(commonId("obsidians"));
        public static final TagKey<Item> BAMBOO = tag(commonId("bamboos"));
        public static final TagKey<Item> BAMBOO_OLD = tag(commonId("bamboo"));

        private static TagKey<Item> tag(ResourceLocation id) {
            return TagKey.create(Registries.ITEM, id);
        }
    }

    public static class Blocks {
        public static final TagKey<Block> WOODEN_CHESTS = ConventionalBlockTags.WOODEN_CHESTS;
        public static final TagKey<Block> WOODEN_BARRELS = ConventionalBlockTags.WOODEN_BARRELS;
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> WOODEN_CHEST_MINECARTS = tag(commonId("chest_minecarts/wooden"));

        private static TagKey<EntityType<?>> tag(ResourceLocation id) {
            return TagKey.create(Registries.ENTITY_TYPE, id);
        }
    }
}
