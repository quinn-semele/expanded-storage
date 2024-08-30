package dev.compasses.expandedstorage.registration;

import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.entity.BarrelBlockEntity;
import dev.compasses.expandedstorage.block.entity.ChestBlockEntity;
import dev.compasses.expandedstorage.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<ChestBlockEntity> CHEST = register("chest", BlockEntityType.Builder.of(ChestBlockEntity::new, ModBlocks.WOODEN_CHEST).build(null));
    public static final BlockEntityType<BarrelBlockEntity> BARREL = register("barrel", BlockEntityType.Builder.of(BarrelBlockEntity::new, ModBlocks.WOODEN_BARREL).build(null));
    public static final BlockEntityType<ShulkerBoxBlockEntity> SHULKER_BOX = register("shulker_box", BlockEntityType.Builder.of(ShulkerBoxBlockEntity::new, ModBlocks.SHULKER_BOX).build(null));

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Utils.id(name), type);
    }

    public static void registerContent() {
        // NO-OP - Registration is done by class loading.
    }
}
