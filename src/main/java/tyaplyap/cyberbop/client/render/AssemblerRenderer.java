package tyaplyap.cyberbop.client.render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.client.render.parts.CyborgPartRenderer;
import tyaplyap.cyberbop.client.render.parts.CyborgPartRenderers;

public class AssemblerRenderer<T extends AssemblerBlockEntity> implements BlockEntityRenderer<T> {

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var world = entity.getWorld();

		if(world != null && !entity.isRemoved()) {
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
		}
	}
}
