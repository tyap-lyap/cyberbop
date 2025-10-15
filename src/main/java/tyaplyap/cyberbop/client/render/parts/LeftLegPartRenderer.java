package tyaplyap.cyberbop.client.render.parts;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
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

public class LeftLegPartRenderer extends CyborgPartRenderer {

	public LeftLegPartRenderer(String name, String texture, Supplier<CyborgPartsModel> model) {
		super(name, texture, model);
	}

	@Override
	public void render(PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity) {
		this.model.get().getLeftLeg().copyTransform(contextModel.leftLeg);

		contextModel.leftLeg.visible = false;

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(CyberbopMod.id(this.texture)));
		this.model.get().getLeftLeg().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
	}

	@Override
	public void renderAssembler(AssemblerBlockEntity assembler, BlockState state, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		this.model.get().getLeftLeg().resetTransform();
		this.model.get().getLeftLeg().translate(new Vector3f(1.9F, -12, 0));

		matrices.translate(0.5, 1, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(AssemblerBlock.FACING).asRotation()));
		matrices.scale(0.95f,0.95f,0.95f);

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(CyberbopMod.id(this.texture)));
		this.model.get().getLeftLeg().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}

}
