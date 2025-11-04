package tyaplyap.cyberbop.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.LightType;
import tyaplyap.cyberbop.extension.PlayerExtension;

import java.util.List;

public class SolarCellModule extends CyborgModuleItem {

	public SolarCellModule(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension extension, ItemStack stack) {
		if (world.getTimeOfDay() % 24000 < 13000) {
			if(world.getLightLevel(LightType.SKY, player.getBlockPos()) == 15) {
				extension.setEnergyStored(Math.min(extension.getEnergyStored() + (world.isRaining() ? 1 : 2), extension.getCapacity()));
			}
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Charges your battery in sunlight.").formatted(Formatting.GRAY));
	}
}
