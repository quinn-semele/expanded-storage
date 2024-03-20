package compasses.expandedstorage.impl.client.compat.carrier;

import compasses.expandedstorage.impl.block.ChestBlock;
import compasses.expandedstorage.impl.block.OpenableBlock;
import me.steven.carrier.api.CarriableRegistry;

public final class CarrierCompat {
    public static void registerChestBlock(ChestBlock block) {
        CarriableRegistry.INSTANCE.register(block.getBlockId(), new CarriableChest(block.getBlockId(), block));
    }

    public static void registerOpenableBlock(OpenableBlock block) {
        CarriableRegistry.INSTANCE.register(block.getBlockId(), new CarriableOldChest(block.getBlockId(), block));
    }
}
