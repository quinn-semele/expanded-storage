package dev.compasses.expandedstorage.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.entity.ChestBlockEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class ChestBlockRenderer implements BlockEntityRenderer<ChestBlockEntity> {
    private static final ResourceLocation CHEST_TEXTURE = Utils.id("block/wooden_chest");
    public static final ModelLayerLocation SINGLE_LID_LAYER = new ModelLayerLocation(Utils.id("chest"), "main");

    public static LayerDefinition createSingleLidLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    private final ModelPart singleLid;

    public ChestBlockRenderer(BlockEntityRendererProvider.Context context) {
        var lockPart = new ModelPart(List.of(new CubeWithDifferentTextureMapping(56, 0, 64, 32, 7, -2, 14, 2, 4, 1, EnumSet.allOf(Direction.class))), Map.of());
        var lidPart = new ModelPart(List.of(new CubeWithDifferentTextureMapping(0, 0, 64, 32, 1, 0, 0, 14, 5, 14, EnumSet.allOf(Direction.class))), Map.of(
                "lock", lockPart
        ));
        lidPart.setInitialPose(PartPose.offset(0, 9, 1));
        lidPart.resetPose();

        singleLid = lidPart;
    }

    private static float getLidAngle(final float openness) {
        float delta = 1 - openness;
        delta = 1 - delta * delta * delta;
        return -delta * Mth.HALF_PI;
    }

    @Override
    public void render(ChestBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = entity.getBlockState();

        if (state.getValue(BlockStateProperties.OPEN)) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 0.5D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()));
            poseStack.translate(-0.5D, -0.5D, -0.5D);

            VertexConsumer consumer = new Material(TextureAtlas.LOCATION_BLOCKS, CHEST_TEXTURE).buffer(bufferSource, RenderType::entityCutout);

            singleLid.xRot = getLidAngle(entity.getOpenness(partialTick));
            singleLid.render(poseStack, consumer, packedLight, packedOverlay);

            poseStack.popPose();
        }
    }
}
