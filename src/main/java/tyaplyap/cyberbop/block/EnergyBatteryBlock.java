package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.block.entity.EnergyBatteryBlockEntity;
import tyaplyap.cyberbop.block.entity.EnergyBlockEntity;
import tyaplyap.cyberbop.util.DebugUtil;

public class EnergyBatteryBlock extends BlockWithEntity implements WireConnectable {

	public static final BooleanProperty LIT = Properties.LIT;
	public static final DirectionProperty FACING = Properties.FACING;
	public static final IntProperty LEVEL = IntProperty.of("level", 0, 4);

	protected static final VoxelShape SHAPE = createShape();

	public EnergyBatteryBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(LIT, false).with(LEVEL, 0));
	}

	public static VoxelShape createShape() {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.1875, 0, 0.1875, 0.8125, 1, 0.8125), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.25, 1, 0.25, 0.75, 1.0625, 0.75), BooleanBiFunction.OR);

		return shape;
	}

	@Override
	public boolean canConnect(BlockState state, BlockPos pos, BlockState wireState, BlockPos wirePos, Direction direction) {
		return (direction.equals(Direction.UP) || direction.equals(Direction.DOWN));
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LIT, FACING, LEVEL);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return createCodec(EnergyBatteryBlock::new);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new EnergyBatteryBlockEntity(pos, state);
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : validateTicker(type, CyberbopBlockEntities.BATTERY_BLOCK, (world1, pos, state1, blockEntity) -> {
			EnergyBlockEntity.BatteryTick(world, pos, state, blockEntity);
			DebugUtil.updateEnergyDebug(world, pos, state, blockEntity);
			if(world.getTime() % 20 == 0) {
				float p = ((float)blockEntity.getEnergyStored() / (float)blockEntity.getCapacity());
				int level = 0;
				if(p >= 0.25) level = 1;
				if(p >= 0.50) level = 2;
				if(p >= 0.75) level = 3;
				if(p >= 0.90) level = 4;

				BlockState newState = state1.with(LEVEL, level);
				if(!newState.equals(state1)) world.setBlockState(pos, newState);
			}

		});
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
}
