package tyaplyap.cyberbop.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import tyaplyap.cyberbop.block.entity.EnergyBlockEntity;

public class DebugEnergyStick extends Item {

	EnergyBlockEntity energyBlock = null;

	public DebugEnergyStick(Settings settings) {
		super(settings);
	}

	private static void sendMessage(PlayerEntity player, Text message) {
		((ServerPlayerEntity)player).sendMessageToClient(message, true);
	}
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.getWorld().getBlockEntity(context.getBlockPos()) instanceof EnergyBlockEntity blockEntity) {
			energyBlock = blockEntity;

		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		if ( energyBlock != null && entity instanceof ServerPlayerEntity player && !world.isClient) {
			sendMessage(player, Text.of(energyBlock.type().toString() + "§a Stored:" + energyBlock.getFreakEnergyStored()));
			if (world.getBlockEntity(energyBlock.getPos()) == null) {
				energyBlock = null;
			}
		}
	}
}
