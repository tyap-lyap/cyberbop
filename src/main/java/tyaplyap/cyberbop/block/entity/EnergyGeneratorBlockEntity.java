package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergyGeneratorBlockEntity extends EnergyBlockEntity {
	private final int generationRate = 1302;

	public EnergyGeneratorBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.ENERGY_GENERATOR, pos, state);
	}

	@Override
	public Type type() {
		return Type.GENERATOR;
	}

	@Override
	public int capacity() {
		return 4096000;
	}

	@Override
	public int transferRate() {
		return capacity();
	}

	public static void tick(World world, BlockPos pos, BlockState state, EnergyGeneratorBlockEntity blockEntity) {
		if (!blockEntity.isFull()) {
			blockEntity.setEnergyStored(Math.min(blockEntity.capacity(), blockEntity.getEnergyStored() + blockEntity.generationRate));
		}
		EnergyBlockEntity.tick(world, pos, state, blockEntity);
	}
}
