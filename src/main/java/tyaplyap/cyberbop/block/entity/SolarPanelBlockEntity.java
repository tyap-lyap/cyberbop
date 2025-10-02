package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class SolarPanelBlockEntity extends EnergyBlockEntity{
	private int minY;

	public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.SOLAR_PANEL_BLOCK_ENTITY, pos, state);
	}

	@Override
	public int capacity() {
		return 2400000;
	}

	@Override
	public Type type() {
		return Type.GENERATOR;
	}

	public static void tick (World world, BlockPos pos, BlockState state, SolarPanelBlockEntity blockEntity) {
		EnergyBlockEntity.tick(world, pos, state, blockEntity);


		if (!world.isClient && world.getTimeOfDay() % 24000 < 13000) {

			BlockPos blockPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());

			int topY = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.getX(), pos.getZ());
			for (int y = pos.getY(); y < topY; y++) {
				BlockState blockState = world.getBlockState(new BlockPos(pos.getX(), y, pos.getZ()));
				if (blockEntity.capacity() == blockEntity.getFreakEnergyStored() || blockState.getOpacity(world, blockPos) >= 15) {

					return;
				}
				blockPos = blockPos.up();
			}

			int generationRate = world.isRaining() ? 128 : 256;

			if (blockEntity.getFreakEnergyStored() < blockEntity.capacity()) {
				blockEntity.setFreakEnergyStored(Math.min(blockEntity.capacity(), blockEntity.getFreakEnergyStored() + generationRate));
			}

		}

	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		this.minY = world.getBottomY() - 1;
	}
}
