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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.block.entity.EnergyWireBlockEntity;
import tyaplyap.cyberbop.block.entity.IEnergy;

public class EnergyWireBlock extends BlockWithEntity {
	public static final BooleanProperty UP = Properties.UP;
	public static final BooleanProperty DOWN = Properties.DOWN;
	public static final BooleanProperty NORTH = Properties.NORTH;
	public static final BooleanProperty EAST = Properties.EAST;
	public static final BooleanProperty SOUTH = Properties.SOUTH;
	public static final BooleanProperty WEST = Properties.WEST;

	protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 4.0, 4.0, 12.0, 12.0, 12.0);

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
		return SHAPE;
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
