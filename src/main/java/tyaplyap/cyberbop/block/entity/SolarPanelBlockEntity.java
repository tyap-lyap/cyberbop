package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

public class SolarPanelBlockEntity extends EnergyBlockEntity{
	private int minY;
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

	public static void tick (World world, BlockPos pos, BlockState state, SolarPanelBlockEntity blockEntity) {
		if (world.getTimeOfDay() % 24000 < 13000) {

			BlockPos blockPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());

			int topY = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.getX(), pos.getZ());
			for (int y = pos.getY(); y < topY; y++) {
				BlockState blockState = world.getBlockState(new BlockPos(pos.getX(), y, pos.getZ()));
				if (blockEntity.getCapacity() == blockEntity.getEnergyStored() || blockState.getOpacity(world, blockPos) >= 15) {

					return;
				}
				blockPos = blockPos.up();
			}

			int generationRate = world.isRaining() ? blockEntity.generationRate / 2 : blockEntity.generationRate;

			if (blockEntity.getEnergyStored() < blockEntity.getCapacity()) {
				blockEntity.setEnergyStored(Math.min(blockEntity.getCapacity(), blockEntity.getEnergyStored() + generationRate));
				blockEntity.markDirty();
			}

		}
		EnergyBlockEntity.BatteryTick(world, pos, state, blockEntity);
	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		this.minY = world.getBottomY() - 1;
	}

	@Override
	public IEnergyStorage.Type typeMachine() {
		return IEnergyStorage.Type.GENERATOR;
	}

	@Override
	public int getTransferRate() {
		return 16;
	}

	@Override
	public int getCapacity() {
		return this.energyCapacity;
	}

	@Override
	boolean canInsertEnergy(EnergyStorage source) {
		return false;
	}

	@Override
	boolean canExtractEnergy(EnergyStorage target) {
		return true;
	}
}
