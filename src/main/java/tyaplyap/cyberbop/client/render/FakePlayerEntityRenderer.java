package tyaplyap.cyberbop.client.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;
import tyaplyap.cyberbop.entity.FakePlayerEntity;

public class FakePlayerEntityRenderer extends LivingEntityRenderer<FakePlayerEntity, PlayerEntityModel<FakePlayerEntity>> {

	public FakePlayerEntityRenderer(EntityRendererFactory.Context ctx, PlayerEntityModel<FakePlayerEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Override
	public Identifier getTexture(FakePlayerEntity entity) {
		return null;
	}
}
