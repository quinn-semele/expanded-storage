package dev.compasses.expandedstorage.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.compasses.expandedstorage.Utils;
import dev.compasses.expandedstorage.block.ChestBlock;
import dev.compasses.expandedstorage.block.entity.ChestBlockEntity;
import dev.compasses.expandedstorage.block.misc.DoubleBlockType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
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
    private static final ResourceLocation LEFT_CHEST_TEXTURE = Utils.id("block/wooden_chest_left");
    private static final ResourceLocation RIGHT_CHEST_TEXTURE = Utils.id("block/wooden_chest_right");
    private static final ResourceLocation TOP_CHEST_TEXTURE = Utils.id("block/wooden_chest_top");
    private static final ResourceLocation FRONT_CHEST_TEXTURE = Utils.id("block/wooden_chest_front");
    private static final ResourceLocation BACK_CHEST_TEXTURE = Utils.id("block/wooden_chest_back");

    private final ModelPart singleLid;
    private final ModelPart leftLid, rightLid;
    private final ModelPart frontLid, backLid;

    private static ModelPart initializeLid(ModelPart lid, float offsetX, float offsetY, float offsetZ) {
        lid.setInitialPose(PartPose.offset(offsetX, offsetY, offsetZ));
        lid.resetPose();

        return lid;
    }

    public ChestBlockRenderer(BlockEntityRendererProvider.Context context) {
        {
            var visibleFaces = EnumSet.allOf(Direction.class);
            var lockPart = new ModelPart(List.of(new CubeWithDifferentTextureMapping(56, 0, 64, 32, 7, -2, 14, 2, 4, 1, visibleFaces)), Map.of());
            var lidPart = new ModelPart(List.of(new CubeWithDifferentTextureMapping(0, 0, 64, 32, 1, 0, 0, 14, 5, 14, visibleFaces)), Map.of("lock", lockPart));
            singleLid = initializeLid(lidPart, 0, 9, 1);
        }

        {
            var visibleFaces = EnumSet.allOf(Direction.class); visibleFaces.remove(Direction.EAST);
            var lockPart = new ModelPart(List.of(new CubeWithDifferentTextureMapping(58, 14, 64, 32, 15, -2, 14, 1, 4, 1, visibleFaces)), Map.of());
            var lidPart = new ModelPart(List.of(new CubeWithDifferentTextureMapping(0, 0, 64, 32, 1, 1, 0, 0, 15, 5, 14, visibleFaces)), Map.of("lock", lockPart));
            leftLid = initializeLid(lidPart, 0, 9, 1);
        }

        {
            var visibleFaces = EnumSet.allOf(Direction.class); visibleFaces.remove(Direction.WEST);
            var lockPart = new ModelPart(List.of(new CubeWithDifferentTextureMapping(58, 14, 64, 32, 0, -2, 14, 1, 4, 1, visibleFaces)), Map.of());
            var lidPart = new ModelPart(List.of(new CubeWithDifferentTextureMapping(0, 0, 64, 32, 1, 0, 0, 0, 15, 5, 14, visibleFaces)), Map.of("lock", lockPart));
            rightLid = initializeLid(lidPart, 0, 9, 1);
        }

        {
            var visibleFaces = EnumSet.allOf(Direction.class); visibleFaces.remove(Direction.NORTH);
            var lockPart = new ModelPart(List.of(new CubeWithDifferentTextureMapping(56, 0, 64, 32, 7, -2, 30, 2, 4, 1, EnumSet.allOf(Direction.class))), Map.of());
            var lidPart = new ModelPart(List.of(new CubeWithDifferentTextureMapping(0, 0, 64, 32, -1, 1, 0, 15, 14, 5, 15, visibleFaces)), Map.of("lock", lockPart));
            frontLid = initializeLid(lidPart, 0, 9, -15);
        }

        {
            var visibleFaces = EnumSet.allOf(Direction.class); visibleFaces.remove(Direction.SOUTH);
            var lidPart = new ModelPart(List.of(new CubeWithDifferentTextureMapping(0, 0, 64, 32, -1, 1, 0, 0, 14, 5, 15, visibleFaces)), Map.of());
            backLid = initializeLid(lidPart, 0, 9, 1);
        }
    }

    private static float getLidAngle(final float openness) {
        float delta = 1 - openness;
        delta = 1 - delta * delta * delta;
        return -delta * Mth.HALF_PI;
    }

    @Override
    public void render(ChestBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = entity.getBlockState();
        DoubleBlockType chestType = state.getValue(ChestBlock.CHEST_TYPE);

        if (!state.getValue(BlockStateProperties.OPEN) || chestType == DoubleBlockType.BOTTOM) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        ResourceLocation chestTexture = switch (chestType) {
            case SINGLE -> CHEST_TEXTURE;
            case LEFT -> LEFT_CHEST_TEXTURE;
            case RIGHT -> RIGHT_CHEST_TEXTURE;
            case TOP -> TOP_CHEST_TEXTURE;
            case FRONT -> FRONT_CHEST_TEXTURE;
            case BACK -> BACK_CHEST_TEXTURE;
            case BOTTOM -> throw Utils.codeError(3);
        };

        VertexConsumer consumer = new Material(TextureAtlas.LOCATION_BLOCKS, chestTexture).buffer(bufferSource, RenderType::entityCutout);

        ModelPart lid = switch (chestType) {
            case SINGLE, TOP -> singleLid;
            case BOTTOM -> throw Utils.codeError(4);
            case LEFT -> leftLid;
            case RIGHT -> rightLid;
            case FRONT -> frontLid;
            case BACK -> backLid;
        };

        lid.xRot = getLidAngle(entity.getOpenness(partialTick));
        lid.render(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
