package tyaplyap.cyberbop.client.render;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.ControllerBlock;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.block.entity.ControllerBlockEntity;
import tyaplyap.cyberbop.client.CyberbopModClient;

public class ControllerRenderer<T extends ControllerBlockEntity> implements BlockEntityRenderer<T> {
	ModelPart model;

	Direction[] directions = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

	public ControllerRenderer(BlockEntityRendererFactory.Context ctx) {
		model = ctx.getLayerModelPart(CyberbopModClient.CONTROLLER_LAYER);
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var world = entity.getWorld();

		if(world != null && !entity.isRemoved() && connected(entity)) {
			matrices.push();
			matrices.translate(0.5, 1.5, 0.5);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.getCachedState().get(ControllerBlock.FACING).asRotation())));

			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(CyberbopMod.id("textures/entity/controller_overlay.png")));
			model.render(matrices, vertexConsumer, light, overlay);

			matrices.pop();
		}
	}

	boolean connected(T entity) {
		for(Direction direction : directions) {
			var assemblerPos = entity.getPos().add(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
			var blockEntity = entity.getWorld().getBlockEntity(assemblerPos);

			if(blockEntity instanceof AssemblerBlockEntity) return true;
		}
		return false;
	}
}
