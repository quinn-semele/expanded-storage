package compasses.expandedstorage.forge.datagen.content;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public final class ForgeTags {
    public static class Items {
        public static final TagKey<Item> BAMBOO = tag(new ResourceLocation("forge", "bamboo"));

        private static TagKey<Item> tag(ResourceLocation id) {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), id);
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> WOODEN_CHEST_MINECARTS = tag(new ResourceLocation("forge", "chest_minecarts/wooden"));

        private static TagKey<EntityType<?>> tag(ResourceLocation id) {
            return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), id);
        }
    }
}
