package tyaplyap.cyberbop.item.parts;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tyaplyap.cyberbop.item.CyborgHeadPartItem;

import java.util.List;

public class AdvancedCyborgHead extends CyborgHeadPartItem {

	public AdvancedCyborgHead(String partName, int energyCapacity, double health, Settings settings) {
		super(partName, energyCapacity, health, settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		super.appendTooltip(stack, context, tooltip, type);
		tooltip.add(Text.literal("").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("Full Set Bonus: +§61 §7Module slot").formatted(Formatting.GRAY));
	}
}
