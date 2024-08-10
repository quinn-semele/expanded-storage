package compasses.expandedstorage.impl.client;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import compasses.expandedstorage.impl.entity.ChestMinecart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ChestCartClientExtensions implements IClientItemExtensions {
    private final Supplier<BlockEntityWithoutLevelRenderer> renderer;
    private Map<Item, ChestMinecart> renderers;

    public ChestCartClientExtensions(Map<Item, EntityType<ChestMinecart>> entityTypes) {
        this.renderer = Suppliers.memoize(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            this.renderers = entityTypes
                    .entrySet().stream()
                    .map(entry -> Map.entry(entry.getKey(), entry.getValue().create(minecraft.level)))
                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

            return new BlockEntityWithoutLevelRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels()) {
                @Override
                public void renderByItem(ItemStack itemStack, ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay) {
                    ChestMinecart renderEntity = renderers.get(itemStack.getItem());

                    Minecraft.getInstance().getEntityRenderDispatcher().render(renderEntity, 0, 0, 0, 0, 0, stack, source, light);
                }
            };
        });
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return renderer.get();
    }
}
