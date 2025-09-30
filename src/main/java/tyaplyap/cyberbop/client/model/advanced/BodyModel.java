package tyaplyap.cyberbop.client.model.advanced;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BodyModel extends Model {
	private final ModelPart body;

	public BodyModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.body = root.getChild("body");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 26).cuboid(-15.0F, 9.0F, -2.0F, 8.0F, 3.0F, 4.0F, new Dilation(0.0F))
			.uv(0, 16).cuboid(-15.0F, 0.0F, -2.0F, 8.0F, 6.0F, 4.0F, new Dilation(0.0F))
			.uv(24, 17).cuboid(-14.0F, 6.0F, -1.0F, 6.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(11.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		body.render(matrices, vertices, light, overlay, color);
	}
}
