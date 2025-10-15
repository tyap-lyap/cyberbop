package tyaplyap.cyberbop.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public abstract class CyborgPartsModel extends Model {

	public CyborgPartsModel() {
		super(RenderLayer::getEntityCutoutNoCull);
	}

	public abstract ModelPart getHead();
	public abstract ModelPart getBody();
	public abstract ModelPart getRightArm();
	public abstract ModelPart getLeftArm();
	public abstract ModelPart getRightLeg();
	public abstract ModelPart getLeftLeg();

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {

	}
}
