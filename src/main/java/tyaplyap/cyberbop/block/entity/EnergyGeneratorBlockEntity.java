package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import tyaplyap.cyberbop.util.transfer.BlockEnergyStorage;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

import java.util.Map;

public class EnergyGeneratorBlockEntity extends EnergyBlockEntity {
	private final int generationRate = 1302;

	public EnergyGeneratorBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.ENERGY_GENERATOR, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState state, EnergyGeneratorBlockEntity blockEntity) {
		if (!blockEntity.isFull()) {
			blockEntity.setEnergyStored(Math.min(blockEntity.getCapacity(), blockEntity.getEnergyStored() + blockEntity.generationRate));
		}
		EnergyBlockEntity.BatteryTick(world, pos, state, blockEntity);
	}

	@Override
	public IEnergyStorage.Type typeMachine() {
		return IEnergyStorage.Type.GENERATOR;
	}

	@Override
	public int getTransferRate() {
		return 900;
	}

	@Override
	public int getCapacity() {
		return 4096000;
	}

	@Override
	public void getDirectionsIO(Map<Direction, BlockEnergyStorage.TypeIO> directionMap) {
		for (var direction : Direction.values()) {
			directionMap.put(direction, BlockEnergyStorage.TypeIO.OUTPUT);
		}
	}

	@Override
	boolean canInsertEnergy(EnergyStorage source, IEnergyStorage.Type sourceType) {
		return false;
	}

	@Override
	boolean canExtractEnergy(EnergyStorage target, IEnergyStorage.Type sourceType) {
		return true;
	}
}
