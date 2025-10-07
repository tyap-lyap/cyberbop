package tyaplyap.cyberbop.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class NightVisionModule extends CyborgModuleItem {

	public NightVisionModule(Settings settings, String module) {
		super(settings, module);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player) {
		if (!player.hasStatusEffect(StatusEffects.NIGHT_VISION) || player.getWorld().getTime() % 40L == 0L) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 250, 0, false, false));
		}
	}

	@Override
	public void onModuleRemoved(ServerWorld world, PlayerEntity player) {
		player.removeStatusEffect(StatusEffects.NIGHT_VISION);
	}
}
