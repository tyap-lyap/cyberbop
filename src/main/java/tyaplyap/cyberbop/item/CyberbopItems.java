package tyaplyap.cyberbop.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import tyaplyap.cyberbop.CyberbopMod;

public class CyberbopItems {

	public static final Item ADVANCED_HEAD = Registry.register(Registries.ITEM, CyberbopMod.id("advanced_head"), new CyborgPartItem("advanced_head", new Item.Settings().maxCount(1)));
	public static final Item ADVANCED_ARM = Registry.register(Registries.ITEM, CyberbopMod.id("advanced_arm"), new CyborgPartItem("advanced_arm", new Item.Settings().maxCount(1)));
	public static final Item DEBUG_ENERGY_STICK = Registry.register(Registries.ITEM, CyberbopMod.id("debug_energy_stick"), new DebugEnergyStick(new Item.Settings().maxCount(1)));

	public static void init() {

	}
}
