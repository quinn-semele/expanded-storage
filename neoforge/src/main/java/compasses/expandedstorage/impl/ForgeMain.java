package compasses.expandedstorage.impl;

import compasses.expandedstorage.impl.block.OpenableBlock;
import compasses.expandedstorage.impl.block.misc.BasicLockable;
import compasses.expandedstorage.impl.block.strategies.ItemAccess;
import compasses.expandedstorage.impl.entity.ChestMinecart;
import compasses.expandedstorage.impl.item.ChestMinecartItem;
import compasses.expandedstorage.impl.misc.ESDataComponents;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.networking.UpdateRecipesPacketPayload;
import compasses.expandedstorage.impl.recipe.ConversionRecipeManager;
import compasses.expandedstorage.impl.recipe.ConversionRecipeReloadListener;
import compasses.expandedstorage.impl.registration.Content;
import compasses.expandedstorage.impl.registration.NamedValue;
import compasses.expandedstorage.impl.block.misc.ChestItemAccess;
import compasses.expandedstorage.impl.block.misc.GenericItemAccess;
import compasses.expandedstorage.impl.item.MiniStorageBlockItem;
import compasses.expandedstorage.impl.misc.ForgeCommonHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod("expandedstorage")
public final class ForgeMain implements IExtensionPoint {
    private Content temporaryContent;

    public ForgeMain(IEventBus modBus, ModContainer mod) {
        mod.registerExtensionPoint(ForgeMain.class, this);

        CommonMain.constructContent(new ForgeCommonHelper(), GenericItemAccess::new, BasicLockable::new,
                FMLLoader.getDist().isClient(), content -> registerContent(modBus, content),
                /*Base*/ false,
                /*Chest*/ BlockItem::new, ChestItemAccess::new,
                /*Minecart Chest*/ ChestMinecartItem::new,
                /*Old Chest*/
                /*Barrel*/ TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("forge", "barrels/wooden")),
                /*Mini Storage*/ MiniStorageBlockItem::new);
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> event.addListener(new ConversionRecipeReloadListener()));
        NeoForge.EVENT_BUS.addListener((OnDatapackSyncEvent event) -> CommonMain.platformHelper().sendConversionRecipesToClient(event.getPlayer(), ConversionRecipeManager.INSTANCE.getBlockRecipes(), ConversionRecipeManager.INSTANCE.getEntityRecipes()));

        NeoForge.EVENT_BUS.addListener((PlayerInteractEvent.EntityInteractSpecific event) -> {
            InteractionResult result = CommonMain.interactWithEntity(event.getLevel(), event.getEntity(), event.getHand(), event.getTarget());
            if (result != InteractionResult.PASS) {
                event.setCancellationResult(result);
                event.setCanceled(true);
            }
        });

        modBus.addListener((RegisterEvent event) -> {
            event.register(Registries.MENU, helper -> {
                helper.register(Utils.HANDLER_TYPE_ID, CommonMain.platformHelper().getScreenHandlerType());
            });
        });

        modBus.addListener(this::registerPayloads);
    }

    @SubscribeEvent
    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Utils.MOD_ID).versioned("2.0.0");

        registrar.playToClient(UpdateRecipesPacketPayload.TYPE, UpdateRecipesPacketPayload.CODEC, (payload, context) -> {
            context.enqueueWork(() -> ConversionRecipeManager.INSTANCE.replaceAllRecipes(payload.blockRecipes(), payload.entityRecipes()));
        });
    }

    private void registerContent(IEventBus modBus, Content content) {
        temporaryContent = content;

        modBus.addListener((RegisterCapabilitiesEvent event) -> {
            event.registerBlock(Capabilities.ItemHandler.BLOCK,
                (level, pos, state, entity, side) -> {
                    return CommonMain.<IItemHandlerModifiable>getItemAccess(level, pos, state, entity).map(ItemAccess::get).orElse(null);
                },
                content.getBlocks().stream().map(NamedValue::getValue).toArray(OpenableBlock[]::new)
            );

            for (NamedValue<EntityType<ChestMinecart>> type : content.getChestMinecartEntityTypes()) {
                event.registerEntity(Capabilities.ItemHandler.ENTITY, type.getValue(), (entity, context) -> new InvWrapper(entity));
                event.registerEntity(Capabilities.ItemHandler.ENTITY_AUTOMATION, type.getValue(), (entity, context) -> new InvWrapper(entity));
            }
        });

        modBus.addListener((RegisterEvent event) -> {
            event.register(Registries.STAT_TYPE, helper -> {
                content.getStats().forEach(it -> Registry.register(BuiltInRegistries.CUSTOM_STAT, it, it));
            });

            event.register(Registries.BLOCK, helper -> {
                CommonMain.iterateNamedList(content.getBlocks(), helper::register);
            });

            event.register(Registries.ITEM, helper -> {
                CommonMain.iterateNamedList(content.getItems(), helper::register);
            });

            event.register(Registries.BLOCK_ENTITY_TYPE, helper -> {
                ForgeMain.registerBlockEntity(helper, content.getChestBlockEntityType());
                ForgeMain.registerBlockEntity(helper, content.getOldChestBlockEntityType());
                ForgeMain.registerBlockEntity(helper, content.getBarrelBlockEntityType());
                ForgeMain.registerBlockEntity(helper, content.getMiniChestBlockEntityType());
            });

            event.register(Registries.ENTITY_TYPE, helper -> {
                CommonMain.iterateNamedList(content.getEntityTypes(), helper::register);
            });

            event.register(Registries.CREATIVE_MODE_TAB, helper -> {
                helper.register(Utils.id("tab"), CreativeModeTab
                        .builder()
                        .icon(() -> BuiltInRegistries.ITEM.get(Utils.id("netherite_chest")).getDefaultInstance())
                        .displayItems((itemDisplayParameters, output) -> {
                            CommonMain.generateDisplayItems(itemDisplayParameters, stack -> {
                                output.accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                            });
                        })
                        .title(Component.translatable("itemGroup.expandedstorage.tab"))
                        .build()
                );
            });

            if (event.getRegistry() == BuiltInRegistries.DATA_COMPONENT_TYPE) {
                ESDataComponents.register();
            }
        });
    }

    public Content getContentForClient() {
        Content local = temporaryContent;
        temporaryContent = null;

        return local;
    }

    private static <T extends BlockEntity> void registerBlockEntity(RegisterEvent.RegisterHelper<BlockEntityType<?>> helper, NamedValue<BlockEntityType<T>> blockEntityType) {
        helper.register(blockEntityType.getName(), blockEntityType.getValue());
    }
}
