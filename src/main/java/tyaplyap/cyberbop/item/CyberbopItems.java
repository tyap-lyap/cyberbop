package tyaplyap.cyberbop.item;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.GlobalPos;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.CyberbopBlocks;
import tyaplyap.cyberbop.item.parts.AdvancedCyborgArm;
import tyaplyap.cyberbop.item.parts.AdvancedCyborgBody;
import tyaplyap.cyberbop.item.parts.AdvancedCyborgHead;
import tyaplyap.cyberbop.item.parts.AdvancedCyborgLeg;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CyberbopItems {
	public static final Map<Identifier, Item> ITEMS = new LinkedHashMap<>();

	public static final ComponentType<Integer> STORED_ENERGY_COMPONENT = Registry.register(
		Registries.DATA_COMPONENT_TYPE,
		Identifier.of(CyberbopMod.MOD_ID, "energy_stored"),
		ComponentType.<Integer>builder().codec(Codec.INT).build()
	);

	public static final ComponentType<GlobalPos> RETREAT_MODULE_COMPONENT = Registry.register(
		Registries.DATA_COMPONENT_TYPE,
		CyberbopMod.id("retreat_module_pos"),
		ComponentType.<GlobalPos>builder().codec(GlobalPos.CODEC).build()
	);

	public static final ComponentType<Boolean> RETREAT_MODULE_STATUS_COMPONENT = Registry.register(
		Registries.DATA_COMPONENT_TYPE,
		CyberbopMod.id("retreat_module_status"),
		ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
	);

	public static final TagKey<Item> SLOT_HEAD_UNLOCK = TagKey.of(RegistryKeys.ITEM, CyberbopMod.id("extra_slot_head_unlock"));
	public static final TagKey<Item> SLOT_BODY_UNLOCK = TagKey.of(RegistryKeys.ITEM, CyberbopMod.id("extra_slot_body_unlock"));
	public static final TagKey<Item> SLOT_ARMS_UNLOCK = TagKey.of(RegistryKeys.ITEM, CyberbopMod.id("extra_slot_arms_unlock"));
	public static final TagKey<Item> SLOT_LEGS_UNLOCK = TagKey.of(RegistryKeys.ITEM, CyberbopMod.id("extra_slot_legs_unlock"));

	public static final Item FLIGHT_MODULE = add("flight_module", new FlightModule(new Item.Settings().maxCount(1).rarity(Rarity.RARE).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)));
	public static final Item CREATIVE_BATTERY_MODULE = add("creative_battery_module", new CreativeBatteryModule(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));
	public static final Item LARGE_BATTERY_MODULE = add("large_battery_module", new BatteryModule(128000, new Item.Settings().maxCount(1)));
	public static final Item EXTRA_BATTERY_MODULE = add("extra_battery_module", new BatteryModule(32000, new Item.Settings().maxCount(1)));
	public static final Item BATTERY = add("battery", new Item(new Item.Settings()));
	public static final Item JETPACK_MODULE = add("jetpack_module", new JetpackModule(new Item.Settings().maxCount(1)));
	public static final Item SOLAR_CELL_MODULE = add("solar_cell_module", new SolarCellModule(new Item.Settings().maxCount(1)));
	public static final Item SOLAR_CELL = add("solar_cell", new Item(new Item.Settings()));
	public static final Item NIGHT_VISION_MODULE = add("night_vision_module", new NightVisionModule(new Item.Settings().maxCount(1)));
	public static final Item XRAY_VISION_MODULE = add("xray_vision_module", new XrayVisionModule(new Item.Settings().maxCount(1)));
	public static final Item RETREAT_MODULE = add("retreat_module", new RetreatModule(new Item.Settings().maxCount(1)));
	public static final Item EXTRA_HEALTH_MODULE = add("extra_health_module", new ExtendedHealthModule(new Item.Settings().maxCount(1)));
	public static final Item MINING_GAUNTLETS_MODULE = add("mining_gauntlets_module", new MiningGauntletsModule(new Item.Settings().maxCount(1)));

	public static final Item BASIC_HEAD = add("basic_cyborg_head", new CyborgHeadPartItem("basic_head", 5000, 3, new Item.Settings().maxCount(1)));
	public static final Item BASIC_BODY = add("basic_cyborg_body", new CyborgBodyPartItem("basic_body", 5000, 5, new Item.Settings().maxCount(1)));
	public static final Item BASIC_ARM = add("basic_cyborg_arm", new CyborgArmPartItem("basic_right_arm", "basic_left_arm", 3500, 1, new Item.Settings().maxCount(1)));
	public static final Item BASIC_LEG = add("basic_cyborg_leg", new CyborgLegPartItem("basic_right_leg", "basic_left_leg", 3500, 1, new Item.Settings().maxCount(1)));

	public static final Item GOLDEN_HEAD = add("golden_cyborg_head", new CyborgHeadPartItem("golden_head", 8000, 4, new Item.Settings().maxCount(1)));
	public static final Item GOLDEN_BODY = add("golden_cyborg_body", new CyborgBodyPartItem("golden_body", 8000, 6, new Item.Settings().maxCount(1)));
	public static final Item GOLDEN_ARM = add("golden_cyborg_arm", new CyborgArmPartItem("golden_right_arm", "golden_left_arm", 5000, 1.5, new Item.Settings().maxCount(1)));
	public static final Item GOLDEN_LEG = add("golden_cyborg_leg", new CyborgLegPartItem("golden_right_leg", "golden_left_leg", 5000, 1.5, new Item.Settings().maxCount(1)));

	public static final Item ADVANCED_HEAD = add("advanced_cyborg_head", new AdvancedCyborgHead("advanced_head", 11350, 7, new Item.Settings().maxCount(1)));
	public static final Item ADVANCED_BODY = add("advanced_cyborg_body", new AdvancedCyborgBody("advanced_body", 11350, 7, new Item.Settings().maxCount(1)));
	public static final Item ADVANCED_ARM = add("advanced_cyborg_arm", new AdvancedCyborgArm("advanced_right_arm", "advanced_left_arm", 7350, 4, new Item.Settings().maxCount(1)));
	public static final Item ADVANCED_LEG = add("advanced_cyborg_leg", new AdvancedCyborgLeg("advanced_right_leg", "advanced_left_leg", 7350, 4, new Item.Settings().maxCount(1)));

	public static final Item CHRONOSTEEL_INGOT = add("chronosteel_ingot", new Item(new Item.Settings()));

	public static final Item DEBUG_ENERGY_STICK = add("debug_energy_stick", new DebugEnergyStick(new Item.Settings().maxCount(1)));


	static ArrayList<Item> debugItems = new ArrayList<>(List.of(CyberbopItems.DEBUG_ENERGY_STICK, CyberbopBlocks.BATTERY_TEST.asItem(), CyberbopBlocks.ENERGY_RECEIVER.asItem(), CyberbopBlocks.ENERGY_GENERATOR.asItem()));

	public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
		.displayName(Text.translatable("itemGroup.cyberbop.items"))
		.entries((ctx, entries) -> {
			CyberbopItems.ITEMS.forEach((id, item) -> {
				if(!debugItems.contains(item)) entries.add(item.getDefaultStack());
			});
			CyberbopBlocks.ITEMS.forEach((id, item) -> {
				if(!debugItems.contains(item)) entries.add(item.getDefaultStack());
			});
		})
		.icon(CyberbopItems.ADVANCED_HEAD::getDefaultStack).build();

	public static final ItemGroup DEBUG_ITEM_GROUP = FabricItemGroup.builder()
		.displayName(Text.translatable("itemGroup.cyberbop.debug_items"))
		.entries((ctx, entries) -> {
			debugItems.forEach(item -> entries.add(item.getDefaultStack()));
		})
		.icon(CyberbopItems.DEBUG_ENERGY_STICK::getDefaultStack).build();

	public static void init() {
		Registry.register(Registries.ITEM_GROUP, CyberbopMod.id("items"), ITEM_GROUP);

		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			Registry.register(Registries.ITEM_GROUP, CyberbopMod.id("debug_items"), DEBUG_ITEM_GROUP);
		}

		ITEMS.forEach((id, item) -> Registry.register(Registries.ITEM, id, item));
	}

	private static Item add(String name, Item item) {
		ITEMS.put(CyberbopMod.id(name), item);
		return item;
	}
}
