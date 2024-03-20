package compasses.expandedstorage.impl.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public final class BlockLootProvider extends FabricBlockLootTableProvider {
    public BlockLootProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        BlockLootTableHelper.registerLootTables(this::add, this::createNameableBlockEntityTable);
    }

    @Override
    public String getName() {
        return "Expanded Storage - Loot Tables";
    }
}
