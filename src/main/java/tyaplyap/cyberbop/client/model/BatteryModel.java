package tyaplyap.cyberbop.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BatteryModel extends Model {
	public final ModelPart base;

	public BatteryModel(ModelPart root) {
		super(RenderLayer::getEntityCutout);
		this.base = root.getChild("base");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -16.0F, -5.0F, 10.0F, 16.0F, 10.0F, new Dilation(0.005F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		base.render(matrices, vertices, light, overlay, color);
	}
}
