package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class EnergyWireBlockEntity extends EnergyBlockEntity {

	public Map<Direction, Value> directionOutput = new HashMap<>();
	public EnergyWireBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.ENERGY_WIRE, pos, state);
		for (var direction : Direction.values()) {
			directionOutput.put(direction, new Value(0, "none"));
		}
	}

	private void timerSide() {
		for (Direction direction : Direction.values()) {
			if (directionOutput.get(direction).getTimer() > 0) {
				directionOutput.get(direction).setTimer(directionOutput.get(direction).getTimer() - 1);
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

	public class Value {
		private int timer;
		private String source;

		public Value(Integer timer, String source) {
			this.timer = timer;
			this.source = source;
		}

		public int getTimer() {
			return timer;
		}

		public void setTimer(int timer) {
			this.timer = timer;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}
	}

}
