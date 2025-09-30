package tyaplyap.cyberbop.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import tyaplyap.cyberbop.CyberbopMod;

import java.util.LinkedHashMap;
import java.util.Map;

public class CyberbopItems {
	public static final Map<Identifier, Item> ITEMS = new LinkedHashMap<>();

	public static final Item FLIGHT_MODULE = add("flight_module", new Item(new Item.Settings().maxCount(1)));
	public static final Item EXTRA_BATTERY = add("extra_battery", new Item(new Item.Settings().maxCount(1)));
	public static final Item JETPACK = add("jetpack", new Item(new Item.Settings().maxCount(1)));
	public static final Item SOLAR_CELL_MODULE = add("solar_cell_module", new Item(new Item.Settings().maxCount(1)));

	public static final Item BASIC_HEAD = add("basic_cyborg_head", new CyborgPartItem("basic_head", new Item.Settings().maxCount(1)));
	public static final Item BASIC_BODY = add("basic_cyborg_body", new CyborgPartItem("basic_body", new Item.Settings().maxCount(1)));
	public static final Item BASIC_ARM = add("basic_cyborg_arm", new CyborgArmPartItem("basic_right_arm", "basic_left_arm", new Item.Settings().maxCount(1)));
	public static final Item BASIC_LEG = add("basic_cyborg_leg", new CyborgLegPartItem("basic_right_leg", "basic_left_leg", new Item.Settings().maxCount(1)));

	public static final Item ADVANCED_HEAD = add("advanced_head", new CyborgPartItem("advanced_head", new Item.Settings().maxCount(1)));
	public static final Item ADVANCED_BODY = add("advanced_body", new CyborgPartItem("advanced_body", new Item.Settings().maxCount(1)));
	public static final Item ADVANCED_ARM = add("advanced_arm", new CyborgArmPartItem("advanced_right_arm", "advanced_left_arm", new Item.Settings().maxCount(1)));
	public static final Item ADVANCED_LEG = add("advanced_leg", new CyborgLegPartItem("advanced_right_leg", "advanced_left_leg", new Item.Settings().maxCount(1)));

	public static final Item DEBUG_ENERGY_STICK = add("debug_energy_stick", new DebugEnergyStick(new Item.Settings().maxCount(1)));

	public static void init() {
		ITEMS.forEach((id, item) -> Registry.register(Registries.ITEM, id, item));
	}

	private static Item add(String name, Item item) {
		ITEMS.put(CyberbopMod.id(name), item);
		return item;
	}
}
