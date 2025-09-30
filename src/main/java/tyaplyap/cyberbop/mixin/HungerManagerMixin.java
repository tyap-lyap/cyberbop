package tyaplyap.cyberbop.mixin;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tyaplyap.cyberbop.extension.PlayerExtension;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {

	@Shadow
	private int foodTickTimer;

	boolean isCyborg = false;

	@Inject(method = "update", at = @At("HEAD"), cancellable = true)
	void update(PlayerEntity player, CallbackInfo ci) {
		if(player instanceof PlayerExtension ex) {
			isCyborg = ex.isCyborg();

			if(isCyborg) {

				if(ex.getCyborgEnergy() != 0) {

					if(player.getHealth() != player.getMaxHealth()) {
						this.foodTickTimer++;
						if (this.foodTickTimer >= 20) {
							player.heal(1.0F);
							ex.setCyborgEnergy(Math.max(ex.getCyborgEnergy() - 20, 0));

							this.foodTickTimer = 0;
						}
					}


				}

				ci.cancel();
			}
		}

	}

	@Inject(method = "getFoodLevel", at = @At("HEAD"), cancellable = true)
	void getFoodLevel(CallbackInfoReturnable<Integer> cir) {
		if(isCyborg) cir.setReturnValue(20);
	}
}
