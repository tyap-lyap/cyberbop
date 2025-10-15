package tyaplyap.cyberbop.client.render.parts;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.AssemblerBlock;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.client.model.CyborgPartsModel;
import tyaplyap.cyberbop.client.render.CyborgPartRenderer;

import java.util.function.Supplier;

public class HeadPartRenderer extends CyborgPartRenderer {

	public HeadPartRenderer(String name, String texture, Supplier<CyborgPartsModel> model) {
		super(name, texture, model);
	}

	@Override
	public void render(PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity) {
		this.model.get().getHead().copyTransform(contextModel.head);
		contextModel.head.visible = false;

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(CyberbopMod.id(this.texture)));
		this.model.get().getHead().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

		matrices.push();
		this.model.get().getHead().copyTransform(contextModel.head);
		VertexConsumer emissiveConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(CyberbopMod.id(this.texture.replace(".png", "_emission.png"))));
		this.model.get().getHead().render(matrices, emissiveConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}

	@Override
	public void renderAssembler(AssemblerBlockEntity assembler, BlockState state, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		this.model.get().getHead().resetTransform();
		this.model.get().getHead().translate(new Vector3f(0, -24, 0));

		matrices.translate(0.5, 1, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(AssemblerBlock.FACING).asRotation()));
		matrices.scale(0.95f,0.95f,0.95f);

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(CyberbopMod.id(this.texture)));
		this.model.get().getHead().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();

		matrices.push();
		matrices.translate(0.5, 1, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(AssemblerBlock.FACING).asRotation()));
		matrices.scale(0.95f,0.95f,0.95f);

		VertexConsumer emissiveConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(CyberbopMod.id(this.texture.replace(".png", "_emission.png"))));
		this.model.get().getHead().render(matrices, emissiveConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}
}
