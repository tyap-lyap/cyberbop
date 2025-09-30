package tyaplyap.cyberbop.client.model.advanced;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class RightLegModel extends Model {

	private final ModelPart right_leg;
	public RightLegModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.right_leg = root.getChild("right_leg");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(16, 33).cuboid(-4.0F, -5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
			.uv(0, 33).cuboid(-4.0F, -12.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
			.uv(32, 38).cuboid(-3.0F, -7.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
			.uv(40, 26).cuboid(-3.9F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		right_leg.render(matrices, vertices, light, overlay, color);
	}

}
