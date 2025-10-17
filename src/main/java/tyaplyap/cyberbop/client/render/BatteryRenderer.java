package tyaplyap.cyberbop.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
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
			BlockState state = entity.getCachedState();

			matrices.translate(0.5, 0.5, 0.5);

			if(state.get(EnergyBatteryBlock.FACING).getAxis().equals(Direction.Axis.Y)) {
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
			}
			if(state.get(EnergyBatteryBlock.FACING).getAxis().equals(Direction.Axis.Z)) {
				matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(180));
			}
			if(state.get(EnergyBatteryBlock.FACING).getAxis().equals(Direction.Axis.X)) {
				matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));
			}

			matrices.multiply(state.get(EnergyBatteryBlock.FACING).getRotationQuaternion());

			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(CyberbopMod.id("textures/entity/battery_block_overlay_" + entity.getCachedState().get(EnergyBatteryBlock.LEVEL) + ".png")));

			model.render(matrices, vertexConsumer, light, overlay);
			matrices.pop();
		}
	}
}
