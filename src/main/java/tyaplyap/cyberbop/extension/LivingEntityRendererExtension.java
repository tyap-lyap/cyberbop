package tyaplyap.cyberbop.extension;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public interface LivingEntityRendererExtension {
	PlayerEntityModel<AbstractClientPlayerEntity> getCyborgModel();
}
