package tyaplyap.cyberbop.client.render.debug;

import net.minecraft.block.Block;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import tyaplyap.cyberbop.block.entity.EnergyBlockEntity;

public class DebugEnergyRenderer implements BlockEntityRenderer<EnergyBlockEntity> {
	public DebugEnergyRenderer(Block batteryTestTech, BlockEntityRendererFactory.Context ctx) {
	}

	@Override
	public void render(EnergyBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		DebugRender.DebugRender(entity, tickDelta, matrices, vertexConsumers, light, overlay);
	}
}
