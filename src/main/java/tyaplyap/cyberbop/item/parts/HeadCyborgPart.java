package tyaplyap.cyberbop.item.parts;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.client.HeadModel;

public class HeadCyborgPart extends CyborgPart {

	ModelPart model;
	String texture;
	public final EntityModelLayer layer;

	public HeadCyborgPart(String name, String texture) {
		super(name);
		layer = new EntityModelLayer(CyberbopMod.id(name), "main");
		this.texture = texture;
	}

	@Override
	public void render(PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity) {
		if(model == null) {
			model = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(layer);
		}
		model.copyTransform(contextModel.getHead());
		contextModel.head.visible = false;

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(CyberbopMod.id(texture)));
		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
	}

	@Override
	public void renderAssembler(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if(model == null) {
			model = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(layer);
		}

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(CyberbopMod.id(texture)));
		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
	}

	public void register() {
		EntityModelLayerRegistry.registerModelLayer(layer, HeadModel::getTexturedModelData);
	}
}
