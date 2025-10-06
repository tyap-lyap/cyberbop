package tyaplyap.cyberbop.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import tyaplyap.cyberbop.block.entity.IEnergy;

public class DebugEnergyStick extends Item {

	BlockEntity energyBlock = null;

	public DebugEnergyStick(Settings settings) {
		super(settings);
	}

	private static void sendMessage(PlayerEntity player, Text message) {
		((ServerPlayerEntity)player).sendMessageToClient(message, true);
	}
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.getWorld().getBlockEntity(context.getBlockPos()) instanceof IEnergy) {
			energyBlock = context.getWorld().getBlockEntity(context.getBlockPos());

		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if ( energyBlock != null && entity instanceof ServerPlayerEntity player && !world.isClient && energyBlock instanceof IEnergy energy) {
			sendMessage(player, Text.of(energy.type().toString() + "§a Stored:" + energy.getEnergyStored()));
			if (world.getBlockEntity(energyBlock.getPos()) == null) {
				energyBlock = null;
			}
		}
	}
}
