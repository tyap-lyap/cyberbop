package tyaplyap.cyberbop.item;

import net.minecraft.item.Item;
import tyaplyap.cyberbop.block.entity.IEnergy;

public abstract class EnergyItem extends Item implements IEnergy {
	public EnergyItem(Settings settings) {
		super(settings);
	}
}
