package compasses.expandedstorage.impl.registration;

import compasses.expandedstorage.impl.block.AbstractChestBlock;
import compasses.expandedstorage.impl.block.BarrelBlock;
import compasses.expandedstorage.impl.block.ChestBlock;
import compasses.expandedstorage.impl.block.MiniStorageBlock;
import compasses.expandedstorage.impl.block.OpenableBlock;
import compasses.expandedstorage.impl.block.entity.BarrelBlockEntity;
import compasses.expandedstorage.impl.block.entity.ChestBlockEntity;
import compasses.expandedstorage.impl.block.entity.MiniStorageBlockEntity;
import compasses.expandedstorage.impl.block.entity.OldChestBlockEntity;
import compasses.expandedstorage.impl.entity.ChestMinecart;
import compasses.expandedstorage.impl.item.ChestMinecartItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Content {
    private final List<ResourceLocation> stats;

    private final List<NamedValue<ChestBlock>> chestBlocks;
    private final List<NamedValue<BlockItem>> chestItems;
    private final List<NamedValue<EntityType<ChestMinecart>>> chestMinecartEntityTypes;
    private final NamedValue<BlockEntityType<ChestBlockEntity>> chestBlockEntityType;

    private final List<NamedValue<AbstractChestBlock>> oldChestBlocks;
    private final NamedValue<BlockEntityType<OldChestBlockEntity>> oldChestBlockEntityType;

    private final List<NamedValue<BarrelBlock>> barrelBlocks;
    private final NamedValue<BlockEntityType<BarrelBlockEntity>> barrelBlockEntityType;

    private final NamedValue<BlockEntityType<MiniStorageBlockEntity>> miniChestBlockEntityType;

    private final List<NamedValue<? extends OpenableBlock>> blocks;
    private final List<NamedValue<? extends Item>> items;
    private final List<NamedValue<? extends EntityType<? extends Entity>>> entityTypes;
    private final List<Map.Entry<NamedValue<ChestMinecartItem>, NamedValue<EntityType<ChestMinecart>>>> chestMinecartAndTypes;

    public Content(
            List<ResourceLocation> stats,
            List<NamedValue<Item>> baseItems,

            List<NamedValue<ChestBlock>> chestBlocks,
            List<NamedValue<BlockItem>> chestItems,
            List<NamedValue<EntityType<ChestMinecart>>> chestMinecartEntityTypes,
            List<NamedValue<ChestMinecartItem>> chestMinecartItems,
            NamedValue<BlockEntityType<ChestBlockEntity>> chestBlockEntityType,

            List<NamedValue<AbstractChestBlock>> oldChestBlocks,
            List<NamedValue<BlockItem>> oldChestItems,
            NamedValue<BlockEntityType<OldChestBlockEntity>> oldChestBlockEntityType,

            List<NamedValue<BarrelBlock>> barrelBlocks,
            List<NamedValue<BlockItem>> barrelItems,
            NamedValue<BlockEntityType<BarrelBlockEntity>> barrelBlockEntityType,

            List<NamedValue<MiniStorageBlock>> miniStorageBlocks,
            List<NamedValue<BlockItem>> miniChestItems,
            NamedValue<BlockEntityType<MiniStorageBlockEntity>> miniChestBlockEntityType
    ) {
        this.stats = stats;

        this.chestBlocks = chestBlocks;
        this.chestItems = chestItems;
        this.chestMinecartEntityTypes = chestMinecartEntityTypes;
        this.chestBlockEntityType = chestBlockEntityType;

        this.oldChestBlocks = oldChestBlocks;
        this.oldChestBlockEntityType = oldChestBlockEntityType;

        this.barrelBlocks = barrelBlocks;
        this.barrelBlockEntityType = barrelBlockEntityType;

        this.miniChestBlockEntityType = miniChestBlockEntityType;

        this.blocks = new ArrayList<>();
        blocks.addAll(chestBlocks);
        blocks.addAll(oldChestBlocks);
        blocks.addAll(barrelBlocks);
        blocks.addAll(miniStorageBlocks);

        this.items = new ArrayList<>();
        items.addAll(baseItems);
        items.addAll(chestItems);
        items.addAll(chestMinecartItems);
        items.addAll(oldChestItems);
        items.addAll(barrelItems);
        items.addAll(miniChestItems);

        this.entityTypes = new ArrayList<>();
        entityTypes.addAll(chestMinecartEntityTypes);

        Map<ResourceLocation, NamedValue<EntityType<ChestMinecart>>> chestMinecartEntityTypesLookup = chestMinecartEntityTypes.stream().map(it -> Map.entry(it.getName(), it)).collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
        this.chestMinecartAndTypes = chestMinecartItems.stream().map((value) -> Map.entry(value, chestMinecartEntityTypesLookup.get(value.getName()))).collect(Collectors.toList());
    }

    public List<ResourceLocation> getStats() {
        return stats;
    }

    // todo: unused?
    public List<NamedValue<ChestBlock>> getChestBlocks() {
        return chestBlocks;
    }

    public List<NamedValue<BlockItem>> getChestItems() {
        return chestItems;
    }

    public List<NamedValue<EntityType<ChestMinecart>>> getChestMinecartEntityTypes() {
        return chestMinecartEntityTypes;
    }

    public NamedValue<BlockEntityType<ChestBlockEntity>> getChestBlockEntityType() {
        return chestBlockEntityType;
    }

    // todo: unused?
    public List<NamedValue<AbstractChestBlock>> getOldChestBlocks() {
        return oldChestBlocks;
    }

    public NamedValue<BlockEntityType<OldChestBlockEntity>> getOldChestBlockEntityType() {
        return oldChestBlockEntityType;
    }

    public List<NamedValue<BarrelBlock>> getBarrelBlocks() {
        return barrelBlocks;
    }

    public NamedValue<BlockEntityType<BarrelBlockEntity>> getBarrelBlockEntityType() {
        return barrelBlockEntityType;
    }

    public NamedValue<BlockEntityType<MiniStorageBlockEntity>> getMiniChestBlockEntityType() {
        return miniChestBlockEntityType;
    }

    public List<NamedValue<? extends OpenableBlock>> getBlocks() {
        return blocks;
    }

    public List<NamedValue<? extends Item>> getItems() {
        return items;
    }

    public List<NamedValue<? extends EntityType<?>>> getEntityTypes() {
        return entityTypes;
    }

    public List<Map.Entry<NamedValue<ChestMinecartItem>, NamedValue<EntityType<ChestMinecart>>>> getChestMinecartAndTypes() {
        return chestMinecartAndTypes;
    }
}
