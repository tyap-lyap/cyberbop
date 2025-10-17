package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.util.transfer.BlockEnergyStorage;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;
import tyaplyap.cyberbop.util.DebugUtil;

import java.util.*;


public abstract class EnergyBlockEntity extends BlockEntity  {
//todo СДЕЛАТЬ СОХРАНЕНИЕ ЭНЕРГИИ ПРИ ЛОМАНИИИ!!!!!!!!!!!!!!!!!!!ё
public final BlockEnergyStorage energyStorage = new BlockEnergyStorage() {
	@Override
	public void getDirectionIO(Map<Direction, TypeIO> direction) {
		getDirectionsIO(direction);
	}

	@Override
	public boolean canInsert(EnergyStorage source, Type sourceType) {
		return canInsertEnergy(source, sourceType);
	}

	@Override
	public boolean canExtract(EnergyStorage target, Type sourceType) {
		return canExtractEnergy(target, sourceType);
	}
	@Override
	public Type type() {
		return typeMachine();
	}

	@Override
	public int transferRate() {
		return getTransferRate();
	}

	@Override
	public int capacity() {
		return getCapacity();
	}
};

	public EnergyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	abstract public IEnergyStorage.Type typeMachine();

	abstract public int getTransferRate();

	abstract public int getCapacity();

	abstract boolean canInsertEnergy(EnergyStorage source, IEnergyStorage.Type sourceType);

	abstract boolean canExtractEnergy(EnergyStorage target, IEnergyStorage.Type sourceType);

	public boolean isFull() {
		return getEnergyStored() >= getCapacity();
	}

	public void setEnergyStored(int energy) {
		energyStorage.storedEnergy = energy;
	}

	public int getEnergyStored() {
		return energyStorage.getEnergy();
	}

	abstract void getDirectionsIO(Map<Direction, BlockEnergyStorage.TypeIO> direction);

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		NbtCompound nbtCompound = super.toInitialChunkDataNbt(registryLookup);
		this.writeNbt(nbtCompound, registryLookup);
		return nbtCompound;
	}

	@Override
	protected void readComponents(ComponentsAccess components) {
		super.readComponents(components);
		this.energyStorage.storedEnergy = components.getOrDefault(CyberbopItems.STORED_ENERGY_COMPONENT, 0);
	}

	@Override
	protected void addComponents(ComponentMap.Builder componentMapBuilder) {
		super.addComponents(componentMapBuilder);
		if (this.energyStorage.storedEnergy > 0) {
			componentMapBuilder.add(CyberbopItems.STORED_ENERGY_COMPONENT, this.energyStorage.storedEnergy);
		}
	}

	@Override
	public void removeFromCopiedStackNbt(NbtCompound nbt) {
		nbt.remove("number");
		}

	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	public void updateListeners() {
		this.markDirty();
		this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
	}

	public static void BatteryTick (World world, BlockPos pos, BlockState state, EnergyBlockEntity blockEntity) {
		DebugUtil.updateEnergyDebug(world, pos, state, blockEntity);
		if (blockEntity.typeMachine().equals(IEnergyStorage.Type.BATTERY) || blockEntity.typeMachine().equals(IEnergyStorage.Type.GENERATOR)) {
			BlockEnergyStorage blockEnergyStorage;
			List<Direction> directions = new ArrayList<>(Arrays.stream(Direction.values()).toList());
			Collections.shuffle(directions);
			for (var direction : directions) {
				blockEnergyStorage = BlockEnergyStorage.SIDED.find(world, pos.offset(direction), direction.getOpposite());
				if (blockEnergyStorage != null && blockEntity.canIO(blockEntity.energyStorage, direction, false) && blockEntity.canIO(blockEnergyStorage, direction.getOpposite(), true)) {
						EnergyStorage.transfer(blockEntity.energyStorage, blockEnergyStorage, blockEntity.getTransferRate(), blockEntity.typeMachine());
				}

			}
		}
	}

	public boolean canIO(BlockEnergyStorage energyStorage, Direction direction, boolean input) {
		BlockEnergyStorage.TypeIO type = energyStorage.directionIO.get(direction);

		if (type.equals(BlockEnergyStorage.TypeIO.IO)) return true;

		if (input && type.equals(BlockEnergyStorage.TypeIO.INPUT)) return true;

		if (!input && type.equals(BlockEnergyStorage.TypeIO.OUTPUT)) return true;

		return false;
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		nbt.putInt("EnergyStored", energyStorage.getEnergy());
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		energyStorage.storedEnergy=  nbt.getInt("EnergyStored");
	}
}
