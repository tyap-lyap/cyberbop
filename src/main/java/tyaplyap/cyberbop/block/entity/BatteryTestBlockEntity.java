package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

public class BatteryTestBlockEntity extends EnergyBlockEntity{

	public BatteryTestBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.BATTERY_TEST, pos, state);
		energyStorage.setEnergy(Integer.MAX_VALUE);
	}

	@Override
	boolean canInsertEnergy(EnergyStorage source) {
		return true;
	}

	@Override
	boolean canExtractEnergy(EnergyStorage target) {
		return true;
	}


	@Override
	public int getCapacity() {
		return Integer.MAX_VALUE;
	}

	@Override
	public IEnergyStorage.Type typeMachine() {
		return IEnergyStorage.Type.BATTERY;
	}


	@Override
	public int getTransferRate() {
		return 900;
	}
}
