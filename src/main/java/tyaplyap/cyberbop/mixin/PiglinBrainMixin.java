package tyaplyap.cyberbop.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyberbopItems;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {

	@Inject(method = "wearsGoldArmor", at = @At("HEAD"), cancellable = true)
	private static void wearsGoldArmor(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if(entity instanceof PlayerExtension ex && ex.isCyborg()) {
			if(ex.getCyborgHead().isOf(CyberbopItems.GOLDEN_HEAD) || ex.getCyborgBody().isOf(CyberbopItems.GOLDEN_BODY)
				|| ex.getCyborgRightArm().isOf(CyberbopItems.GOLDEN_ARM) || ex.getCyborgLeftArm().isOf(CyberbopItems.GOLDEN_ARM)
				|| ex.getCyborgRightLeg().isOf(CyberbopItems.GOLDEN_LEG) || ex.getCyborgLeftLeg().isOf(CyberbopItems.GOLDEN_LEG)) cir.setReturnValue(true);
		}
	}
}
