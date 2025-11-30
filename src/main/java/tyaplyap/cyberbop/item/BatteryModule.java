package tyaplyap.cyberbop.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class BatteryModule extends CyborgModuleItem {
	int energyCapacity;

	public BatteryModule(int energyCapacity, Settings settings) {
		super(settings);
		this.energyCapacity = energyCapacity;
	}

	public int getEnergyCapacity() {
		return energyCapacity;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("+§b" + getEnergyCapacity() + " §7Energy Capacity").formatted(Formatting.GRAY));
	}
}
