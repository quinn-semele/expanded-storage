package compasses.expandedstorage.impl.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public final class BlockLootProvider extends FabricBlockLootTableProvider {
    public BlockLootProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    public void generate() {
        BlockLootTableHelper.registerLootTables(this::add, this::createNameableBlockEntityTable);
    }
}
