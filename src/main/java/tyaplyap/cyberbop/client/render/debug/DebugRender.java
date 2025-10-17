package tyaplyap.cyberbop.client.render.debug;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import tyaplyap.cyberbop.block.entity.EnergyBlockEntity;
import tyaplyap.cyberbop.block.entity.EnergyWireBlockEntity;
import tyaplyap.cyberbop.packet.DebugCablePacket;

import java.awt.*;

public class DebugRender {

	public static void DebugRenderWires(EnergyWireBlockEntity entity, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

		HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;//MinecraftClient.getInstance().player.raycast(20, 0.0F, false);
		if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
			MatrixStack matrixStack = new MatrixStack();
			matrixStack.push();


			BlockPos blockPos4 = ((BlockHitResult) hitResult).getBlockPos();
			if (entity == entity.getWorld().getBlockEntity(blockPos4)) {
				WorldRenderer.drawShapeOutline(matrixStack, vertexConsumers.getBuffer(RenderLayer.getLines()), state.getOutlineShape(entity.getWorld(), blockPos4, ShapeContext.of(MinecraftClient.getInstance().player)), (double) entity.getPos().getX() - MinecraftClient.getInstance().getCameraEntity().getPos().getX(), (double) entity.getPos().getY() - MinecraftClient.getInstance().getCameraEntity().getEyeY(), (double) entity.getPos().getZ() - MinecraftClient.getInstance().getCameraEntity().getPos().getZ(), 1.0F, 0.0F, 0.0F, 1.0F, true);

				if (!DebugCablePacket.debugCables.isEmpty()) {
					for (var cable : DebugCablePacket.debugCables) {
						BlockState blockState = entity.getWorld().getBlockState(cable);
						WorldRenderer.drawShapeOutline(matrixStack, vertexConsumers.getBuffer(RenderLayer.getLines()), blockState.getOutlineShape(entity.getWorld(), cable, ShapeContext.of(MinecraftClient.getInstance().player)), (double) cable.getX() - MinecraftClient.getInstance().getCameraEntity().getPos().getX(), (double) cable.getY() - MinecraftClient.getInstance().getCameraEntity().getEyeY(), (double) cable.getZ() - MinecraftClient.getInstance().getCameraEntity().getPos().getZ(), 0.0F, 1.0F, 0.0F, 1.0F, true);

					}
					if (DebugCablePacket.ownerCable != null) {
						WorldRenderer.drawShapeOutline(matrixStack, vertexConsumers.getBuffer(RenderLayer.getLines()), entity.getWorld().getBlockState(DebugCablePacket.ownerCable).getOutlineShape(entity.getWorld(), DebugCablePacket.ownerCable, ShapeContext.of(MinecraftClient.getInstance().player)), (double) DebugCablePacket.ownerCable.getX() - MinecraftClient.getInstance().getCameraEntity().getPos().getX(), (double) DebugCablePacket.ownerCable.getY() - MinecraftClient.getInstance().getCameraEntity().getEyeY(), (double) DebugCablePacket.ownerCable.getZ() - MinecraftClient.getInstance().getCameraEntity().getPos().getZ(), 0.0F, 0.0F, 1.0F, 1.0F, true);

					}
				}
				matrixStack.pop();
			}
		}
		if (MinecraftClient.getInstance().player.age % 21 == 20) {
			DebugCablePacket.debugCables.clear();
			DebugCablePacket.ownerCable = null;
		}
	}

	public static void DebugRender(EnergyBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		float f;
		matrices.translate(0.5,1.5,0.5);



		String text = String.valueOf("Stored:" + entity.energyStorage.storedEnergy);
		int wigth =  MinecraftClient.getInstance().textRenderer.getWidth(text);
		matrices.multiply(MinecraftClient.getInstance().getBlockEntityRenderDispatcher().camera.getRotation());
		Matrix4f matrix4f = matrices.peek().getPositionMatrix();
		if (String.valueOf(entity.energyStorage.storedEnergy).length() > 6) {
			f = 0.010F;
		} else {
			f =0.014F;

		}
		matrices.scale(f, -f, f);
		Random random = Random.create(BlockPos.add(entity.getPos().getY(), entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ()));
		Random random1 = Random.create(BlockPos.add(entity.getPos().getY(), entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ())* 2);
		Random random2 = Random.create(BlockPos.add(entity.getPos().getY(), entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ())* 3);

		MinecraftClient.getInstance().textRenderer.draw(String.valueOf(text), (float)0-wigth/2, (float)0, new Color(random.nextBetween(0,255), random1.nextBetween(0,255), random2.nextBetween(0,255)).getRGB(), true, matrix4f, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH,new Color(0, 0, 0, 0).getRGB(), 15728880);

		matrices.pop();
	}
}
