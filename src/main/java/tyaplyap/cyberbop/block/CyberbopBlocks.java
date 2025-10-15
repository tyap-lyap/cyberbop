package tyaplyap.cyberbop.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import tyaplyap.cyberbop.CyberbopMod;

import java.util.LinkedHashMap;
import java.util.Map;

public class CyberbopBlocks {

	public static final Map<Identifier, BlockItem> ITEMS = new LinkedHashMap<>();
	public static final Map<Identifier, Block> BLOCKS = new LinkedHashMap<>();

	public static final Block CONTROLLER = add("controller", new ControllerBlock(AbstractBlock.Settings.create().requiresTool().strength(2.0f, 4.0f)));
	public static final Block ASSEMBLER = add("assembler", new AssemblerBlock(AbstractBlock.Settings.create().requiresTool().strength(2.0f, 4.0f).nonOpaque()));

	public static final Block ENERGY_WIRE = add("energy_wire", new EnergyWireBlock(AbstractBlock.Settings.create().strength(1.0f, 3.0f).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static final Block SOLAR_PANEL = add("solar_panel", new SolarPanelBlock(16000, 8, AbstractBlock.Settings.create().requiresTool().strength(2.0f, 4.0f).sounds(BlockSoundGroup.METAL)));
	public static final Block ADVANCED_SOLAR_PANEL = add("advanced_solar_panel", new SolarPanelBlock(32000, 24, AbstractBlock.Settings.create().requiresTool().strength(2.0f, 4.0f).sounds(BlockSoundGroup.METAL)));
	public static final Block CHARGING_PAD = add("charging_pad", new ChargingPadBlock(AbstractBlock.Settings.create().requiresTool().strength(2.0f, 4.0f).sounds(BlockSoundGroup.METAL)));
	public static final Block FURNACE_GENERATOR = add("furnace_generator", new FurnaceGeneratorBlock(AbstractBlock.Settings.create().requiresTool().strength(2.0f, 4.0f).sounds(BlockSoundGroup.METAL)));

	//Debug
	public static final Block ENERGY_RECEIVER = add("energy_receiver", new EnergyReceiverBlock(AbstractBlock.Settings.create().strength(2.0f, 4.0f).sounds(BlockSoundGroup.METAL)));
	public static final Block ENERGY_GENERATOR = add("energy_generator", new EnergyGeneratorBlock(AbstractBlock.Settings.create().strength(3.0f, 6.0f).sounds(BlockSoundGroup.METAL)));
	public static final Block BATTERY_TEST = add("battery_test", new BatteryTestBlock(AbstractBlock.Settings.create().strength(2.0f, 4.0f).sounds(BlockSoundGroup.METAL)));

	public static void init() {
		ITEMS.forEach((id, item) -> Registry.register(Registries.ITEM, id, item));
		BLOCKS.forEach((id, block) -> Registry.register(Registries.BLOCK, id, block));
	}

	public static Block add(String name, Block block) {
		Item.Settings settings = new Item.Settings();
		return addBlockItem(name, block, new BlockItem(block, settings));
	}

	public static Block addBlockItem(String name, Block block, BlockItem item) {
		addBlock(name, block);
		if (item != null) {
			item.appendBlocks(Item.BLOCK_ITEMS, item);
			ITEMS.put(CyberbopMod.id(name), item);
		}
		return block;
	}

	public static Block addBlock(String name, Block block) {
		BLOCKS.put(CyberbopMod.id(name), block);
		return block;
	}
}
