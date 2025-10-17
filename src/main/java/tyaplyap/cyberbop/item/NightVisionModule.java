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
	static int nightVisionStrength = 0;

	public NightVisionModule(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension ex) {
		if(!player.isCreative() && !player.isSpectator() && ex.getEnergyStored() > 0) {
			ex.setEnergyStored(Math.max(ex.getEnergyStored() - 2, 0));
		}
	}

	@Override
	public void clientTick(ClientWorld world, PlayerEntity player, PlayerExtension extension) {
		if(extension.getEnergyStored() > 0) {
			if(nightVisionStrength < 100) {
				nightVisionStrength++;
			}
		}
	}

	@Override
	public void onModuleRemoved(World world, PlayerEntity player) {
		if(world.isClient()) nightVisionStrength = 0;
	}

	public static float getNightVisionStrength() {
		return ((float)nightVisionStrength / 100F);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Grants night vision status effect.").formatted(Formatting.GRAY));
	}
}
