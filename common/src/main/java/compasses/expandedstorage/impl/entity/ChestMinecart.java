package compasses.expandedstorage.impl.entity;

import compasses.expandedstorage.impl.block.ChestBlock;
import compasses.expandedstorage.impl.inventory.ExposedInventory;
import compasses.expandedstorage.impl.inventory.OpenableInventory;
import compasses.expandedstorage.impl.inventory.OpenableInventoryProvider;
import compasses.expandedstorage.impl.inventory.context.BaseContext;
import compasses.expandedstorage.impl.client.helpers.InventoryOpeningApi;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ChestMinecart extends AbstractMinecart implements ExposedInventory, OpenableInventoryProvider<BaseContext>, OpenableInventory {
    private final NonNullList<ItemStack> inventory;
    private final Item dropItem;
    private final BlockState renderBlockState;
    private final Component title;
    private final Item chestItem;

    public ChestMinecart(EntityType<?> entityType, Level level, Item dropItem, ChestBlock block) {
        super(entityType, level);
        this.dropItem = dropItem;
        renderBlockState = block.defaultBlockState();
        title = dropItem.getDescription();
        inventory = NonNullList.withSize(block.getSlotCount(), ItemStack.EMPTY);
        chestItem = block.asItem();
    }

    @NotNull
    @Override
    protected Item getDropItem() {
        return dropItem;
    }

    @NotNull
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(dropItem);
    }

    @NotNull
    @Override
    public BlockState getDisplayBlockState() {
        return renderBlockState;
    }

    @NotNull
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        boolean isClient = level().isClientSide();
        if (!isClient) {
            InventoryOpeningApi.openEntityInventory((ServerPlayer) player, this);
        }
        return InteractionResult.sidedSuccess(isClient);
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        if (!level().isClientSide() && reason.shouldDestroy()) {
            Containers.dropContents(level(), this, this);
        }
        super.remove(reason);
    }

    @Override
    public void onInitialOpen(ServerPlayer player) {
        if (!player.level().isClientSide()) {
            PiglinAi.angerNearbyPiglins(player, true);
        }
    }

    public void destroy(DamageSource source) {
        this.kill();
        if (level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            Entity breaker = source.getDirectEntity();
            if (breaker != null && breaker.isShiftKeyDown()) {
                ItemStack stack = new ItemStack(chestItem);
                if (this.hasCustomName()) {
                    stack.setHoverName(this.getCustomName());
                }
                this.spawnAtLocation(stack);
                this.spawnAtLocation(Items.MINECART);
            } else {
                ItemStack stack = new ItemStack(this.getDropItem());
                if (this.hasCustomName()) {
                    stack.setHoverName(this.getCustomName());
                }
                this.spawnAtLocation(stack);
            }
            if (!level().isClientSide()) {
                if (breaker != null && breaker.getType() == EntityType.PLAYER) {
                    PiglinAi.angerNearbyPiglins((Player) breaker, true);
                }
            }
        }
    }

    @NotNull
    @Override
    public Type getMinecartType() {
        return Type.CHEST;
    }

    @SuppressWarnings("DataFlowIssue")
    public static ChestMinecart createMinecart(Level level, Vec3 pos, ResourceLocation cartItemId) {
        return level.registryAccess().registry(Registries.ENTITY_TYPE).map(registry -> {
            ChestMinecart cart = (ChestMinecart) registry.get(cartItemId).create(level);
            cart.setPos(pos);
            return cart;
        }).orElse(null);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return this.isAlive() && player.distanceToSqr(this) <= 64.0D;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        this.saveInventoryToTag(tag);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.loadInventoryFromTag(tag);
    }

    @Override
    public OpenableInventory getOpenableInventory(BaseContext context) {
        return this;
    }

    @Override
    public boolean canBeUsedBy(ServerPlayer player) {
        return this.stillValid(player);
    }

    @Override
    public Container getInventory() {
        return this;
    }

    @Override
    public Component getInventoryTitle() {
        return this.hasCustomName() ? this.getCustomName() : title;
    }

    @Override
    public int getDefaultDisplayOffset() {
        return 8;
    }
}
