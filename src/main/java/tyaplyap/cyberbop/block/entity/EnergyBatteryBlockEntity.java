package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import tyaplyap.cyberbop.block.EnergyBatteryBlock;
import tyaplyap.cyberbop.util.transfer.BlockEnergyStorage;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

import java.util.Map;

public class EnergyBatteryBlockEntity extends EnergyBlockEntity{

	public EnergyBatteryBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.BATTERY_BLOCK, pos, state);
	}

	@Override
	boolean canInsertEnergy(EnergyStorage source, IEnergyStorage.Type sourceType) {
		return true;
	}

	@Override
	boolean canExtractEnergy(EnergyStorage target, IEnergyStorage.Type sourceType) {
		return true;
	}

	@Override
	public void getDirectionsIO(Map<Direction, BlockEnergyStorage.TypeIO> directionMap) {
		directionMap.put(getCachedState().get(EnergyBatteryBlock.FACING), BlockEnergyStorage.TypeIO.OUTPUT);
		directionMap.put(getCachedState().get(EnergyBatteryBlock.FACING).getOpposite(), BlockEnergyStorage.TypeIO.INPUT);
	}

	@Override
	public int getCapacity() {
		return 256000;
	}

	@Override
	public IEnergyStorage.Type typeMachine() {
		return IEnergyStorage.Type.BATTERY;
	}


	@Override
	public int getTransferRate() {
		return 128;
	}
}
