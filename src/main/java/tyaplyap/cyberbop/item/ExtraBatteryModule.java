package tyaplyap.cyberbop.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ExtraBatteryModule extends CyborgModuleItem {

	public ExtraBatteryModule(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("+32000 Energy Capacity").formatted(Formatting.GRAY));
	}
}
