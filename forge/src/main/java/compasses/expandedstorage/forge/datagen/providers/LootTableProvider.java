package compasses.expandedstorage.forge.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public final class LootTableProvider extends net.minecraft.data.loot.LootTableProvider {
    public LootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(new SubProviderEntry(BlockLootProvider::new, LootContextParamSets.BLOCK)));
    }

    @Override
    public String getName() {
        return "Expanded Storage - Loot Tables";
    }
}
