package tyaplyap.cyberbop.client.model.basic;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BasicRightLegModel extends Model {

	private final ModelPart right_leg;
	public BasicRightLegModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.right_leg = root.getChild("right_leg");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData Right_Leg2 = modelPartData.addChild("Right_Leg2", ModelPartBuilder.create().uv(0, 54).cuboid(-2.0F, 6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
			.uv(0, 44).cuboid(-3.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
			.uv(16, 48).cuboid(-3.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(1F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		right_leg.render(matrices, vertices, light, overlay, color);
	}

}
