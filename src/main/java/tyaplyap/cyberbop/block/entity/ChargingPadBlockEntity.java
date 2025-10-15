package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

public class ChargingPadBlockEntity extends EnergyBlockEntity{
	public ChargingPadBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.CHARGING_PAD, pos, state);
	}

	@Override
	public IEnergyStorage.Type typeMachine() {
		return IEnergyStorage.Type.RECEIVER;
	}

	@Override
	boolean canInsertEnergy(EnergyStorage source) {
		return true;
	}

	@Override
	boolean canExtractEnergy(EnergyStorage target) {
		return target.type().equals(IEnergyStorage.Type.CYBORG);
	}


	@Override
	public int getTransferRate() {
		return 100;
	}

	@Override
	public int getCapacity() {
		return 2400000;
	}
}
