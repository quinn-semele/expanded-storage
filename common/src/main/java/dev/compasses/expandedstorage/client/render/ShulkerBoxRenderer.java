package dev.compasses.expandedstorage.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.compasses.expandedstorage.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class ShulkerBoxRenderer implements BlockEntityRenderer<ShulkerBoxBlockEntity> {
    private final ModelPart base;
    private final ModelPart lid;

    public ShulkerBoxRenderer(BlockEntityRendererProvider.Context context) {
        lid = new ModelPart(List.of(new CubeWithDifferentTextureMapping(0, 0, 64, 64, -8, 4, -8, 16, 12, 16, EnumSet.allOf(Direction.class))), Map.of());
        base = new ModelPart(List.of(new CubeWithDifferentTextureMapping(0, 28, 64, 64, -8, 0, -8, 16, 8, 16, EnumSet.allOf(Direction.class))), Map.of("lid", lid));
        base.setInitialPose(PartPose.offset(0, -8, 0)); base.resetPose();
    }

    @Override
    public void render(ShulkerBoxBlockEntity entity, float partialTick, PoseStack poses, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = entity.getBlockState();

        if (!state.getValue(BlockStateProperties.OPEN)) {
            return;
        }

        Direction facing = entity.getBlockState().getValue(BlockStateProperties.FACING);

        Material material = EntityTextures.shulkerBoxMaterials.get(entity.getColor());

        poses.pushPose();
        poses.translate(0.5F, 0.5F, 0.5F);
        poses.mulPose(facing.getRotation());
        poses.scale(0.995F, 0.995F, 0.995F);

        float lidOpenness = entity.lidController().getOpenness(partialTick);

        lid.setPos(0, 8 * lidOpenness , 0);
        lid.yRot = 270 * lidOpenness * Mth.DEG_TO_RAD;

        VertexConsumer vertexconsumer = material.buffer(bufferSource, RenderType::entityCutoutNoCull);
        base.render(poses, vertexconsumer, packedLight, packedOverlay);
        poses.popPose();
    }
}
