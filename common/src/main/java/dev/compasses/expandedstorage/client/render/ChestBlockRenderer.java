package dev.compasses.expandedstorage.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.entity.ChestBlockEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ChestBlockRenderer implements BlockEntityRenderer<ChestBlockEntity> {
    public static final ModelLayerLocation SINGLE_LID_LAYER = new ModelLayerLocation(Utils.id("chest"), "main");

    public static LayerDefinition createSingleLidLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    private final ModelPart singleLid;

    public ChestBlockRenderer(BlockEntityRendererProvider.Context context) {
        singleLid = context.bakeLayer(ChestBlockRenderer.SINGLE_LID_LAYER);
    }

    @Override
    public void render(ChestBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = entity.getBlockState();

        if (state.getValue(BlockStateProperties.OPEN)) {

        }
    }
}
