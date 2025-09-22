package tyaplyap.cyberbop.item;

import net.minecraft.item.Item;

public class CyborgPartItem extends Item {

	public String partName;

	public CyborgPartItem(String partName, Settings settings) {
		super(settings);
		this.partName = partName;
	}
}
