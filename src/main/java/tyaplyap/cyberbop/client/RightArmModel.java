package tyaplyap.cyberbop.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class RightArmModel extends Model {
	private final ModelPart Right_Arm2;

	public RightArmModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.Right_Arm2 = root.getChild("Right_Arm2");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData RightArm = modelPartData.addChild("RightArm", ModelPartBuilder.create().uv(10, 0).cuboid(0.0F, -2.0F, -2.0F, 1.0F, 12.0F, 4.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-2.0F, -2.0F, -2.0F, 1.0F, 12.0F, 4.0F, new Dilation(0.0F))
			.uv(56, -4).cuboid(-2.25F, -2.0F, -2.0F, 0.0F, 12.0F, 4.0F, new Dilation(0.0F))
			.uv(20, 8).cuboid(-2.75F, 3.0F, -2.5F, 4.0F, 2.0F, 5.0F, new Dilation(0.0F))
			.uv(20, 0).cuboid(-2.75F, -2.25F, -2.5F, 4.0F, 3.0F, 5.0F, new Dilation(0.0F))
			.uv(38, 0).cuboid(-2.75F, 7.25F, -2.5F, 4.0F, 3.0F, 5.0F, new Dilation(0.0F))
			.uv(0, 16).cuboid(-2.75F, -2.0F, -2.25F, 4.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		Right_Arm2.render(matrices, vertices, light, overlay, color);
	}

}
