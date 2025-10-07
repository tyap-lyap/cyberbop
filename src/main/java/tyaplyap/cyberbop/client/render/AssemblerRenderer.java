package tyaplyap.cyberbop.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.client.CyberbopModClient;
import tyaplyap.cyberbop.client.render.parts.CyborgPartRenderer;
import tyaplyap.cyberbop.client.render.parts.CyborgPartRenderers;

public class AssemblerRenderer<T extends AssemblerBlockEntity> implements BlockEntityRenderer<T> {
	ModelPart model;
	ModelPart overlayPart;

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if(model == null) {
			model = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(CyberbopModClient.ASSEMBLER_LAYER);
			overlayPart = model.getChild("overlay");
		}
		var world = entity.getWorld();

		if(world != null && !entity.isRemoved()) {
			matrices.push();
			matrices.translate(0.5, 1.5, 0.5);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(CyberbopMod.id("textures/entity/assembler.png")));
			overlayPart.visible = false;
			model.render(matrices, vertexConsumer, light, overlay);

			matrices.pop();

			if(!entity.head.isBlank()) {
				CyborgPartRenderer part = CyborgPartRenderers.getPart(entity.head);
				if(part != null) part.renderAssembler(tickDelta, matrices, vertexConsumers, light, overlay);
			}
			if(!entity.body.isBlank()) {
				CyborgPartRenderer part = CyborgPartRenderers.getPart(entity.body);
				if(part != null) part.renderAssembler(tickDelta, matrices, vertexConsumers, light, overlay);
			}
			if(!entity.rightArm.isBlank()) {
				CyborgPartRenderer part = CyborgPartRenderers.getPart(entity.rightArm);
				if(part != null) part.renderAssembler(tickDelta, matrices, vertexConsumers, light, overlay);
			}
			if(!entity.leftArm.isBlank()) {
				CyborgPartRenderer part = CyborgPartRenderers.getPart(entity.leftArm);
				if(part != null) part.renderAssembler(tickDelta, matrices, vertexConsumers, light, overlay);
			}
			if(!entity.rightLeg.isBlank()) {
				CyborgPartRenderer part = CyborgPartRenderers.getPart(entity.rightLeg);
				if(part != null) part.renderAssembler(tickDelta, matrices, vertexConsumers, light, overlay);
			}
			if(!entity.leftLeg.isBlank()) {
				CyborgPartRenderer part = CyborgPartRenderers.getPart(entity.leftLeg);
				if(part != null) part.renderAssembler(tickDelta, matrices, vertexConsumers, light, overlay);
			}

			renderOverlay(entity, tickDelta, matrices, vertexConsumers, light, overlay);
		}
	}

	public void renderOverlay(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		matrices.translate(0.5, 1.5, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(CyberbopMod.id("textures/entity/assembler_overlay.png")));
		overlayPart.visible = true;
		overlayPart.render(matrices, vertexConsumer, light, overlay);

		matrices.pop();
	}
}
