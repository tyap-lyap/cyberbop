package tyaplyap.cyberbop.util.transfer;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.CyberbopMod;

public interface IEnergyStorage {

	int capacity();

	default int transferRate() {
		return 0;
	}

	 BlockApiLookup<EnergyStorage, @Nullable Direction> SIDED =
		BlockApiLookup.get(CyberbopMod.id("energy"), EnergyStorage.class, Direction.class);

	ItemApiLookup<EnergyStorage, ContainerItemContext> ITEM =
		ItemApiLookup.get(CyberbopMod.id("energy"), EnergyStorage.class, ContainerItemContext.class);


	default int energyConsumption() {
		return 0;
	}

	void setEnergy(int energy);

	int getEnergy();

	boolean canInsert(EnergyStorage target);

	boolean canExtract(EnergyStorage source);

	int insert(int transferEnergy, Transaction transaction);

	int extract(int transferEnergy, Transaction transaction);

	Type type();

	enum Type {
		BATTERY(),
		WIRE(),
		RECEIVER(),
		GENERATOR(),
		CYBORG()
	}
}
