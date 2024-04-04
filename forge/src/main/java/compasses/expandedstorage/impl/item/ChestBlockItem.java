package compasses.expandedstorage.impl.item;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import compasses.expandedstorage.impl.CommonMain;
import compasses.expandedstorage.impl.block.entity.ChestBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ChestBlockItem extends BlockItem {
    public ChestBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final Supplier<BlockEntityWithoutLevelRenderer> renderer = Suppliers.memoize(this::createItemRenderer);

            private BlockEntityWithoutLevelRenderer createItemRenderer() {
                ChestBlockEntity renderEntity = CommonMain.getChestBlockEntityType().create(BlockPos.ZERO, ChestBlockItem.this.getBlock().defaultBlockState());
                Minecraft minecraft = Minecraft.getInstance();
                return new BlockEntityWithoutLevelRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels()) {
                    @Override
                    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poses, MultiBufferSource source, int light, int overlay) {
                        renderEntity.setCustomName(stack.getHoverName());
                        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(renderEntity, poses, source, light, overlay);
                    }
                };
            }

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer.get();
            }
        });
    }
}
