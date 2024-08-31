package dev.compasses.expandedstorage.client.render;

import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.misc.BlockColor;
import dev.compasses.expandedstorage.block.misc.DoubleBlockType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityTextures {
    @SuppressWarnings("deprecation")
    private static final ResourceLocation BLOCK_ATLAS = TextureAtlas.LOCATION_BLOCKS;
    
    public static final Map<DoubleBlockType, Material> chestMaterials = Map.of(
            DoubleBlockType.SINGLE, material("block/chest"),
            DoubleBlockType.LEFT, material("block/chest_left"),
            DoubleBlockType.RIGHT, material("block/chest_right"),
            DoubleBlockType.FRONT, material("block/chest_front"),
            DoubleBlockType.BACK, material("block/chest_back"),
            DoubleBlockType.TOP, material("block/chest_top")
    );

    public static final Map<BlockColor, Material> shulkerBoxMaterials = Arrays
            .stream(BlockColor.values())
            .map(color -> Map.entry(color, material("block/" + color.prefix() + "shulker_box")))
            .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

    private static Material material(String texture) {
        return new Material(BLOCK_ATLAS, Utils.id(texture));
    }
}
