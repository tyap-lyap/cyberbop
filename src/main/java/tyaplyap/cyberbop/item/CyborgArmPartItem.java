package tyaplyap.cyberbop.item;

import net.minecraft.item.Item;

public class CyborgArmPartItem extends Item {

	public String right;
	public String left;

	public CyborgArmPartItem(String right, String left, Settings settings) {
		super(settings);
		this.right = right;
		this.left = left;
	}
}
