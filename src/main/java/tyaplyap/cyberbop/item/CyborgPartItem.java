package tyaplyap.cyberbop.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import tyaplyap.cyberbop.util.CyborgPartType;

import java.util.List;

public abstract class CyborgPartItem extends EnergyItem {

	String partName;
	int energyCapacity;
	double health;

	public CyborgPartItem(String partName, int energyCapacity, double health, Settings settings) {
		super(settings);
		this.partName = partName;
		this.energyCapacity = energyCapacity;
		this.health = health;
	}

	public String getPartName(CyborgPartType partType) {
		return partName;
	}

	public double getHealth() {
		return health;
	}

	@Override
	public int getCapacity() {
		return energyCapacity;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.addAll(Text.literal("+" + getCapacity() + " Energy Capacity").getWithStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY))));
		tooltip.addAll(Text.literal("+" + getHealth() + " Max Health").getWithStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY))));
	}
}
