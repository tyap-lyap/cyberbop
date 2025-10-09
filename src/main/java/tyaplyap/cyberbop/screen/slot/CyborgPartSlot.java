package tyaplyap.cyberbop.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import tyaplyap.cyberbop.item.CyborgPartItem;
import tyaplyap.cyberbop.util.CyborgPartType;

public class CyborgPartSlot extends Slot {

	private final CyborgPartType partType;

	public CyborgPartSlot(Inventory inventory, int index, int x, int y, CyborgPartType partType) {
		super(inventory, index, x, y);
		this.partType = partType;
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return (stack.getItem() instanceof CyborgPartItem partItem && partItem.getPartName(partType) != null);
	}

	@Override
	public int getMaxItemCount(ItemStack stack) {
		return 1;
	}
}

