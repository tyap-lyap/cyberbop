package tyaplyap.cyberbop.client.animation.module;

import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LongArmModuleAnimation implements AnimatableModule{
	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	protected static final RawAnimation HOOK_ANIM = RawAnimation.begin().thenPlay("hook");

	public AnimationController test = new AnimationController<>(this, "hook", state -> PlayState.STOP)
		.triggerableAnim("hook", HOOK_ANIM);

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(test);
		//controllers.add(new AnimationController<>(this, "Hook", state -> {
		//	return state.setAndContinue(HOOK_ANIM);
		//}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoCache;
	}
}
