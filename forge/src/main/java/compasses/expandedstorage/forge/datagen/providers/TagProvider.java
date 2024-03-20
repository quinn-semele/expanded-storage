package compasses.expandedstorage.forge.datagen.providers;

import compasses.expandedstorage.impl.datagen.content.ModTags;
import compasses.expandedstorage.impl.datagen.providers.TagHelper;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.registration.ModBlocks;
import compasses.expandedstorage.impl.registration.ModItems;
import compasses.expandedstorage.forge.datagen.content.ForgeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class TagProvider {
    public static final class Block extends BlockTagsProvider {
        public Block(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Utils.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            TagHelper.registerBlockTags(this::tag);
            this.tag(Tags.Blocks.CHESTS_WOODEN).add(ModBlocks.WOOD_CHEST);
            this.tag(ModTags.Blocks.ES_WOODEN_CHESTS)
                .addTag(Tags.Blocks.CHESTS_WOODEN);
        }

        @Override
        public String getName() {
            return "Expanded Storage - Block Tags";
        }
    }

    public static final class Item extends ItemTagsProvider {
        public Item(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, blockTagsProvider.contentsGetter(), Utils.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            TagHelper.registerItemTags(this::tag);
            this.tag(Tags.Items.CHESTS_WOODEN).add(ModItems.WOOD_CHEST);
            this.tag(ModTags.Items.ES_WOODEN_CHESTS)
                .addTag(Tags.Items.CHESTS_WOODEN);
            this.tag(ForgeTags.Items.BAMBOO)
                .add(Items.BAMBOO);
        }

        @Override
        public String getName() {
            return "Expanded Storage - Item Tags";
        }
    }

    public static final class EntityType extends EntityTypeTagsProvider {
        public EntityType(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Utils.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            TagHelper.registerEntityTypeTags(this::tag);
        }

        @Override
        public String getName() {
            return "Expanded Storage - Entity Type Tags";
        }
    }
}
