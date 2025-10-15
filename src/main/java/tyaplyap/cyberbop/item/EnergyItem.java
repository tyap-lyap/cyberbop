package tyaplyap.cyberbop.item;

import net.minecraft.item.Item;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;

public abstract class EnergyItem extends Item {
	public final EnergyStorage storageTEST = new EnergyStorage() {
		@Override
		public Type type() {
			return Type.BATTERY;
		}

		@Override
		public int transferRate() {
			return 0;
		}

		@Override
		public boolean canInsert(EnergyStorage target) {
			return true;
		}

		@Override
		public boolean canExtract(EnergyStorage source) {
			return true;
		}

		@Override
		public int capacity() {
			return getCapacity();
		}
	};

	abstract int getCapacity();

	public EnergyItem(Settings settings) {
		super(settings);
	}
}
