package tyaplyap.cyberbop.client.model.basic;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BasicLeftLegModel extends Model {

	private final ModelPart left_leg;

	public BasicLeftLegModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.left_leg = root.getChild("left_leg");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData left_Leg3 = modelPartData.addChild("left_Leg3", ModelPartBuilder.create().uv(32, 54).cuboid(-0.8F, 6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
			.uv(32, 44).cuboid(-1.8F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
			.uv(48, 48).cuboid(-1.8F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-0.2F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		left_leg.render(matrices, vertices, light, overlay, color);
	}

}
