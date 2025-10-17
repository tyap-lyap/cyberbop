package tyaplyap.cyberbop.util.transfer;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EnergyStorage extends SnapshotParticipant<Integer> implements IEnergyStorage{
	public int storedEnergy;

	public EnergyStorage() {}


	public static int transfer(EnergyStorage source, EnergyStorage target, int transferRate, Type sourceType) {
		if (source != null && target != null && source.canExtract(target, sourceType) && target.canInsert(source, sourceType)) {
			try (Transaction transaction = Transaction.openOuter()) {
				int a = target.insert(Math.min(source.storedEnergy, transferRate), transaction);
				if (a == source.extract(a, transaction)) {

					transaction.commit();
					return a;
				}
			}
		}
	return 0;
	}

	@Override
	public int transferRate() {
		return 0;
	}

	@Override
	protected Integer createSnapshot() {
		return storedEnergy;
	}

	@Override
	protected void readSnapshot(Integer snapshot) {
		storedEnergy = snapshot;
	}

	@Override
	public int capacity() {
		return 0;
	}

	@Override
	public void setEnergy(int energy) {
		storedEnergy = energy;
	}

	@Override
	public int getEnergy() {
		return storedEnergy;
	}

	@Override
	public int insert(int transferEnergy, Transaction transaction) {
		int transfer = Math.min(transferEnergy,capacity() - getEnergy());
		if (transfer > 0) {
			updateSnapshots(transaction);
			setEnergy(getEnergy() + transfer);
			return transfer;
		}
		return 0;
	}

	@Override
	public int extract(int transferEnergy, Transaction transaction) {
		int transfer = Math.min(getEnergy(), transferEnergy);
		if (transfer > 0) {
			updateSnapshots(transaction);
			setEnergy(getEnergy() - transfer);
			return transfer;
		}
		return 0;
	}

    public boolean isFull() {
		return getEnergy() >= capacity();
    }
}
