package compasses.expandedstorage.impl.client;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import compasses.expandedstorage.impl.block.ChestBlock;
import compasses.expandedstorage.impl.block.entity.ChestBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.Map;
import java.util.function.Supplier;

public class ChestBlockClientExtensions implements IClientItemExtensions {
    private final Map<ChestBlock, ChestBlockEntity> renderers;
    final Supplier<BlockEntityWithoutLevelRenderer> renderer = Suppliers.memoize(this::createItemRenderer);

    public ChestBlockClientExtensions(Map<ChestBlock, ChestBlockEntity> renderers) {
        this.renderers = renderers;
    }

    private BlockEntityWithoutLevelRenderer createItemRenderer() {
        Minecraft minecraft = Minecraft.getInstance();
        return new BlockEntityWithoutLevelRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels()) {
            @Override
            public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poses, MultiBufferSource source, int light, int overlay) {
                ChestBlockEntity renderEntity = renderers.get((ChestBlock) ((BlockItem) stack.getItem()).getBlock());
                renderEntity.setCustomName(stack.getHoverName());
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(renderEntity, poses, source, light, overlay);
            }
        };
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return renderer.get();
    }
}
