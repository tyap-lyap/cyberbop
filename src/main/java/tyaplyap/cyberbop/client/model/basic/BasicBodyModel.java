package tyaplyap.cyberbop.client.model.basic;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BasicBodyModel extends Model {
	private final ModelPart body;

	public BasicBodyModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.body = root.getChild("body");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData Body2 = modelPartData.addChild("Body2", ModelPartBuilder.create().uv(36, 22).cuboid(-4.0F, -24.0F, -2.0F, 8.0F, 5.0F, 4.0F, new Dilation(0.0F))
			.uv(48, 16).cuboid(1.0F, -19.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
			.uv(40, 16).cuboid(-3.0F, -19.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
			.uv(36, 31).cuboid(-4.0F, -15.0F, -2.0F, 8.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		body.render(matrices, vertices, light, overlay, color);
	}
}
