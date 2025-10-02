package tyaplyap.cyberbop.block.entity;

import net.minecraft.util.math.Direction;

import java.util.LinkedHashMap;;

public interface IEnergy {

	default int capacity() {
		return 1024;
	}

	default int transferRate() {
		return 0;
	}

	default int energyConsumption() {
		return 0;
	}

	default Type type() {
		return Type.BATTERY;
	}

	void setFreakEnergyStored(int energy);

	int getFreakEnergyStored();

	default boolean isReceiver() {
		return type().equals(Type.RECEIVER) || type().equals(Type.WIRE);
	}

	void transferEnergy(LinkedHashMap<Direction, EnergyBlockEntity> energyBlockEntities);

	void receiveEnergy(int freakEnergy);

	enum Type {
		BATTERY(),
		WIRE(),
		RECEIVER(),
		GENERATOR()
	}
}
