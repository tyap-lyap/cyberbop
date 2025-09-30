package tyaplyap.cyberbop.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import tyaplyap.cyberbop.CyberbopMod;

public class CyberbopBlocks {

	public static final Block CONTROLLER = Registry.register(Registries.BLOCK, CyberbopMod.id("controller"), new ControllerBlock(AbstractBlock.Settings.create()));
	public static final Block ASSEMBLER = Registry.register(Registries.BLOCK, CyberbopMod.id("assembler"), new Block(AbstractBlock.Settings.create()));
	//test
	public static final Block ENERGY_GENERATOR = new EnergyGeneratorBlock(AbstractBlock.Settings.create().strength(3.0f, 6.0f).sounds(BlockSoundGroup.METAL));
	public static final Block ENERGY_WIRE = new EnergyWireBlock(AbstractBlock.Settings.create().strength(1.0f, 3.0f).sounds(BlockSoundGroup.METAL).nonOpaque());
	public static final Block ENERGY_RECEIVER = new EnergyReceiverBlock(AbstractBlock.Settings.create().strength(2.0f, 4.0f).sounds(BlockSoundGroup.METAL));
	public static final Block BATTERY_TEST = new BatteryTestBlock(AbstractBlock.Settings.create().strength(2.0f, 4.0f).sounds(BlockSoundGroup.METAL));

	public static void init() {
		Registry.register(Registries.ITEM, CyberbopMod.id("controller"), new BlockItem(CONTROLLER, new Item.Settings()));
		Registry.register(Registries.ITEM, CyberbopMod.id("assembler"), new BlockItem(ASSEMBLER, new Item.Settings()));

		//test
		Registry.register(Registries.BLOCK, CyberbopMod.id("energy_generator"), ENERGY_GENERATOR);
		Registry.register(Registries.BLOCK, CyberbopMod.id("energy_wire"), ENERGY_WIRE);
		Registry.register(Registries.ITEM, CyberbopMod.id("energy_generator"), new BlockItem(ENERGY_GENERATOR, new Item.Settings()));
		Registry.register(Registries.ITEM, CyberbopMod.id("energy_wire"), new BlockItem(ENERGY_WIRE, new Item.Settings()));
		Registry.register(Registries.BLOCK, CyberbopMod.id("energy_receiver"), ENERGY_RECEIVER);
		Registry.register(Registries.ITEM, CyberbopMod.id("energy_receiver"), new BlockItem(ENERGY_RECEIVER, new Item.Settings()));
		Registry.register(Registries.BLOCK, CyberbopMod.id("battery_test"), BATTERY_TEST);
		Registry.register(Registries.ITEM, CyberbopMod.id("battery_test"), new BlockItem(BATTERY_TEST, new Item.Settings()));
	}
}
