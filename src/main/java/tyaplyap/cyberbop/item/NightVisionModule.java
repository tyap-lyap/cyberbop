package tyaplyap.cyberbop.item;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import tyaplyap.cyberbop.extension.PlayerExtension;

import java.util.List;

public class NightVisionModule extends CyborgModuleItem {
	static float nightVisionStrength = 0.0F;

	public NightVisionModule(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension ex) {
		if(ex.getEnergyStored() > 0) {
			if(world.getTime() % 4 == 0) ex.setEnergyStored(Math.max(ex.getEnergyStored() - 1, 0));
		}
	}

	@Override
	public void clientTick(ClientWorld world, PlayerEntity player, PlayerExtension extension) {
		if(extension.getEnergyStored() > 0) {
			if(nightVisionStrength < 1.0F) {
				nightVisionStrength = nightVisionStrength + 0.01F;
			}
		}
	}

	@Override
	public void onModuleRemoved(World world, PlayerEntity player) {
		if(world.isClient()) nightVisionStrength = 0.0F;
	}

	public static float getNightVisionStrength() {
		return nightVisionStrength;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Grants night vision status effect.").formatted(Formatting.GRAY));
	}
}
