package compasses.expandedstorage.impl.datagen.content;

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
        public static final TagKey<Item> WOODEN_CHESTS = tag(commonId("wooden_chests"));
        public static final TagKey<Item> WOODEN_BARRELS = tag(commonId("wooden_barrels"));
        public static final TagKey<Item> GLASS_BLOCKS = tag(commonId("glass"));
        public static final TagKey<Item> DIAMONDS = tag(commonId("diamonds"));
        public static final TagKey<Item> COPPER_INGOTS = tag(commonId("copper_ingots"));
        public static final TagKey<Item> IRON_NUGGETS = tag(commonId("iron_nuggets"));
        public static final TagKey<Item> IRON_INGOTS = tag(commonId("iron_ingots"));
        public static final TagKey<Item> GOLD_INGOTS = tag(commonId("gold_ingots"));
        public static final TagKey<Item> NETHERITE_INGOTS = tag(commonId("netherite_ingots"));
        public static final TagKey<Item> RED_DYES = tag(commonId("red_dyes"));
        public static final TagKey<Item> WHITE_DYES = tag(commonId("white_dyes"));
        public static final TagKey<Item> OBSIDIAN = tag(commonId("obsidian"));
        public static final TagKey<Item> BAMBOO = tag(commonId("bamboo"));

        private static TagKey<Item> tag(ResourceLocation id) {
            return TagKey.create(Registries.ITEM, id);
        }
    }

    public static class Blocks {
        public static final TagKey<Block> WOODEN_CHESTS = tag(commonId("wooden_chests"));
        public static final TagKey<Block> WOODEN_BARRELS = tag(commonId("wooden_barrels"));

        private static TagKey<Block> tag(ResourceLocation id) {
            return TagKey.create(Registries.BLOCK, id);
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> WOODEN_CHEST_MINECARTS = tag(commonId("wooden_chest_minecarts"));

        private static TagKey<EntityType<?>> tag(ResourceLocation id) {
            return TagKey.create(Registries.ENTITY_TYPE, id);
        }
    }
}
