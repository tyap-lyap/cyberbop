package tyaplyap.cyberbop.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import tyaplyap.cyberbop.block.entity.IEnergy;

import java.util.Optional;

public class DebugEnergyStick extends Item {

	public DebugEnergyStick(Settings settings) {
		super(settings);
	}

	private static void sendMessage(PlayerEntity player, Text message) {
		((ServerPlayerEntity)player).sendMessageToClient(message, true);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockPos blockPos = context.getBlockPos();
		World world = context.getWorld();
		if (context.getWorld().getBlockEntity(context.getBlockPos()) instanceof IEnergy) {
			world.playSound(null, blockPos, SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
			ItemStack itemStack = context.getStack();

			LodestoneTrackerComponent lodestoneTrackerComponent = new LodestoneTrackerComponent(Optional.of(GlobalPos.create(world.getRegistryKey(), blockPos)), true);

			itemStack.set(DataComponentTypes.LODESTONE_TRACKER, lodestoneTrackerComponent);

			return ActionResult.success(world.isClient);
		} else {
			return super.useOnBlock(context);
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!world.isClient) {
			LodestoneTrackerComponent lodestoneTrackerComponent = stack.get(DataComponentTypes.LODESTONE_TRACKER);
			if (lodestoneTrackerComponent != null && world.getBlockEntity(lodestoneTrackerComponent.target().get().pos()) instanceof IEnergy energy && entity instanceof ServerPlayerEntity player) {
				sendMessage(player, Text.of(energy.type().toString() + "§a Stored:" + energy.getEnergyStored()));
			}
		}
	}
}
