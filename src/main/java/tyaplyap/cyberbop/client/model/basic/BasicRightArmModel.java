package tyaplyap.cyberbop.client.model.basic;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BasicRightArmModel extends Model {
	private final ModelPart rightArm;

	public BasicRightArmModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.rightArm = root.getChild("right_arm");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData Right_Arm2 = modelPartData.addChild("Right_Arm2", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
			.uv(14, 16).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F))
			.uv(0, 23).cuboid(-2.0F, 7.0F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
			.uv(22, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		rightArm.render(matrices, vertices, light, overlay, color);
	}

}
