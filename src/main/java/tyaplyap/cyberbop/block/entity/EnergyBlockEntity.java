package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;


public abstract class EnergyBlockEntity extends BlockEntity implements IEnergy{

	private int freakEnergyStored;

	public EnergyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static void tick (World world, BlockPos pos, BlockState state, EnergyBlockEntity blockEntity) {
		if (!world.isClient()) {

			List<Direction> directionsFind = new ArrayList<>(Arrays.stream(Direction.values()).toList());

			Collections.shuffle(directionsFind);
			List<Direction> directions = new ArrayList<>();
			for (var directionNeighbor : directionsFind) {
				if (!(blockEntity instanceof EnergyWireBlockEntity wireBlock && wireBlock.directionOutput.get(directionNeighbor) > 0)) {
					directions.add(directionNeighbor.getOpposite());
				}
			}
			if (!directions.isEmpty()) {
				blockEntity.transferEnergy(blockEntity.findPriority(directions));
			}
		}
	}

	private LinkedHashMap<Direction,EnergyBlockEntity> findPriority(List<Direction> directions) {
		LinkedHashMap<Direction, EnergyBlockEntity> energyBlockEntities = new LinkedHashMap<>();
		for (var direction : directions) {
			BlockEntity blockEntity = this.getWorld().getBlockEntity(this.getPos().offset(direction));
			if (blockEntity instanceof EnergyBlockEntity energyBlock && !(energyBlock instanceof BatteryTestBlockEntity)) {//Потом сделать зарядку аккумулятору
				if (energyBlock instanceof EnergyTestReceiverBlockEntity) {
					energyBlockEntities.putFirst(direction, energyBlock);
				} else {
					energyBlockEntities.putLast(direction, energyBlock);
				}
			}
		}
		return energyBlockEntities;
	}

	@Override
	public void transferEnergy(LinkedHashMap<Direction, EnergyBlockEntity> energyBlockEntities) {
			if (this.getFreakEnergyStored() == 0 || energyBlockEntities.isEmpty()){
				return;
			}

			for (var direction : energyBlockEntities.keySet()) {

				EnergyBlockEntity energyBlock = energyBlockEntities.get(direction);

				int transfer = Math.min(Math.min(transferRate(), this.freakEnergyStored), energyBlock.capacity() - energyBlock.getFreakEnergyStored());

				if (transfer > 0) {
					energyBlock.receiveEnergy(transfer);
					this.freakEnergyStored -= transfer;
				}
				if (energyBlock instanceof EnergyWireBlockEntity wire && !(this instanceof EnergyTestReceiverBlockEntity)) {
					wire.directionOutput.put(direction, 15);
				}
			}
	}

	public void receiveEnergy(int freakEnergy) {
		int energyReceived = Math.min(capacity() - this.freakEnergyStored, freakEnergy);
			this.freakEnergyStored += energyReceived;
	}

	@Override
	public int capacity() {
		return 40000;
	}

	@Override
	public int transferRate() {
		return 300;
	}

	@Override
	public Type type() {
		return Type.BATTERY;
	}

	@Override
	public int getFreakEnergyStored() {
		return freakEnergyStored;
	}

	@Override
	public void setFreakEnergyStored(int storedEnergy) {
		this.freakEnergyStored = storedEnergy;
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		nbt.putInt("FreakEnergy", this.getFreakEnergyStored());
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		this.setFreakEnergyStored(nbt.getInt("FreakEnergy"));
	}
}
