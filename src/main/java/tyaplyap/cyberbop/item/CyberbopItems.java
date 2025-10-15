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

	public static final Item FLIGHT_MODULE = add("flight_module", new FlightModule(new Item.Settings().maxCount(1)));
	public static final Item EXTRA_BATTERY_MODULE = add("extra_battery_module", new ExtraBatteryModule(new Item.Settings().maxCount(1)));
	public static final Item BATTERY = add("battery", new Item(new Item.Settings()));
	public static final Item JETPACK_MODULE = add("jetpack_module", new JetpackModule(new Item.Settings().maxCount(1)));
	public static final Item SOLAR_CELL_MODULE = add("solar_cell_module", new SolarCellModule(new Item.Settings().maxCount(1)));
	public static final Item SOLAR_CELL = add("solar_cell", new Item(new Item.Settings()));
	public static final Item NIGHT_VISION_MODULE = add("night_vision_module", new NightVisionModule(new Item.Settings().maxCount(1)));
	public static final Item XRAY_VISION_MODULE = add("xray_vision_module", new XrayVisionModule(new Item.Settings().maxCount(1)));

	public static final Item BASIC_HEAD = add("basic_cyborg_head", new CyborgHeadPartItem("basic_head", 4000, 2, new Item.Settings().maxCount(1)));
	public static final Item BASIC_BODY = add("basic_cyborg_body", new CyborgBodyPartItem("basic_body", 4000, 2, new Item.Settings().maxCount(1)));
	public static final Item BASIC_ARM = add("basic_cyborg_arm", new CyborgArmPartItem("basic_right_arm", "basic_left_arm", 4000, 2, new Item.Settings().maxCount(1)));
	public static final Item BASIC_LEG = add("basic_cyborg_leg", new CyborgLegPartItem("basic_right_leg", "basic_left_leg", 4000, 2, new Item.Settings().maxCount(1)));

	public static final Item GOLDEN_HEAD = add("golden_cyborg_head", new CyborgHeadPartItem("golden_head", 5300, 4, new Item.Settings().maxCount(1)));
	public static final Item GOLDEN_BODY = add("golden_cyborg_body", new CyborgBodyPartItem("golden_body", 5300, 4, new Item.Settings().maxCount(1)));
	public static final Item GOLDEN_ARM = add("golden_cyborg_arm", new CyborgArmPartItem("golden_right_arm", "golden_left_arm", 5300, 4, new Item.Settings().maxCount(1)));
	public static final Item GOLDEN_LEG = add("golden_cyborg_leg", new CyborgLegPartItem("golden_right_leg", "golden_left_leg", 5300, 4, new Item.Settings().maxCount(1)));

	public static final Item ADVANCED_HEAD = add("advanced_cyborg_head", new CyborgHeadPartItem("advanced_head", 9350, 6, new Item.Settings().maxCount(1)));
	public static final Item ADVANCED_BODY = add("advanced_cyborg_body", new CyborgBodyPartItem("advanced_body", 9350, 6, new Item.Settings().maxCount(1)));
	public static final Item ADVANCED_ARM = add("advanced_cyborg_arm", new CyborgArmPartItem("advanced_right_arm", "advanced_left_arm", 9350, 6, new Item.Settings().maxCount(1)));
	public static final Item ADVANCED_LEG = add("advanced_cyborg_leg", new CyborgLegPartItem("advanced_right_leg", "advanced_left_leg", 9350, 6, new Item.Settings().maxCount(1)));

	public static final Item DEBUG_ENERGY_STICK = add("debug_energy_stick", new DebugEnergyStick(new Item.Settings().maxCount(1)));

	public static void init() {
		ITEMS.forEach((id, item) -> Registry.register(Registries.ITEM, id, item));
	}

	private static Item add(String name, Item item) {
		ITEMS.put(CyberbopMod.id(name), item);
		return item;
	}
}
