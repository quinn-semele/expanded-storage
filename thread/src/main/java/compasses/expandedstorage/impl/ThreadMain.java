package compasses.expandedstorage.impl;

import compasses.expandedstorage.impl.block.ChestBlock;
import compasses.expandedstorage.impl.block.MiniStorageBlock;
import compasses.expandedstorage.impl.block.OpenableBlock;
import compasses.expandedstorage.impl.block.misc.BasicLockable;
import compasses.expandedstorage.impl.block.misc.CopperBlockHelper;
import compasses.expandedstorage.impl.block.strategies.ItemAccess;
import compasses.expandedstorage.impl.compat.carrier.CarrierCompat;
import compasses.expandedstorage.impl.compat.htm.HTMLockable;
import compasses.expandedstorage.impl.item.ChestMinecartItem;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.recipe.ConversionRecipeReloadListener;
import compasses.expandedstorage.impl.registration.Content;
import compasses.expandedstorage.impl.registration.NamedValue;
import compasses.expandedstorage.impl.block.misc.ChestItemAccess;
import compasses.expandedstorage.impl.block.misc.GenericItemAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ThreadMain implements ModInitializer {
    public static final ResourceLocation UPDATE_RECIPES_ID = Utils.id("update_conversion_recipes");
    private static Content temporaryContent;

    @Override
    public void onInitialize() {
        FabricLoader fabricLoader = FabricLoader.getInstance();

        boolean isClient = fabricLoader.getEnvironmentType() == EnvType.CLIENT;

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Utils.id("tab"), FabricItemGroup
                .builder()
                .icon(() -> BuiltInRegistries.ITEM.get(Utils.id("netherite_chest")).getDefaultInstance())
                .displayItems((itemDisplayParameters, output) -> {
                    CommonMain.generateDisplayItems(itemDisplayParameters, stack -> {
                        output.accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                    });
                })
                .title(Component.translatable("itemGroup.expandedstorage.tab")).build()
        );

        CommonMain.constructContent(new ThreadCommonHelper(), GenericItemAccess::new, fabricLoader.isModLoaded("htm") ? HTMLockable::new : BasicLockable::new, isClient, ThreadMain::registerContent,
                /*Base*/ true,
                /*Chest*/ BlockItem::new, ChestItemAccess::new,
                /*Minecart Chest*/ ChestMinecartItem::new,
                /*Old Chest*/
                /*Barrel*/ TagKey.create(Registries.BLOCK, new ResourceLocation("c", "wooden_barrels")),
                /*Mini Storage*/ BlockItem::new);

        UseEntityCallback.EVENT.register((player, world, hand, entity, hit) -> CommonMain.interactWithEntity(world, player, hand, entity));
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            private final PreparableReloadListener base = new ConversionRecipeReloadListener();

            @Override
            public ResourceLocation getFabricId() {
                return Utils.id("conversion_recipe_loader");
            }

            @NotNull
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller filler1, ProfilerFiller filler2, Executor executor1, Executor executor2) {
                return base.reload(barrier, manager, filler1, filler2, executor1, executor2);
            }
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            ((ThreadCommonHelper) CommonMain.platformHelper()).setServerInstance(null);
        });
    }

    @SuppressWarnings({"UnstableApiUsage"})
    public static Storage<ItemVariant> getItemAccess(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @SuppressWarnings("unused") Direction context) {
        return CommonMain.<Storage<ItemVariant>>getItemAccess(level, pos, state, blockEntity).map(ItemAccess::get).orElse(null);
    }

    public static Content getContentForClient() {
        var content = temporaryContent;
        temporaryContent = null;

        return content;
    }

    public static void registerContent(Content content) {
        for (ResourceLocation stat : content.getStats()) {
            Registry.register(BuiltInRegistries.CUSTOM_STAT, stat, stat);
        }

        CommonMain.iterateNamedList(content.getBlocks(), (name, value) -> {
            Registry.register(BuiltInRegistries.BLOCK, name, value);
        });

        //noinspection UnstableApiUsage
        ItemStorage.SIDED.registerForBlocks(ThreadMain::getItemAccess, content.getBlocks().stream().map(NamedValue::getValue).toArray(OpenableBlock[]::new));

        CommonMain.iterateNamedList(content.getItems(), (name, value) -> Registry.register(BuiltInRegistries.ITEM, name, value));

        CommonMain.iterateNamedList(content.getEntityTypes(), (name, value) -> {
            Registry.register(BuiltInRegistries.ENTITY_TYPE, name, value);
        });

        ThreadMain.registerBlockEntity(content.getChestBlockEntityType());
        ThreadMain.registerBlockEntity(content.getOldChestBlockEntityType());
        ThreadMain.registerBlockEntity(content.getBarrelBlockEntityType());
        ThreadMain.registerBlockEntity(content.getMiniChestBlockEntityType());

        CopperBlockHelper.oxidisation().forEach(OxidizableBlocksRegistry::registerOxidizableBlockPair);
        CopperBlockHelper.dewaxing().inverse().forEach(OxidizableBlocksRegistry::registerWaxableBlockPair);

        if (FabricLoader.getInstance().isModLoaded("carrier")) {
            for (NamedValue<? extends OpenableBlock> block : content.getBlocks()) {
                CarrierCompat.removeEntry(block.getValue());

                if (block.getValue() instanceof ChestBlock chestBlock) {
                    CarrierCompat.registerChestBlock(chestBlock);
                } else if (block.getValue() instanceof MiniStorageBlock storageBlock) {
                  CarrierCompat.registerMiniBlock(storageBlock);
                } else {
                    CarrierCompat.registerOpenableBlock(block.getValue());
                }
            }
        }

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            temporaryContent = content;
        }
    }

    private static <T extends BlockEntity> void registerBlockEntity(NamedValue<BlockEntityType<T>> blockEntityType) {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, blockEntityType.getName(), blockEntityType.getValue());
    }
}
