package tyaplyap.cyberbop.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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

	public NightVisionModule(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension ex) {
		if(ex.getEnergyStored() > 0) {
			if (!player.hasStatusEffect(StatusEffects.NIGHT_VISION) || player.getWorld().getTime() % 40L == 0L) {
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 250, 0, false, false));
			}
		}
		else {
			if(player.hasStatusEffect(StatusEffects.NIGHT_VISION)) player.removeStatusEffect(StatusEffects.NIGHT_VISION);
		}
	}

	@Override
	public void onModuleRemoved(World world, PlayerEntity player) {
		if(!world.isClient()) {
			player.removeStatusEffect(StatusEffects.NIGHT_VISION);
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Grants night vision status effect.").formatted(Formatting.GRAY));
	}
}
