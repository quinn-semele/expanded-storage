package compasses.expandedstorage.impl.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class BlockStateProvider extends FabricModelProvider {
    public BlockStateProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        Consumer<Item> generateFlatItemModel = item -> generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        ModelHelper.registerItemModels(generateFlatItemModel);
    }

    @NotNull
    @Override
    public String getName() {
        return "Expanded Storage - BlockStates / Models";
    }
}
