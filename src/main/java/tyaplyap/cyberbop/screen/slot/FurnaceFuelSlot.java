package tyaplyap.cyberbop.screen.slot;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class FurnaceFuelSlot extends Slot {

	public FurnaceFuelSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return AbstractFurnaceBlockEntity.canUseAsFuel(stack);
	}

	@Override
	public int getMaxItemCount(ItemStack stack) {
		return super.getMaxItemCount(stack);
	}
}

