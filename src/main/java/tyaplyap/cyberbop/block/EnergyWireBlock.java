package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.block.entity.EnergyWireBlockEntity;
import tyaplyap.cyberbop.block.entity.IEnergy;

import java.util.ArrayList;
import java.util.List;

public class EnergyWireBlock extends BlockWithEntity {
	public static final BooleanProperty UP = Properties.UP;
	public static final BooleanProperty DOWN = Properties.DOWN;
	public static final BooleanProperty NORTH = Properties.NORTH;
	public static final BooleanProperty EAST = Properties.EAST;
	public static final BooleanProperty SOUTH = Properties.SOUTH;
	public static final BooleanProperty WEST = Properties.WEST;
	public static final BooleanProperty[] WIRE_DIRECTIONS = {UP, DOWN, NORTH, EAST, SOUTH, WEST};

	protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 4.0, 4.0, 12.0, 12.0, 12.0);
	protected static final VoxelShape SHAPE_Z = Block.createCuboidShape(4.0, 4.0, 12.0, 12.0, 12.0, 16.0);
	protected static final VoxelShape SHAPE_MINUS_Z = Block.createCuboidShape(4.0, 4.0, 0.0, 12.0, 12.0, 4.0);
	protected static final VoxelShape SHAPE_X = Block.createCuboidShape(12.0, 4.0, 4.0, 16.0, 12.0, 12.0);
	protected static final VoxelShape SHAPE_MINUS_X = Block.createCuboidShape(0.0, 4.0, 4.0, 4.0, 12.0, 12.0);
	protected static final VoxelShape SHAPE_Y = Block.createCuboidShape(4.0, 12.0, 4.0, 12.0, 16.0, 12.0);
	protected static final VoxelShape SHAPE_MINUS_Y = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 4.0, 12.0);

	public EnergyWireBlock(Settings settings) {
		super(settings);

		this.setDefaultState(
			this.stateManager
				.getDefaultState()
				.with(UP, false)
				.with(DOWN, false)
				.with(NORTH, false)
				.with(EAST, false)
				.with(SOUTH, false)
				.with(WEST, false)
		);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		List<VoxelShape> shapes = getSideOutline(state);

		if (shapes.isEmpty()) {
			return SHAPE;
		} else {
			VoxelShape[] shapesArray = shapes.toArray(shapes.toArray(new VoxelShape[0]));

			return VoxelShapes.union(SHAPE, shapesArray);
		}
	}

	private List<VoxelShape> getSideOutline(BlockState state) {
		List<VoxelShape> shapes = new ArrayList<>();

		for(var wireDirection : WIRE_DIRECTIONS) {
			if (state.get(wireDirection)) {
				if (wireDirection.equals(UP)) {
					shapes.add(SHAPE_Y);
				} else if (wireDirection.equals(DOWN)) {
					shapes.add(SHAPE_MINUS_Y);
				} else if (wireDirection.equals(NORTH)) {
					shapes.add(SHAPE_MINUS_Z);
				} else if (wireDirection.equals(EAST)) {
					shapes.add(SHAPE_X);
				} else if (wireDirection.equals(SOUTH)) {
					shapes.add(SHAPE_Z);
				} else if (wireDirection.equals(WEST)) {
					shapes.add(SHAPE_MINUS_X);
				}
			}
		}
		return shapes;
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return createCodec(EnergyWireBlock::new);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new EnergyWireBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return validateTicker(type, CyberbopBlockEntities.ENERGY_WIRE_BLOCK_ENTITY, EnergyWireBlockEntity::tick);
	}

	public boolean canConnect(BlockState state) {
		return state.isIn(TagKey.of(RegistryKeys.BLOCK, CyberbopMod.id("wire_connectable")));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = getDefaultState();
		BlockState down = ctx.getWorld().getBlockState(ctx.getBlockPos().down());
		state = state.with(DOWN, canConnect(down));

		BlockState up = ctx.getWorld().getBlockState(ctx.getBlockPos().up());
		state = state.with(UP, canConnect(up));

		BlockState west = ctx.getWorld().getBlockState(ctx.getBlockPos().add(-1, 0, 0));
		state = state.with(WEST, canConnect(west));

		BlockState east = ctx.getWorld().getBlockState(ctx.getBlockPos().add(1, 0, 0));
		state = state.with(EAST, canConnect(east));

		BlockState north = ctx.getWorld().getBlockState(ctx.getBlockPos().add(0, 0, -1));
		state = state.with(NORTH, canConnect(north));

		BlockState south = ctx.getWorld().getBlockState(ctx.getBlockPos().add(0, 0, 1));
		state = state.with(SOUTH, canConnect(south));

		return state;
	}

	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		BlockState down = world.getBlockState(pos.down());
		state = state.with(DOWN, canConnect(down));

		BlockState up = world.getBlockState(pos.up());
		state = state.with(UP, canConnect(up));

		BlockState west = world.getBlockState(pos.add(-1, 0, 0));
		state = state.with(WEST, canConnect(west));

		BlockState east = world.getBlockState(pos.add(1, 0, 0));
		state = state.with(EAST, canConnect(east));

		BlockState north = world.getBlockState(pos.add(0, 0, -1));
		state = state.with(NORTH, canConnect(north));

		BlockState south = world.getBlockState(pos.add(0, 0, 1));
		state = state.with(SOUTH, canConnect(south));


		return state;
	}
}
