package tyaplyap.cyberbop.client.render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;

public class AssemblerRenderer<T extends AssemblerBlockEntity> implements BlockEntityRenderer<T> {

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var world = entity.getWorld();

		if(world != null && !entity.isRemoved()) {

		}
	}
}
