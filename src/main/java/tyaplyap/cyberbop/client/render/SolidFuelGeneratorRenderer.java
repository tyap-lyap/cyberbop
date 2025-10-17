package tyaplyap.cyberbop.client.render;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.FurnaceGeneratorBlock;
import tyaplyap.cyberbop.block.entity.FurnaceGeneratorBlockEntity;
import tyaplyap.cyberbop.client.CyberbopModClient;

public class SolidFuelGeneratorRenderer<T extends FurnaceGeneratorBlockEntity> implements BlockEntityRenderer<T> {
	ModelPart model;

	public SolidFuelGeneratorRenderer(BlockEntityRendererFactory.Context ctx) {
		model = ctx.getLayerModelPart(CyberbopModClient.SOLID_FUEL_GENERATOR_LAYER);
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var world = entity.getWorld();

		if(world != null && !entity.isRemoved() && isLit(entity)) {
			matrices.push();
			matrices.translate(0.5, 1.5, 0.5);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.getCachedState().get(FurnaceGeneratorBlock.FACING).asRotation())));

			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(CyberbopMod.id("textures/entity/solid_fuel_generator_overlay.png")));
			model.render(matrices, vertexConsumer, light, overlay);

			matrices.pop();
		}
	}

	boolean isLit(T entity) {
		return entity.getCachedState().get(FurnaceGeneratorBlock.LIT);
	}
}
