package tyaplyap.cyberbop.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class AssemblerModel extends Model {
	public final ModelPart bone;
	private final ModelPart handle;

	public AssemblerModel(ModelPart root) {
		super(RenderLayer::getEntityTranslucent);
		this.bone = root.getChild("bone");
		this.handle = root.getChild("handle");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData handle = modelPartData.addChild("handle", ModelPartBuilder.create().uv(64, 0).cuboid(-1.0F, -28.0F, 4.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F))
			.uv(72, 0).cuboid(-2.0F, -31.0F, 3.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
			.uv(88, 0).cuboid(-5.0F, -40.0F, 1.0F, 10.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r1 = handle.addChild("cube_r1", ModelPartBuilder.create().uv(64, 0).cuboid(-1.0F, -9.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -30.0F, 5.0F, 0.2182F, 0.0F, 0.0F));

		ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 0).cuboid(-16.0F, -16.0F, 0.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 24.0F, -8.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		bone.render(matrices, vertices, light, overlay, color);
		handle.render(matrices, vertices, light, overlay, color);
	}
}
