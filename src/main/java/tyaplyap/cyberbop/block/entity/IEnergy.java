package tyaplyap.cyberbop.block.entity;

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

	default boolean transferEnergy(int transferEnergy, IEnergy container) {
		int transfer = Math.min(Math.min(transferEnergy, getEnergyStored()), container.capacity() - container.getEnergyStored());
		if (transfer > 0) {
			container.receiveEnergy(transfer);
			setEnergyStored(getEnergyStored() - transfer);
			return true;
		}
		return false;
	}

	default void receiveEnergy(int energy) {
			setEnergyStored(getEnergyStored() + Math.min(capacity() - getEnergyStored(), energy));
	}

	enum Type {
		BATTERY(),
		WIRE(),
		RECEIVER(),
		GENERATOR()
	}
}
