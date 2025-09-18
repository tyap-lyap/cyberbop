package tyaplyap.cyberbop.client;

import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class CyborgModel extends PlayerEntityModel<AbstractClientPlayerEntity> {

	public CyborgModel(ModelPart root) {
		super(root, false);
	}

	public static TexturedModelData getTexturedModelData() {
		return TexturedModelData.of(PlayerEntityModel.getTexturedModelData(Dilation.NONE, true), 64, 64);
	}
}
