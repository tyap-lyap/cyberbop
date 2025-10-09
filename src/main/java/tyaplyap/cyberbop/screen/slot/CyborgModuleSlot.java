package tyaplyap.cyberbop.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import tyaplyap.cyberbop.item.CyborgModuleItem;

public class CyborgModuleSlot extends Slot {
	private final int[] slotsIndex;
	private final int index;

	public CyborgModuleSlot(Inventory inventory, int index, int x, int y, int[] slotsIndex) {
		super(inventory, index, x, y);
		this.slotsIndex = slotsIndex;
		this.index = index;
	}
	@Override
	public boolean canInsert(ItemStack stack) {
		if (stack.getItem() instanceof CyborgModuleItem module) {
			for (var index : slotsIndex) {
				if (index != this.index) {
					if (!inventory.getStack(index).isEmpty() && inventory.getStack(index).getItem() == module) {
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getMaxItemCount(ItemStack stack) {
		return 1;
	}
}
