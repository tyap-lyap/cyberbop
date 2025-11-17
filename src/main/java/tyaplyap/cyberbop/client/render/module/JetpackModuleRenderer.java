package tyaplyap.cyberbop.client.render.module;

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
import tyaplyap.cyberbop.client.model.module.ModuleModel;

public class JetpackModuleRenderer extends ModuleRenderer {

	public JetpackModuleRenderer(String texture, ModuleModel model) {
		super(texture, model);
	}

	@Override
	public void render(PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity) {
		this.model.getRoot().copyTransform(contextModel.body);

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(CyberbopMod.id(this.texture)));
		this.model.getRoot().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
	}

	@Override
	public void renderAssembler(AssemblerBlockEntity assembler, BlockState state, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		this.model.getRoot().resetTransform();
		this.model.getRoot().translate(new Vector3f(0, -24, 0));

		matrices.translate(0.5, 1, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(AssemblerBlock.FACING).asRotation()));
		matrices.scale(0.95f,0.95f,0.95f);

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(CyberbopMod.id(this.texture)));
		this.model.getRoot().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}
}
