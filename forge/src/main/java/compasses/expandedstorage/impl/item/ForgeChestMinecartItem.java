package compasses.expandedstorage.impl.item;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import compasses.expandedstorage.impl.entity.ChestMinecart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ForgeChestMinecartItem extends ChestMinecartItem {
    public ForgeChestMinecartItem(Properties properties, ResourceLocation cartId) {
        super(properties, cartId);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final Supplier<BlockEntityWithoutLevelRenderer> renderer = Suppliers.memoize(this::createItemRenderer);

            private BlockEntityWithoutLevelRenderer createItemRenderer() {
                Minecraft minecraft = Minecraft.getInstance();
                EntityType<ChestMinecart> entityType = (EntityType<ChestMinecart>) BuiltInRegistries.ENTITY_TYPE.get(ForgeChestMinecartItem.this.cartId);
                Supplier<ChestMinecart> renderEntity = Suppliers.memoize(() -> entityType.create(minecraft.level));
                return new BlockEntityWithoutLevelRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels()) {
                    @Override
                    public void renderByItem(ItemStack itemStack, ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay) {
                        Minecraft.getInstance().getEntityRenderDispatcher().render(renderEntity.get(), 0, 0, 0, 0, 0, stack, source, light);
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
