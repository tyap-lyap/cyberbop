package tyaplyap.cyberbop.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import tyaplyap.cyberbop.extension.PlayerExtension;

import java.util.List;

public class FlightModule extends CyborgModuleItem {

	public FlightModule(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension ex, ItemStack stack) {
		if(ex.isCyborg() && !player.isCreative() && !player.isSpectator()) {
			if(ex.getEnergyStored() > 0) {
				if(!player.getAbilities().allowFlying) {
					player.getAbilities().allowFlying = true;
					player.sendAbilitiesUpdate();
				}
				if(player.getAbilities().flying) {
					ex.setEnergyStored(Math.max(ex.getEnergyStored() - 20, 0));
				}
			}
			else {
				if(player.getAbilities().allowFlying) {
					player.getAbilities().allowFlying = false;
					player.getAbilities().flying = false;
					player.sendAbilitiesUpdate();
				}
			}

		}
	}

	@Override
	public void onModuleRemoved(World world, PlayerEntity player) {
		if(!world.isClient() && !player.isCreative() && !player.isSpectator()) {
			player.getAbilities().allowFlying = false;
			player.getAbilities().flying = false;
			player.sendAbilitiesUpdate();
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Grants creative flight but").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("consumes a lot of energy.").formatted(Formatting.GRAY));
	}
}
