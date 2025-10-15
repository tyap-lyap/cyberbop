package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyaplyap.cyberbop.block.EnergyReceiverBlock;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;
import tyaplyap.cyberbop.util.DebugUtil;

public class EnergyTestReceiverBlockEntity extends EnergyBlockEntity {

	private boolean isActive = false;

	public EnergyTestReceiverBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.ENERGY_RECEIVER, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState state, EnergyTestReceiverBlockEntity blockEntity) {
		DebugUtil.updateEnergyDebug(world, pos, state, blockEntity);
		blockEntity.updateReceiver();
	}

	public int energyConsumption() {
		return 300;
	}

	private void updateReceiver() {
		boolean wasActive = isActive;
		int consumption = energyConsumption();

		if (this.getEnergyStored() >= consumption) {
			setEnergyStored(getEnergyStored() - consumption);
			isActive = true;
		} else {
			isActive = false;
			}
		if (wasActive != isActive) {
			updateBlockState();
		}
	}

	private void updateBlockState() {
		if (world != null && !world.isClient()) {
			BlockState currentState = world.getBlockState(pos);
			if (currentState.getBlock() instanceof EnergyReceiverBlock) {
				world.setBlockState(pos, currentState.with(EnergyReceiverBlock.POWERED, isActive));

				world.updateNeighborsAlways(pos, this.getCachedState().getBlock());
			}
		}
	}

	public boolean isActive() {
		return isActive;
	}

	@Override
	public IEnergyStorage.Type typeMachine() {
		return IEnergyStorage.Type.RECEIVER;
	}

	@Override
	public int getTransferRate() {
		return 90;
	}

	@Override
	public int getCapacity() {
		return 15000;
	}

	@Override
	boolean canInsertEnergy(EnergyStorage source) {
		return true;
	}

	@Override
	boolean canExtractEnergy(EnergyStorage target) {
		return false;
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.putBoolean("IsActive", isActive);
	}

	@Override
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt,registryLookup);
		isActive = nbt.getBoolean("IsActive");
	}
}
