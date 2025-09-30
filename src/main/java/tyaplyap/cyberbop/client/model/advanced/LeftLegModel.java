package tyaplyap.cyberbop.client.model.advanced;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class LeftLegModel extends Model {

	private final ModelPart left_leg;

	public LeftLegModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.left_leg = root.getChild("left_leg");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(16, 42).cuboid(0.0F, -5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
			.uv(0, 42).cuboid(0.0F, -12.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
			.uv(32, 47).cuboid(1.0F, -7.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
			.uv(40, 42).cuboid(0.1F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		left_leg.render(matrices, vertices, light, overlay, color);
	}

}
