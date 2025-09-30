package tyaplyap.cyberbop.client.render.parts.basic;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.client.model.basic.BasicLeftArmModel;
import tyaplyap.cyberbop.client.render.parts.CyborgPartRenderer;

public class BasicLeftArmCyborgPart extends CyborgPartRenderer {

	ModelPart model;
	String texture;
	public final EntityModelLayer layer;

	public BasicLeftArmCyborgPart(String name, String texture) {
		super(name);
		layer = new EntityModelLayer(CyberbopMod.id(name), "main");
		this.texture = texture;
	}

	@Override
	public void render(PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity) {
		if(model == null) {
			model = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(layer);
		}
		model.copyTransform(contextModel.leftArm);

		contextModel.leftArm.visible = false;

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(CyberbopMod.id(texture)));
		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
	}

	@Override
	public void renderAssembler(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if(model == null) {
			model = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(layer);
		}
		matrices.push();
		model.resetTransform();
		matrices.translate(0.19, 2.35, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(CyberbopMod.id(texture)));
		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}

	public void register() {
		EntityModelLayerRegistry.registerModelLayer(layer, BasicLeftArmModel::getTexturedModelData);
	}
}
