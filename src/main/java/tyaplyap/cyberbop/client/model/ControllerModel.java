package tyaplyap.cyberbop.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ControllerModel extends Model {
	public final ModelPart base;
	private final ModelPart screen;

	public ControllerModel(ModelPart root) {
		super(RenderLayer::getEntityTranslucent);
		this.base = root.getChild("base");
		this.screen = root.getChild("screen");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 17).cuboid(-5.0F, -14.0F, -4.0F, 10.0F, 14.0F, 8.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData screen = modelPartData.addChild("screen", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -5.0F, 16.0F, 4.0F, 13.0F, new Dilation(0.01F)), ModelTransform.of(0.0F, 16.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		base.render(matrices, vertices, light, overlay, color);
		screen.render(matrices, vertices, light, overlay, color);
	}
}

