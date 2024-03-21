package compasses.expandedstorage.impl.compat.carrier;

import compasses.expandedstorage.impl.block.ChestBlock;
import compasses.expandedstorage.impl.block.OpenableBlock;
import me.steven.carrier.api.CarriableRegistry;
import net.minecraft.resources.ResourceLocation;

public final class CarrierCompat {
    public static void registerChestBlock(ChestBlock block) {
        CarriableRegistry.INSTANCE.register(block.getBlockId(), new CarriableChest(block.getBlockId(), block));
    }

    public static void registerOpenableBlock(OpenableBlock block) {
        CarriableRegistry.INSTANCE.register(block.getBlockId(), new CarriableOldChest(block.getBlockId(), block));
    }

    public static void removeEntry(OpenableBlock block) {
        ((AccessibleCarrierRegistry) CarriableRegistry.INSTANCE).expandedstorage$removeEntry(new ResourceLocation("carrier", block.getBlockId().getNamespace() + "_" + block.getBlockId().getPath()));
    }
}