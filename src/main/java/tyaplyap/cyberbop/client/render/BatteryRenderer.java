package tyaplyap.cyberbop.client.render;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.EnergyBatteryBlock;
import tyaplyap.cyberbop.block.entity.EnergyBatteryBlockEntity;
import tyaplyap.cyberbop.client.CyberbopModClient;

public class BatteryRenderer<T extends EnergyBatteryBlockEntity> implements BlockEntityRenderer<T> {
	ModelPart model;

	public BatteryRenderer(BlockEntityRendererFactory.Context ctx) {
		model = ctx.getLayerModelPart(CyberbopModClient.BATTERY_BLOCK_LAYER);
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var world = entity.getWorld();

		if(world != null && !entity.isRemoved() && entity.getCachedState().get(EnergyBatteryBlock.LEVEL) > 0) {
			matrices.push();
			matrices.translate(0.5, 1.5, 0.5);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(CyberbopMod.id("textures/entity/battery_block_overlay_" + entity.getCachedState().get(EnergyBatteryBlock.LEVEL) + ".png")));

			model.render(matrices, vertexConsumer, light, overlay);
			matrices.pop();
		}
	}
}
