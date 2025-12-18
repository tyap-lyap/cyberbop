package tyaplyap.cyberbop.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import tyaplyap.cyberbop.item.CyberbopItems;

import static tyaplyap.cyberbop.screen.AssemblerScreenHandler.EXTRA_MODULE_SLOTS;

public class ScreenUtil {
	public static boolean isUnlockExtraModule(Inventory inventory) {
		return inventory.getStack(0).isIn(CyberbopItems.UNLOCKS_MODULE_SLOT) &&
			inventory.getStack(1).isIn(CyberbopItems.UNLOCKS_MODULE_SLOT) &&
			inventory.getStack(2).isIn(CyberbopItems.UNLOCKS_MODULE_SLOT) &&
			inventory.getStack(3).isIn(CyberbopItems.UNLOCKS_MODULE_SLOT) &&
			inventory.getStack(4).isIn(CyberbopItems.UNLOCKS_MODULE_SLOT) &&
			inventory.getStack(5).isIn(CyberbopItems.UNLOCKS_MODULE_SLOT);
	}

	public static boolean isUnlockExtraModule(DefaultedList<ItemStack> inventory) {
		return inventory.get(0).isIn(CyberbopItems.UNLOCKS_MODULE_SLOT) &&
			inventory.get(1).isIn(CyberbopItems.UNLOCKS_MODULE_SLOT) &&
			inventory.get(2).isIn(CyberbopItems.UNLOCKS_MODULE_SLOT) &&
			inventory.get(3).isIn(CyberbopItems.UNLOCKS_MODULE_SLOT) &&
			inventory.get(4).isIn(CyberbopItems.UNLOCKS_MODULE_SLOT) &&
			inventory.get(5).isIn(CyberbopItems.UNLOCKS_MODULE_SLOT);
	}

	public static boolean haveExtraModuleStack(Inventory inventory) {
		for (var index : EXTRA_MODULE_SLOTS) {
			if (!inventory.getStack(index).isEmpty()) {
				return true;
			}
		}
		return false;
	}
}
