package tyaplyap.cyberbop.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import tyaplyap.cyberbop.CyberbopMod;

public class CyberbopBlocks {


	public static void init() {
		Registry.register(Registries.BLOCK, CyberbopMod.id("controller"), new ControllerBlock(AbstractBlock.Settings.create()));
		Registry.register(Registries.BLOCK, CyberbopMod.id("assembler"), new Block(AbstractBlock.Settings.create()));

		Registry.register(Registries.ITEM, CyberbopMod.id("controller"), new BlockItem(Registries.BLOCK.get(CyberbopMod.id("controller")), new Item.Settings()));
		Registry.register(Registries.ITEM, CyberbopMod.id("assembler"), new BlockItem(Registries.BLOCK.get(CyberbopMod.id("assembler")), new Item.Settings()));
	}
}
