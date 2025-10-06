package tyaplyap.cyberbop.block.entity;

import net.minecraft.util.math.Direction;

import java.util.LinkedHashMap;

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

	void setEnergyStored(int energy);

	int getEnergyStored();

	default boolean isReceiver() {
		return type().equals(Type.RECEIVER);
	}

	default void transferEnergy(LinkedHashMap<Direction, EnergyBlockEntity> energyBlockEntities) {}

	default void receiveEnergy(int energy) {}

	enum Type {
		BATTERY(),
		WIRE(),
		RECEIVER(),
		GENERATOR()
	}
}
