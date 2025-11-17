package tyaplyap.cyberbop.client.animation.module;

import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.util.RenderUtil;

public interface AnimatableModule extends SingletonGeoAnimatable {
	@Override
	default double getTick(Object object) {
		return RenderUtil.getCurrentTick();
	}

}
