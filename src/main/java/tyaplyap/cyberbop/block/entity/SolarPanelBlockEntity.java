package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

public class SolarPanelBlockEntity extends EnergyBlockEntity{
	private int minY;

	public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.SOLAR_PANEL, pos, state);
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

			int generationRate = world.isRaining() ? 128 : 256;

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
		return 20;
	}

	@Override
	public int getCapacity() {
		return Integer.MAX_VALUE;
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
