package tyaplyap.cyberbop.util;

import net.minecraft.inventory.Inventory;
import tyaplyap.cyberbop.item.CyberbopItems;

import static tyaplyap.cyberbop.screen.AssemblerScreenHandler.EXTRA_MODULE_SLOTS;

public class ScreenUtil {
	public static boolean isUnlockExtraModule(Inventory inventory) {
		return inventory.getStack(0).isIn(CyberbopItems.SLOT_HEAD_UNLOCK) &&
			inventory.getStack(1).isIn(CyberbopItems.SLOT_BODY_UNLOCK) &&
			inventory.getStack(2).isIn(CyberbopItems.SLOT_ARMS_UNLOCK) &&
			inventory.getStack(3).isIn(CyberbopItems.SLOT_ARMS_UNLOCK) &&
			inventory.getStack(4).isIn(CyberbopItems.SLOT_LEGS_UNLOCK) &&
			inventory.getStack(5).isIn(CyberbopItems.SLOT_LEGS_UNLOCK);
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
