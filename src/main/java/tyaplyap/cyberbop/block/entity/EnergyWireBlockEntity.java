package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class EnergyWireBlockEntity extends EnergyBlockEntity {

	public Map<Direction, Integer> directionOutput = new HashMap<>();
	public EnergyWireBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.ENERGY_WIRE_BLOCK_ENTITY, pos, state);
		for (var direction : Direction.values()) {
			directionOutput.put(direction, 0);
		}
	}

	private void timerSide() {
		for (Direction direction : Direction.values()) {
			if (directionOutput.get(direction).intValue() > 0) {
				this.directionOutput.put(direction, directionOutput.get(direction).intValue() - 1);
			}
		}
	}



	public static void tick (World world, BlockPos pos, BlockState state, EnergyWireBlockEntity blockEntity) {
		blockEntity.timerSide();
		EnergyBlockEntity.tick(world, pos, state, blockEntity);
	}

	@Override
	public int capacity() {
		return 32000;
	}

	@Override
	public Type type() {
		return Type.WIRE;
	}

	@Override
	public int transferRate() {
		return 24000;
	}
}
