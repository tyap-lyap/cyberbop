package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class ChargingPadBlockEntity extends EnergyBlockEntity{
	public ChargingPadBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.CHARGING_PAD, pos, state);
	}

	@Override
	public int capacity() {
		return 2400000;
	}

	@Override
	public int transferRate() {
		return 100;
	}

	@Override
	public Type type() {
		return Type.RECEIVER;
	}
}
