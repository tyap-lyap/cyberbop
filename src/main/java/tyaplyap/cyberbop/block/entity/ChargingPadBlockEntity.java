package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import tyaplyap.cyberbop.util.transfer.BlockEnergyStorage;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

import java.util.Map;

public class ChargingPadBlockEntity extends EnergyBlockEntity{
	public ChargingPadBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.CHARGING_PAD, pos, state);
	}

	@Override
	public IEnergyStorage.Type typeMachine() {
		return IEnergyStorage.Type.RECEIVER;
	}

	@Override
	boolean canInsertEnergy(EnergyStorage source, IEnergyStorage.Type sourceType) {
		return true;
	}

	@Override
	boolean canExtractEnergy(EnergyStorage target, IEnergyStorage.Type sourceType) {
		return target.type().equals(IEnergyStorage.Type.CYBORG);
	}

	@Override
	public void getDirectionsIO(Map<Direction, BlockEnergyStorage.TypeIO> directionMap) {
		for (var direction : Direction.values()) {
			if (!direction.equals(Direction.UP)) {
				directionMap.put(direction, BlockEnergyStorage.TypeIO.INPUT);
			}
		}
	}

	@Override
	public int getTransferRate() {
		return 32;
	}

	@Override
	public int getCapacity() {
		return 64000;
	}
}
