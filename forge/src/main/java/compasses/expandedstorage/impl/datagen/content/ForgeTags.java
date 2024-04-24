package compasses.expandedstorage.impl.datagen.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public final class ForgeTags {
    public static class Items {
        public static final TagKey<Item> BAMBOO = tag(new ResourceLocation("c", "bamboos"));

        private static TagKey<Item> tag(ResourceLocation id) {
            return TagKey.create(Registries.ITEM, id);
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> WOODEN_CHEST_MINECARTS = tag(new ResourceLocation("c", "chest_minecarts/wooden"));

        private static TagKey<EntityType<?>> tag(ResourceLocation id) {
            return TagKey.create(Registries.ENTITY_TYPE, id);
        }
    }
}
