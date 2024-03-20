package compasses.expandedstorage.impl.client.compat.inventory_tabs;

import compasses.expandedstorage.api.EsChestType;
import compasses.expandedstorage.api.ExpandedStorageAccessors;
import compasses.expandedstorage.impl.block.AbstractChestBlock;
import compasses.expandedstorage.impl.entity.ChestMinecart;
import compasses.expandedstorage.impl.misc.Utils;
import folk.sisby.inventory_tabs.TabProviders;
import folk.sisby.inventory_tabs.providers.BlockTabProvider;
import folk.sisby.inventory_tabs.tabs.BlockTab;
import folk.sisby.inventory_tabs.tabs.Tab;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

public class ExpandedBlockTabProvider extends BlockTabProvider {
    public static final ExpandedBlockTabProvider INSTANCE = TabProviders.register(Utils.id("block_chest"), new ExpandedBlockTabProvider());

    ExpandedBlockTabProvider() {
        super();
        matches.put(Utils.id("abstract_chest_block"), b -> b instanceof AbstractChestBlock);
        preclusions.put(Utils.id("access_blocked"), (w, p) -> {
            if (w.getBlockState(p).getBlock() instanceof AbstractChestBlock acb) {
                Optional<Direction> otherDirection = ExpandedStorageAccessors.getAttachedChestDirection(w.getBlockState(p));
                if (otherDirection.isPresent() && w.getBlockState(p.relative(otherDirection.get())).getBlock() instanceof AbstractChestBlock otherAcb) {
                    return otherAcb.isAccessBlocked(w, p.relative(otherDirection.get())) || acb.isAccessBlocked(w, p);
                }
                return acb.isAccessBlocked(w, p);
            }
            return false;
        });
    }

    public static void register() {
        TabProviders.ENTITY_SIMPLE.warmMatches.put(Utils.id("chest_minecart"), e -> e instanceof ChestMinecart);
    }

    @Override
    public int getTabOrderPriority(Level world, BlockPos pos) {
        return -50;
    }

    @Override
    public int getRegistryPriority() {
        return 100;
    }

    @Override
    public Tab createTab(Level world, BlockPos pos) {
        return new ExpandedBlockTab(world, pos, preclusions, getTabOrderPriority(world, pos), isUnique());
    }

    public static class ExpandedBlockTab extends BlockTab {
        public ExpandedBlockTab(Level world, BlockPos pos, Map<ResourceLocation, BiPredicate<Level, BlockPos>> preclusions, int priority, boolean unique) {
            super(world, pos, preclusions, priority, unique);
        }

        @Override
        protected void refreshMultiblock(Level world) {
            multiblockPositions.clear();
            multiblockPositions.add(pos);
            BlockState state = world.getBlockState(pos);
            ExpandedStorageAccessors.getAttachedChestDirection(state).ifPresent(otherBlockDirection -> {
                multiblockPositions.add(pos.relative(otherBlockDirection));
            });
            if (multiblockPositions.size() == 2) { // Ensure the list has a stable order for tab-equality checks.
                BlockState otherState = world.getBlockState(multiblockPositions.get(1));
                Optional<EsChestType> type = ExpandedStorageAccessors.getChestType(state);
                Optional<EsChestType> otherType = ExpandedStorageAccessors.getChestType(otherState);
                if (type.isPresent() && otherType.isPresent()) {
                    if (type.get().ordinal() > otherType.get().ordinal()) {
                        Collections.reverse(multiblockPositions);
                    }
                }
            }
        }
    }
}
