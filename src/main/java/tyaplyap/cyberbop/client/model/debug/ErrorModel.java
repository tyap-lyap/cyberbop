package tyaplyap.cyberbop.client.model.debug;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ErrorModel extends Model {
	private final ModelPart error;

	public ErrorModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.error = root.getChild("error");
	}


	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData error = modelPartData.addChild("error", ModelPartBuilder.create().uv(0, 0).cuboid(4.5838F, -2.0217F, -1.0F, 0.7296F, 4.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(3.8542F, -2.0217F, -1.0F, 0.7296F, 0.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(2.7598F, -2.0217F, -1.0F, 0.7296F, 4.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(2.0302F, -2.0217F, -1.0F, 0.7296F, 0.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(0.571F, -2.0217F, -1.0F, 0.7296F, 4.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-0.1586F, -2.0217F, -1.0F, 0.7296F, 0.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-2.7122F, -2.0217F, -1.0F, 1.4592F, 0.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-4.1714F, -2.0217F, -1.0F, 0.7296F, 4.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-4.901F, -2.0217F, -1.0F, 0.7296F, 0.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(1.6654F, -1.5217F, -1.0F, 0.7296F, 1.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-0.5234F, -1.5217F, -1.0F, 0.7296F, 1.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-1.6178F, -1.5217F, -1.0F, 0.7296F, 3.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-3.077F, -1.5217F, -1.0F, 0.7296F, 3.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-5.2658F, -1.5217F, -1.0F, 0.7296F, 1.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(3.8542F, -0.0217F, -1.0F, 0.7296F, 0.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(2.0302F, -0.0217F, -1.0F, 0.7296F, 0.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-0.1586F, -0.0217F, -1.0F, 0.7296F, 0.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-4.901F, -0.0217F, -1.0F, 0.7296F, 0.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(1.6654F, 0.4783F, -1.0F, 0.7296F, 2.0F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-0.5234F, 0.4783F, -1.0F, 0.7296F, 2.0F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-5.2658F, 0.4783F, -1.0F, 0.7296F, 2.0F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(3.8542F, 1.9783F, -1.0F, 0.7296F, 0.5F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-2.7122F, 1.9783F, -1.0F, 1.4592F, 0.5F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.0326F, 15.0217F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		error.render(matrices, vertices, light, overlay);
	}
}
