package compasses.expandedstorage.impl.compat.carrier;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.steven.carrier.api.Carriable;
import me.steven.carrier.api.CarriablePlacementContext;
import me.steven.carrier.api.CarrierComponent;
import me.steven.carrier.api.CarryingData;
import me.steven.carrier.mixin.AccessorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class CarriableOldChest implements Carriable<Block> {
    private final ResourceLocation id;
    private final Block parent;

    public CarriableOldChest(ResourceLocation id, Block parent) {
        this.id = id;
        this.parent = parent;
    }

    @NotNull
    @Override
    public final Block getParent() {
        return parent;
    }

    @Override
    public @NotNull InteractionResult tryPickup(@NotNull Player player, @NotNull Level level, @NotNull BlockPos blockPos, @Nullable Entity entity) {
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            BlockState state = level.getBlockState(blockPos);

            new CarryingData(id, state, blockEntity);

            level.removeBlockEntity(blockPos);
            level.removeBlock(blockPos, false);

            return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResult tryPlace(@NotNull CarryingData carryingData, @NotNull Level level, @NotNull CarriablePlacementContext context) {
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        BlockPos pos = context.getBlockPos();

        BlockState state = parent.getStateForPlacement(new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, ItemStack.EMPTY, new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), context.getSide(), context.getBlockPos(), false)) {
            public Direction getHorizontalDirection() {
                return context.getPlayerLook();
            }

            public boolean isSecondaryUseActive() {
                return context.isSneaking();
            }
        });

        level.setBlockAndUpdate(pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity != null) {
            CompoundTag tag = carryingData.getBlockEntityTag();
            ((AccessorBlockEntity)blockEntity).carrier_writeIdentifyingData(tag);
            blockEntity.load(tag);
        }

        level.blockUpdated(pos, state.getBlock());

        return InteractionResult.SUCCESS;
    }

    @Override
    public final void render(@NotNull Player player, @NotNull CarrierComponent component, @NotNull PoseStack stack, @NotNull MultiBufferSource consumer, float delta, int light) {
        stack.pushPose();
        stack.scale(0.6F, 0.6F, 0.6F);
        float yaw = Mth.approachDegrees(delta, player.yBodyRotO, player.yBodyRot);
        stack.mulPose(Axis.YP.rotationDegrees(180 - yaw));
        stack.translate(-0.5D, 0.8D, -1.3D);
        this.preRenderBlock(player, component, stack, consumer, delta, light);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(this.getParent().defaultBlockState(), stack, consumer, light, OverlayTexture.NO_OVERLAY);
        stack.popPose();
    }

    protected void preRenderBlock(Player player, CarrierComponent component, PoseStack stack, MultiBufferSource consumer, float delta, int light) {

    }
}
