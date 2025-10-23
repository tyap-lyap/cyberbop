package tyaplyap.cyberbop.mixin.toughasnails;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.thirst.ThirstHooks;
import tyaplyap.cyberbop.extension.PlayerExtension;

@Mixin(ThirstHooks.class)
public class ThirstHooksMixin {

	@Inject(method = "doFoodDataTick", at = @At("HEAD"), cancellable = true)
	private static void update(HungerManager data, PlayerEntity player, CallbackInfo ci) {
		if(player instanceof PlayerExtension ex && ex.isCyborg()) {

			if(ex.getEnergyStored() > 0) {
				if(player.getHealth() != player.getMaxHealth()) {
					data.foodTickTimer++;
					if (data.foodTickTimer >= 40) {
						player.heal(1.0F);

						if(!player.isCreative() && !player.isSpectator()) {
							ex.setEnergyStored(Math.max(ex.getEnergyStored() - 20, 0));
						}

						data.foodTickTimer = 0;
					}
				}
			}
			ci.cancel();
		}
	}
}
