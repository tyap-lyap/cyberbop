package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BatteryTestBlockEntity extends EnergyBlockEntity{

	public BatteryTestBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.BATTERY_TEST, pos, state);
		setFreakEnergyStored(Integer.MAX_VALUE);
	}

	@Override
	public int capacity() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int transferRate() {
		return capacity();
	}

	@Override
	public Type type() {
		return Type.BATTERY;
	}
}
