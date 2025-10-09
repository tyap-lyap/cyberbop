package tyaplyap.cyberbop.item;

import tyaplyap.cyberbop.block.entity.IEnergy;

public class ExtraBattery extends EnergyItem {
	public ExtraBattery(Settings settings) {
		super(settings);
	}

	@Override
	public void setEnergyStored(int energy) {
	}

	@Override
	public int getEnergyStored() {
		return 0;
	}
}
