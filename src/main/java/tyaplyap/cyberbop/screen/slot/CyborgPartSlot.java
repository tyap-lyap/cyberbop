package tyaplyap.cyberbop.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import tyaplyap.cyberbop.item.CyborgArmPartItem;
import tyaplyap.cyberbop.item.CyborgLegPartItem;
import tyaplyap.cyberbop.item.CyborgPartItem;

public class CyborgPartSlot extends Slot {

	private final String partName;

	public CyborgPartSlot(Inventory inventory, int index, int x, int y, String partName) {
		super(inventory, index, x, y);
		this.partName = partName;
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return true;
//		Item cyborgPart = stack.getItem();
//		if (cyborgPart instanceof CyborgPartItem part && part.partName.contains(this.partName)) {
//			return true;
//		} else if (cyborgPart instanceof CyborgArmPartItem part && (part.right.contains(this.partName) || part.left.contains(this.partName))) {
//			return true;
//		} else return cyborgPart instanceof CyborgLegPartItem part && (part.right.contains(this.partName) || part.left.contains(this.partName));
	}

	@Override
	public int getMaxItemCount(ItemStack stack) {
		return 1;
	}
}

