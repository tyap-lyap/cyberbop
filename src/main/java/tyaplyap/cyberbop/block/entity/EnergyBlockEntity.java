package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;
import tyaplyap.cyberbop.util.DebugUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public abstract class EnergyBlockEntity extends BlockEntity  {
//todo СДЕЛАТЬ СОХРАНЕНИЕ ЭНЕРГИИ ПРИ ЛОМАНИИИ!!!!!!!!!!!!!!!!!!!ё
public final EnergyStorage energyStorage = new EnergyStorage() {
	@Override
	public boolean canInsert(EnergyStorage source) {
		return canInsertEnergy(source);
	}

	@Override
	public boolean canExtract(EnergyStorage target) {
		return canExtractEnergy(target);
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

	abstract boolean canInsertEnergy(EnergyStorage source);

	abstract boolean canExtractEnergy(EnergyStorage target);

	public boolean isFull() {
		return getEnergyStored() >= getCapacity();
	}

	public void setEnergyStored(int energy) {
		energyStorage.storedEnergy = energy;
	}

	public int getEnergyStored() {
		return energyStorage.getEnergy();
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		NbtCompound nbtCompound = super.toInitialChunkDataNbt(registryLookup);
		this.writeNbt(nbtCompound, registryLookup);
		return nbtCompound;
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
			EnergyStorage energyStorageTEST;
			List<Direction> directions = new ArrayList<>(Arrays.stream(Direction.values()).toList());
			Collections.shuffle(directions);
			for (var direction : directions) {
				energyStorageTEST = IEnergyStorage.SIDED.find(world, pos.offset(direction), direction.getOpposite());
				EnergyStorage.transfer(blockEntity.energyStorage, energyStorageTEST, blockEntity.getTransferRate());
			}
		}
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
