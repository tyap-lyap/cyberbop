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


public abstract class EnergyBlockEntity extends BlockEntity implements IEnergy {

	private int energyStored;

	public EnergyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static void tick (World world, BlockPos pos, BlockState state, EnergyBlockEntity blockEntity) {
		if (!blockEntity.type().equals(Type.RECEIVER) && blockEntity.getEnergyStored() != 0) {

			List<Direction> directionsFind = blockEntity.getRandomDirections();

			List<Direction> directions = new ArrayList<>();
			for (var directionNeighbor : directionsFind) {
				if (!(blockEntity instanceof EnergyWireBlockEntity wireBlock && wireBlock.directionOutput.get(directionNeighbor).getTimer() > 0)) {
					directions.add(directionNeighbor.getOpposite());
				}
			}
			if (!directions.isEmpty()) {
				blockEntity.transferLogic(blockEntity.findPriority(directions));
			}
		}
	}

	public List<Direction> getRandomDirections() {
		List<Direction> directions = new ArrayList<>(Arrays.stream(Direction.values()).toList());
		Collections.shuffle(directions);
		return directions;
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

	public boolean isFull(){
		return this.capacity() == this.getEnergyStored() || this.capacity() < this.getEnergyStored();
	}

//НЕ ЗАБЫТЬ ПЕРЕПИСАТЬ!!!!!!!!!!!!!!!!!!
	public void balanceEnergy(EnergyWireBlockEntity wireBlock) {
		if (!wireBlock.isFull() && (this.type().equals(Type.GENERATOR) || this.type().equals(Type.BATTERY))) {
			List<EnergyBlockEntity> sourceNeighbors = new ArrayList<>();
			for (var direction : Direction.values()) {
				BlockEntity neighbor = this.getWorld().getBlockEntity(wireBlock.getPos().offset(direction));
				if (neighbor instanceof EnergyBlockEntity energyBlockEntity && energyBlockEntity.getEnergyStored() != 0 && (energyBlockEntity.type().equals(Type.BATTERY) || energyBlockEntity.type().equals(Type.GENERATOR))) {
					sourceNeighbors.add(energyBlockEntity);
				}
			}
			if (!sourceNeighbors.isEmpty()) {
				int needEnergy = (wireBlock.capacity() - wireBlock.getEnergyStored()) / sourceNeighbors.size();
				if (needEnergy == 0) {
					if (transferEnergy(transferRate(), wireBlock)) markDirty();
				} else {
					for (var neighbor : sourceNeighbors) {
						if (neighbor.transferEnergy(transferRate(), wireBlock)) markDirty();
					}
				}
			}
		}
	}

	public void transferLogic(LinkedHashMap<Direction, EnergyBlockEntity> energyBlockEntities) {
		if (this.getEnergyStored() == 0 || energyBlockEntities.isEmpty()) {
			return;
		}
		for (var direction : energyBlockEntities.keySet()) {

			EnergyBlockEntity energyBlock = energyBlockEntities.get(direction);

			if (energyBlock instanceof EnergyWireBlockEntity wire && (this.type().equals(Type.GENERATOR) || this.type().equals(Type.BATTERY))) {
				this.balanceEnergy(wire);
			} else {
				if (!energyBlock.type().equals(Type.GENERATOR) && !energyBlock.type().equals(Type.BATTERY)) {
					if (transferEnergy(transferRate(), energyBlock)) markDirty();
				}
			}
			if (energyBlock instanceof EnergyWireBlockEntity wire && !(this instanceof EnergyTestReceiverBlockEntity)) {
				wire.directionOutput.get(direction).setTimer(15);
				if (this.type().equals(Type.GENERATOR)) {
					wire.directionOutput.get(direction).setSource("test");
				}
			}
		}
	}



	@Override
	public int getEnergyStored() {
		return energyStored;
	}

	@Override
	public void setEnergyStored(int storedEnergy) {
		this.energyStored = storedEnergy;
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		nbt.putInt("EnergyStored", this.getEnergyStored());
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		this.setEnergyStored(nbt.getInt("EnergyStored"));
	}
}
