package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergyGeneratorBlockEntity extends EnergyBlockEntity {
	private final int generationRate = 1302;

	public EnergyGeneratorBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.ENERGY_GENERATOR_BLOCK_ENTITY, pos, state);
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
		return 256000;
	}

	public static void tick(World world, BlockPos pos, BlockState state, EnergyGeneratorBlockEntity blockEntity) {
		if (world.isClient()) return;

		if (blockEntity.getFreakEnergyStored() < blockEntity.capacity()) {
			blockEntity.setFreakEnergyStored(Math.min(blockEntity.capacity(), blockEntity.getFreakEnergyStored() + blockEntity.generationRate));
		}
		EnergyBlockEntity.tick(world, pos, state, blockEntity);
	}
}
