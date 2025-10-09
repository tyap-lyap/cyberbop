package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.ControllerBlockEntity;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.entity.FakePlayerEntity;
import tyaplyap.cyberbop.extension.PlayerExtension;

public class ControllerBlock extends BlockWithEntity {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	protected static final VoxelShape NORTH_SHAPE = createNorthShape();
	protected static final VoxelShape SOUTH_SHAPE = createSouthShape();
	protected static final VoxelShape EAST_SHAPE = createEastShape();
	protected static final VoxelShape WEST_SHAPE = createWestShape();

	public ControllerBlock(Settings settings) {
		super(settings);
	}

	static VoxelShape createNorthShape() {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.1875, 0, 0.25, 0.8125, 0.875, 0.75), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.625, 0.0625, 1, 0.875, 0.3125), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.875, 0.5625, 1, 1.125, 0.8125), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.75, 0.3125, 1, 1, 0.5625), BooleanBiFunction.OR);
		return shape;
	}

	static VoxelShape createSouthShape() {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.1875, 0, 0.25, 0.8125, 0.875, 0.75), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.625, 0.6875, 1, 0.875, 0.9375), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.875, 0.1875, 1, 1.125, 0.4375), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.75, 0.4375, 1, 1, 0.6875), BooleanBiFunction.OR);
		return shape;
	}

	static VoxelShape createEastShape() {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.25, 0, 0.1875, 0.75, 0.875, 0.8125), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.6875, 0.625, 0, 0.9375, 0.875, 1), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.1875, 0.875, 0, 0.4375, 1.125, 1), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.4375, 0.75, 0, 0.6875, 1, 1), BooleanBiFunction.OR);
		return shape;
	}

	static VoxelShape createWestShape() {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.25, 0, 0.1875, 0.75, 0.875, 0.8125), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.0625, 0.625, 0, 0.3125, 0.875, 1), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.5625, 0.875, 0, 0.8125, 1.125, 1), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.3125, 0.75, 0, 0.5625, 1, 1), BooleanBiFunction.OR);
		return shape;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if(state.get(FACING).equals(Direction.NORTH)) return NORTH_SHAPE;
		if(state.get(FACING).equals(Direction.EAST)) return EAST_SHAPE;
		if(state.get(FACING).equals(Direction.SOUTH)) return SOUTH_SHAPE;
		if(state.get(FACING).equals(Direction.WEST)) return WEST_SHAPE;
		return NORTH_SHAPE;
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return createCodec(ControllerBlock::new);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ControllerBlockEntity(pos, state);
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

//	@Override
//	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
//		// Make sure to check world.isClient if you only want to tick only on serverside.
//		return validateTicker(type, CyberbopBlockEntities.CONTROLLER, ControllerBlockEntity::tick);
//	}

	@Override
	protected boolean hasSidedTransparency(BlockState state) {
		return true;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if(!world.isClient()) {
			((PlayerExtension)player).setCyborg(!((PlayerExtension)player).isCyborg());
			player.sendMessage(Text.literal("isCyborg: " + ((PlayerExtension)player).isCyborg()));
			if (player instanceof ServerPlayerEntity playerEntity) {
				FakePlayerEntity fakePlayerEntity = new FakePlayerEntity(CyberbopMod.FAKE_PLAYER_ENTITY,world, playerEntity);
				fakePlayerEntity.setPosition(playerEntity.getPos());
				world.spawnEntity(fakePlayerEntity);
			}
		}
		return ActionResult.SUCCESS;
	}


}
