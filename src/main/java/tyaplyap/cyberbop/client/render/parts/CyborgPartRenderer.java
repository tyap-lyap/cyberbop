package tyaplyap.cyberbop.client.render.parts;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public abstract class CyborgPartRenderer {

	public String name;

	public CyborgPartRenderer(String name) {
		this.name = name;
	}

	abstract public void render(PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity);
	abstract public void renderAssembler(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay);


	public ModelPart getModelPart() {
		return null;
	}

	abstract public void register();
}
