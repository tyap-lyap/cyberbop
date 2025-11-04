package tyaplyap.cyberbop.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tyaplyap.cyberbop.extension.PlayerExtension;

import java.util.List;

public class CreativeBatteryModule extends CyborgModuleItem {

	public CreativeBatteryModule(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension extension, ItemStack stack) {
		extension.setEnergyStored(extension.getCapacity());
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Disables any energy consumption.").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("Unobtainable item.").formatted(Formatting.GRAY));
	}
}
