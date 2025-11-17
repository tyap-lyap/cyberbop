package tyaplyap.cyberbop.item.parts;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tyaplyap.cyberbop.item.CyborgLegPartItem;

import java.util.List;

public class AdvancedCyborgLeg extends CyborgLegPartItem {

	public AdvancedCyborgLeg(String right, String left, int energyCapacity, double health, Settings settings) {
		super(right, left, energyCapacity, health, settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		super.appendTooltip(stack, context, tooltip, type);
		tooltip.add(Text.literal("").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("Full Set Bonus: +1 Module slot").formatted(Formatting.GRAY));
	}
}
