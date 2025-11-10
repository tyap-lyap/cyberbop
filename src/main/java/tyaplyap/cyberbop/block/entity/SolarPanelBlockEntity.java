package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import tyaplyap.cyberbop.util.transfer.BlockEnergyStorage;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

import java.util.Map;

public class SolarPanelBlockEntity extends EnergyBlockEntity{
	public final int energyCapacity;
	public final int generationRate;

	public SolarPanelBlockEntity(int energyCapacity, int generationRate, BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.SOLAR_PANEL, pos, state);
		this.energyCapacity = energyCapacity;
		this.generationRate = generationRate;
	}

	public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.SOLAR_PANEL, pos, state);
		this.energyCapacity = 0;
		this.generationRate = 0;
	}

	@Override
	public void getDirectionsIO(Map<Direction, BlockEnergyStorage.TypeIO> directionMap) {
		for (var direction : Direction.values()) {
			if (!direction.equals(Direction.UP)) {
				directionMap.put(direction, BlockEnergyStorage.TypeIO.OUTPUT);
			}
		}
	}

	public static void tick (World world, BlockPos pos, BlockState state, SolarPanelBlockEntity blockEntity) {
		if (world.getLightLevel(LightType.SKY, pos) == 15) {
			int generationRate = world.isRaining() ? blockEntity.generationRate / 2 : blockEntity.generationRate;

			if (blockEntity.getEnergyStored() < blockEntity.getCapacity()) {
				blockEntity.setEnergyStored(Math.min(blockEntity.getCapacity(), blockEntity.getEnergyStored() + generationRate));
				blockEntity.markDirty();
			}
		}
		EnergyBlockEntity.BatteryTick(world, pos, state, blockEntity);
	}

	@Override
	public IEnergyStorage.Type typeMachine() {
		return IEnergyStorage.Type.GENERATOR;
	}

	@Override
	public int getTransferRate() {
		return this.generationRate * 4;
	}

	@Override
	public int getCapacity() {
		return this.energyCapacity;
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
