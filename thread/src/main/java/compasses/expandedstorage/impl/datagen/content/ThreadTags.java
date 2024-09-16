package compasses.expandedstorage.impl.datagen.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public final class ThreadTags {
    private static ResourceLocation commonId(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }

    public static class Items {
        public static final TagKey<Item> IRON_NUGGETS = tag(commonId("nuggets/iron"));
        public static final TagKey<Item> BAMBOO = tag(commonId("bamboos"));
        public static final TagKey<Item> BAMBOO_OLD = tag(commonId("bamboo"));

        private static TagKey<Item> tag(ResourceLocation id) {
            return TagKey.create(Registries.ITEM, id);
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> WOODEN_CHEST_MINECARTS = tag(commonId("chest_minecarts/wooden"));

        private static TagKey<EntityType<?>> tag(ResourceLocation id) {
            return TagKey.create(Registries.ENTITY_TYPE, id);
        }
    }
}
