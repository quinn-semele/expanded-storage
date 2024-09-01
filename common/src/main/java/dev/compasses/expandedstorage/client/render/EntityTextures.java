package dev.compasses.expandedstorage.client.render;

import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.ChestBlock;
import dev.compasses.expandedstorage.block.misc.BlockColor;
import dev.compasses.expandedstorage.block.misc.DoubleBlockType;
import dev.compasses.expandedstorage.registration.ModBlocks;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityTextures {
    @SuppressWarnings("deprecation")
    private static final ResourceLocation BLOCK_ATLAS = TextureAtlas.LOCATION_BLOCKS;
    
    public static final Map<ChestBlock, Map<DoubleBlockType, Material>> chestMaterials = new HashMap<>();

    static {
        for (ChestBlock chest : ModBlocks.CHESTS) {
            String name = chest.builtInRegistryHolder().key().location().getPath();

            chestMaterials.put(chest, Map.of(
                    DoubleBlockType.SINGLE, material("block/" + name),
                    DoubleBlockType.LEFT, material("block/" + name + "_left"),
                    DoubleBlockType.RIGHT, material("block/" + name + "_right"),
                    DoubleBlockType.FRONT, material("block/" + name + "_front"),
                    DoubleBlockType.BACK, material("block/" + name + "_back"),
                    DoubleBlockType.TOP, material("block/" + name + "_top")
            ));
        }
    }

    public static final Map<BlockColor, Material> shulkerBoxMaterials = Arrays
            .stream(BlockColor.values())
            .map(color -> Map.entry(color, material("block/" + color.prefix() + "shulker_box")))
            .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

    private static Material material(String texture) {
        return new Material(BLOCK_ATLAS, Utils.id(texture));
    }
}
