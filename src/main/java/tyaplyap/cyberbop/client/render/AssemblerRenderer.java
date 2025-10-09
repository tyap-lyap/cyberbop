package tyaplyap.cyberbop.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.client.CyberbopModClient;
import tyaplyap.cyberbop.client.model.debug.ErrorModel;
import tyaplyap.cyberbop.client.render.parts.CyborgPartRenderer;
import tyaplyap.cyberbop.client.render.parts.CyborgPartRenderers;
import tyaplyap.cyberbop.item.CyborgArmPartItem;
import tyaplyap.cyberbop.item.CyborgLegPartItem;
import tyaplyap.cyberbop.item.CyborgPartItem;

public class AssemblerRenderer<T extends AssemblerBlockEntity> implements BlockEntityRenderer<T> {
	ModelPart model;
	ModelPart overlayPart;

	int light;

	boolean reverse;

	ModelPart errorModel;

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if(model == null) {
			model = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(CyberbopModClient.ASSEMBLER_LAYER);
			overlayPart = model.getChild("overlay");
			errorModel = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(CyberbopModClient.ERROR_LAYER);
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

			if(!entity.getHead().isEmpty()) {
				if (entity.getHead().getItem() instanceof CyborgPartItem item && item.partName.contains("head")) {
					CyborgPartRenderer part = CyborgPartRenderers.getPart(((CyborgPartItem) entity.getHead().getItem()).partName);
					if (part != null) part.renderAssembler(tickDelta, matrices, vertexConsumers, light, overlay);
				} else {
					renderError(matrices,tickDelta,vertexConsumers,entity.tickError, overlay, new Vec3d(0.5, 4.3, 0.5));
				}
			}
			if(!entity.getBody().isEmpty()) {
				if (entity.getBody().getItem() instanceof CyborgPartItem item && item.partName.contains("body")) {
					CyborgPartRenderer part = CyborgPartRenderers.getPart(((CyborgPartItem) entity.getBody().getItem()).partName);
					if (part != null) part.renderAssembler(tickDelta, matrices, vertexConsumers, light, overlay);
				} else {
					renderError(matrices,tickDelta,vertexConsumers,entity.tickError, overlay, new Vec3d(0.5, 3.7, 0.5));
				}
			}
			if(!entity.getRightArm().isEmpty()) {
				if (entity.getRightArm().getItem() instanceof CyborgArmPartItem) {
					CyborgPartRenderer part = CyborgPartRenderers.getPart(((CyborgArmPartItem) entity.getRightArm().getItem()).right);
					if (part != null) part.renderAssembler(tickDelta, matrices, vertexConsumers, light, overlay);
				} else {
					renderError(matrices,tickDelta,vertexConsumers,entity.tickError, overlay, new Vec3d(1, 3.8, 0.5));
				}
			}
			if(!entity.getLeftArm().isEmpty()) {
				if (entity.getLeftArm().getItem() instanceof CyborgArmPartItem) {
					CyborgPartRenderer part = CyborgPartRenderers.getPart(((CyborgArmPartItem) entity.getLeftArm().getItem()).left);
					if (part != null) part.renderAssembler(tickDelta, matrices, vertexConsumers, light, overlay);
				} else {
					renderError(matrices,tickDelta,vertexConsumers,entity.tickError, overlay, new Vec3d(0, 3.8, 0.5));
				}
			}
			if(!entity.getRightLeg().isEmpty()) {
				if (entity.getRightLeg().getItem() instanceof CyborgLegPartItem) {
					CyborgPartRenderer part = CyborgPartRenderers.getPart(((CyborgLegPartItem) entity.getRightLeg().getItem()).right);
					if (part != null) part.renderAssembler(tickDelta, matrices, vertexConsumers, light, overlay);
				} else {
					renderError(matrices,tickDelta,vertexConsumers,entity.tickError, overlay, new Vec3d(0.7, 3.1, 0.5));
				}
			}
			if(!entity.getLeftLeg().isEmpty()) {
				if (entity.getLeftLeg().getItem() instanceof CyborgLegPartItem) {
					CyborgPartRenderer part = CyborgPartRenderers.getPart(((CyborgLegPartItem) entity.getLeftLeg().getItem()).left);
					if (part != null) part.renderAssembler(tickDelta, matrices, vertexConsumers, light, overlay);
				} else {
					renderError(matrices,tickDelta,vertexConsumers,entity.tickError, overlay, new Vec3d(0.3, 3.1, 0.5));
				}
			}

			renderOverlay(entity, tickDelta, matrices, vertexConsumers, light, overlay);
		}
	}

	public void renderError (MatrixStack matrices, float tickDelta, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d vec3d) {
		matrices.push();
		matrices.translate(vec3d.getX(), vec3d.getY(), vec3d.getZ());
		matrices.scale(1.6f,1.6f,1.6f);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MinecraftClient.getInstance().player.age * 5));

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(CyberbopMod.id("textures/entity/error_tex.png")));

		errorModel.render(matrices, vertexConsumer, MathHelper.clamp(light, 20, 255), overlay);
		matrices.pop();

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
