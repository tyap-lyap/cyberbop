package tyaplyap.cyberbop.client.model.basic;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BasicLeftArmModel extends Model {
	private final ModelPart leftArm;

	public BasicLeftArmModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.leftArm = root.getChild("left_arm");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData Left_Arm4 = modelPartData.addChild("Left_Arm4", ModelPartBuilder.create().uv(0, 30).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
			.uv(14, 30).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F))
			.uv(0, 37).cuboid(-1.0F, 7.0F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
			.uv(22, 30).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		leftArm.render(matrices, vertices, light, overlay, color);
	}

}
