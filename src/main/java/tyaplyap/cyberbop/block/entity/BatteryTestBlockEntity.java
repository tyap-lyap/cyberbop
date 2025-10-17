package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import tyaplyap.cyberbop.util.transfer.BlockEnergyStorage;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

import java.util.Map;

public class BatteryTestBlockEntity extends EnergyBlockEntity{

	public BatteryTestBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.BATTERY_TEST, pos, state);
		energyStorage.setEnergy(Integer.MAX_VALUE);
	}

	@Override
	boolean canInsertEnergy(EnergyStorage source, IEnergyStorage.Type sourceType) {
		return false;
	}

	@Override
	boolean canExtractEnergy(EnergyStorage target, IEnergyStorage.Type sourceType) {
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
	public void getDirectionsIO(Map<Direction, BlockEnergyStorage.TypeIO> directionMap) {
		for (var direction : Direction.values()) {
				directionMap.put(direction, BlockEnergyStorage.TypeIO.OUTPUT);
		}
	}

	@Override
	public int getTransferRate() {
		return 900;
	}
}
