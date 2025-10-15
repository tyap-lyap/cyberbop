package tyaplyap.cyberbop.item;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tyaplyap.cyberbop.client.util.ClientJetpackHandler;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.util.JetpackUseTracker;

import java.util.List;

public class JetpackModule extends CyborgModuleItem {

	public JetpackModule(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension ex) {
		if (ex.isCyborg() && ex.containsModule(CyberbopItems.JETPACK_MODULE) && !ex.containsModule(CyberbopItems.FLIGHT_MODULE) && !player.isCreative() && !player.isSpectator()) {

			if (JetpackUseTracker.usesJetpack(player.getUuid())) {
				if (ex.getEnergyStored() > 0) {
					ex.setEnergyStored(Math.max(ex.getEnergyStored() - 4, 0));
					player.fallDistance = 0;
				}
			}
		}
	}

	@Override
	public void clientTick(ClientWorld world, PlayerEntity player, PlayerExtension extension) {
		ClientJetpackHandler.tick();
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Double jump to start ascending").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("and hold to continue flying.").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("Consumes 4/t of energy.").formatted(Formatting.GRAY));
	}
}
