package compasses.expandedstorage.impl.block;

import compasses.expandedstorage.impl.CommonMain;
import compasses.expandedstorage.impl.inventory.OpenableInventory;
import compasses.expandedstorage.impl.inventory.context.BlockContext;
import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MiniStorageBlock extends OpenableBlock implements SimpleWaterloggedBlock {
    private static final VoxelShape NO_RIBBON_NO_SPARROW;
    private static final VoxelShape RIBBON_NO_SPARROW;

    private static final VoxelShape NO_RIBBON_SPARROW_NS, NO_RIBBON_SPARROW_EW;
    private static final VoxelShape RIBBON_SPARROW_NS, RIBBON_SPARROW_EW;

    static {
        NO_RIBBON_NO_SPARROW = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
        RIBBON_NO_SPARROW = Shapes.or(NO_RIBBON_NO_SPARROW, Block.box(6.0D, 8.0D, 6.0D, 10.0D, 9.0D, 10.0D));
        NO_RIBBON_SPARROW_NS = Shapes.or(NO_RIBBON_NO_SPARROW, Block.box(6.0D, 8.0D, 5.0D, 10.0D, 13.0D, 11.0D));
        NO_RIBBON_SPARROW_EW = Shapes.or(NO_RIBBON_NO_SPARROW, Block.box(5.0D, 8.0D, 6.0D, 11.0D, 13.0D, 10.0D));
        RIBBON_SPARROW_NS = Shapes.or(NO_RIBBON_NO_SPARROW, Block.box(6.0D, 8.0D, 5.0D, 10.0D, 14.0D, 11.0D));
        RIBBON_SPARROW_EW = Shapes.or(NO_RIBBON_NO_SPARROW, Block.box(5.0D, 8.0D, 6.0D, 11.0D, 14.0D, 10.0D));
    }

    public static final BooleanProperty SPARROW = BooleanProperty.create("sparrow");
    private final boolean hasRibbon;

    public MiniStorageBlock(Properties settings, ResourceLocation openingStat, boolean hasRibbon) {
        super(settings, openingStat, 1);
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(BlockStateProperties.WATERLOGGED, false).setValue(SPARROW, false));
        this.hasRibbon = hasRibbon;
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter blockLevel, BlockPos pos, CollisionContext context) {
        boolean hasSparrow = state.hasProperty(SPARROW) && state.getValue(SPARROW);
        if (hasRibbon) {
            if (hasSparrow) {
                return state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() % 2 == 0 ? RIBBON_SPARROW_NS : RIBBON_SPARROW_EW;
            }
            return RIBBON_NO_SPARROW;
        } else if (hasSparrow) {
            return state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() % 2 == 0 ? NO_RIBBON_SPARROW_NS : NO_RIBBON_SPARROW_EW;
        }
        return NO_RIBBON_NO_SPARROW;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean placingInWater = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        boolean isSparrowItem = hasSparrowProperty(context.getItemInHand());
        return this.defaultBlockState()
                   .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite())
                   .setValue(BlockStateProperties.WATERLOGGED, placingInWater)
                   .setValue(SPARROW, isSparrowItem);
    }

    public static boolean hasSparrowProperty(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            Tag blockStateTag = tag.get("BlockStateTag");
            if (blockStateTag != null && blockStateTag.getId() == Tag.TAG_COMPOUND) {
                return ((CompoundTag) blockStateTag).getString("sparrow").equals("true");
            }
        }
        return false;
    }

    @NotNull
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.WATERLOGGED, SPARROW);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return CommonMain.getMiniStorageBlockEntityType().create(pos, state);
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public OpenableInventory getOpenableInventory(BlockContext context) {
        if (context.getLevel().getBlockEntity(context.getBlockPos()) instanceof OpenableInventory inventory) {
            return inventory;
        }
        return null;
    }

    @Override
    public ResourceLocation getForcedScreenType() {
        return Utils.MINI_STORAGE_SCREEN_TYPE;
    }
}
