package compasses.expandedstorage.impl.datagen.providers;

import compasses.expandedstorage.impl.datagen.content.ModEntityTypes;
import compasses.expandedstorage.impl.datagen.content.ModTags;
import compasses.expandedstorage.impl.registration.ModItems;
import compasses.expandedstorage.impl.datagen.content.ThreadTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class TagProvider {
    public static final class Block extends FabricTagProvider.BlockTagProvider {
        public Block(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> providerFuture) {
            super(output, providerFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            TagHelper.registerBlockTags(this::getOrCreateTagBuilder);
            this.getOrCreateTagBuilder(ModTags.Blocks.ES_WOODEN_CHESTS)
                .addOptionalTag(ConventionalBlockTags.WOODEN_CHESTS);
        }

        @NotNull
        @Override
        public String getName() {
            return "Expanded Storage - Block Tags";
        }
    }

    public static final class Item extends FabricTagProvider.ItemTagProvider {
        public Item(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> providerFuture) {
            super(output, providerFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            TagHelper.registerItemTags(this::getOrCreateTagBuilder);
            this.getOrCreateTagBuilder(ConventionalItemTags.WOODEN_CHESTS)
                    .add(ModItems.WOOD_CHEST);
            this.getOrCreateTagBuilder(ModTags.Items.ES_WOODEN_CHESTS)
                .addOptionalTag(ConventionalItemTags.WOODEN_CHESTS);
            this.getOrCreateTagBuilder(ThreadTags.Items.IRON_NUGGETS)
                .add(Items.IRON_NUGGET);
            this.getOrCreateTagBuilder(ThreadTags.Items.OBSIDIAN)
                .add(Items.OBSIDIAN);
            this.getOrCreateTagBuilder(ThreadTags.Items.BAMBOO)
                .add(Items.BAMBOO)
                .addOptionalTag(ThreadTags.Items.BAMBOO_OLD);
        }

        @NotNull
        @Override
        public String getName() {
            return "Expanded Storage - Item Tags";
        }
    }

    public static final class EntityTypes extends FabricTagProvider.EntityTypeTagProvider {
        public EntityTypes(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> providerFuture) {
            super(output, providerFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            this.getOrCreateTagBuilder(ThreadTags.Entities.WOODEN_CHEST_MINECARTS)
                .add(EntityType.CHEST_MINECART);

            this.getOrCreateTagBuilder(ModTags.Entities.ES_WOODEN_CHEST_MINECARTS)
                .addTag(ThreadTags.Entities.WOODEN_CHEST_MINECARTS)
                .add(ModEntityTypes.WOOD_CHEST_MINECART)
                .add(ModEntityTypes.PUMPKIN_CHEST_MINECART)
                .add(ModEntityTypes.PRESENT_MINECART)
                .add(ModEntityTypes.BAMBOO_CHEST_MINECART)
                .add(ModEntityTypes.MOSS_CHEST_MINECART);

            TagHelper.registerEntityTypeTags(this::getOrCreateTagBuilder);
        }

        @NotNull
        @Override
        public String getName() {
            return "Expanded Storage - Entity Type Tags";
        }
    }
}
