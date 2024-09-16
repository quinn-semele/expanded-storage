package compasses.expandedstorage.impl.datagen.providers;

import compasses.expandedstorage.impl.registration.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;

import java.util.concurrent.CompletableFuture;

public class CopperDataMapProvider extends DataMapProvider {
    public CopperDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        builder(NeoForgeDataMaps.WAXABLES)
                .add(ModBlocks.COPPER_BARREL.getBlockId(), new Waxable(ModBlocks.WAXED_COPPER_BARREL), false)
                .add(ModBlocks.EXPOSED_COPPER_BARREL.getBlockId(), new Waxable(ModBlocks.WAXED_EXPOSED_COPPER_BARREL), false)
                .add(ModBlocks.WEATHERED_COPPER_BARREL.getBlockId(), new Waxable(ModBlocks.WAXED_WEATHERED_COPPER_BARREL), false)
                .add(ModBlocks.OXIDIZED_COPPER_BARREL.getBlockId(), new Waxable(ModBlocks.WAXED_OXIDIZED_COPPER_BARREL), false)
                .add(ModBlocks.COPPER_MINI_BARREL.getBlockId(), new Waxable(ModBlocks.WAXED_COPPER_MINI_BARREL), false)
                .add(ModBlocks.EXPOSED_COPPER_MINI_BARREL.getBlockId(), new Waxable(ModBlocks.WAXED_EXPOSED_COPPER_MINI_BARREL), false)
                .add(ModBlocks.WEATHERED_COPPER_MINI_BARREL.getBlockId(), new Waxable(ModBlocks.WAXED_WEATHERED_COPPER_MINI_BARREL), false)
                .add(ModBlocks.OXIDIZED_COPPER_MINI_BARREL.getBlockId(), new Waxable(ModBlocks.WAXED_OXIDIZED_COPPER_MINI_BARREL), false)
                .build();

        builder(NeoForgeDataMaps.OXIDIZABLES)
                .add(ModBlocks.COPPER_BARREL.getBlockId(), new Oxidizable(ModBlocks.EXPOSED_COPPER_BARREL), false)
                .add(ModBlocks.EXPOSED_COPPER_BARREL.getBlockId(), new Oxidizable(ModBlocks.WEATHERED_COPPER_BARREL), false)
                .add(ModBlocks.WEATHERED_COPPER_BARREL.getBlockId(), new Oxidizable(ModBlocks.OXIDIZED_COPPER_BARREL), false)
                .add(ModBlocks.COPPER_MINI_BARREL.getBlockId(), new Oxidizable(ModBlocks.EXPOSED_COPPER_MINI_BARREL), false)
                .add(ModBlocks.EXPOSED_COPPER_MINI_BARREL.getBlockId(), new Oxidizable(ModBlocks.WEATHERED_COPPER_MINI_BARREL), false)
                .add(ModBlocks.WEATHERED_COPPER_MINI_BARREL.getBlockId(), new Oxidizable(ModBlocks.OXIDIZED_COPPER_MINI_BARREL), false)
                .build();
    }

    @Override
    public String getName() {
        return "Expanded Storage/" + super.getName();
    }
}
