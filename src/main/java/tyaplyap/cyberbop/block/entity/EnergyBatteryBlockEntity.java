package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

public class EnergyBatteryBlockEntity extends EnergyBlockEntity{

	public EnergyBatteryBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.BATTERY_BLOCK, pos, state);
		energyStorage.setEnergy(getCapacity());
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
		return 100000;
	}

	@Override
	public IEnergyStorage.Type typeMachine() {
		return IEnergyStorage.Type.BATTERY;
	}


	@Override
	public int getTransferRate() {
		return 100;
	}
}
