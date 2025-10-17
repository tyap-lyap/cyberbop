package tyaplyap.cyberbop.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;
import tyaplyap.cyberbop.client.util.ClientOreHighlightData;
import tyaplyap.cyberbop.item.XrayVisionModule;

import java.util.ArrayList;

public class OreHighlightRenderer {
	private static final float OUTLINE_RED = 1.0f;
	private static final float OUTLINE_GREEN = 0.6f;
	private static final float OUTLINE_BLUE = 0.2f;
	private static final float OUTLINE_ALPHA = 0.55f;
	private static final float OUTLINE_THICKNESS = 0.01f;

	public static void renderOutlines(WorldRenderContext context) {
		ArrayList<BlockPos> highlightedBlocks = ClientOreHighlightData.getHighlightedBlocks();
		if (highlightedBlocks.isEmpty()) return;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

		MatrixStack matrices = context.matrixStack();
		if(matrices == null) return;
		matrices.push();

		Camera camera = context.camera();
		matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);

		Matrix4f positionMatrix = matrices.peek().getPositionMatrix();

		for (BlockPos pos : highlightedBlocks) {
			renderBlockOutline(buffer, positionMatrix, pos);
		}

		matrices.pop();
		setupRenderState();
		BufferRenderer.drawWithGlobalProgram(buffer.end());
		cleanupRenderState();
	}

	private static void setupRenderState() {
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.lineWidth(3.0f);
	}

	private static void cleanupRenderState() {
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.lineWidth(1.0f);
	}

	private static void renderBlockOutline(BufferBuilder buffer, Matrix4f matrix, BlockPos pos) {
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		double offset = OUTLINE_THICKNESS;

		float r = OUTLINE_RED;
		float g = OUTLINE_GREEN;
		float b = OUTLINE_BLUE;
		float a = XrayVisionModule.getAlpha();

		// Bottom face (Y = 0)
		buffer.vertex(matrix, (float)(x - offset), (float)(y - offset), (float)(z - offset)).color(r, g, b, a);
		buffer.vertex(matrix, (float)(x + 1 + offset), (float)(y - offset), (float)(z - offset)).color(r, g, b, a);

		buffer.vertex(matrix, (float)(x + 1 + offset), (float)(y - offset), (float)(z - offset)).color(r, g, b, a);
		buffer.vertex(matrix, (float)(x + 1 + offset), (float)(y - offset), (float)(z + 1 + offset)).color(r, g, b, a);

		buffer.vertex(matrix, (float)(x + 1 + offset), (float)(y - offset), (float)(z + 1 + offset)).color(r, g, b, a);
		buffer.vertex(matrix, (float)(x - offset), (float)(y - offset), (float)(z + 1 + offset)).color(r, g, b, a);

		buffer.vertex(matrix, (float)(x - offset), (float)(y - offset), (float)(z + 1 + offset)).color(r, g, b, a);
		buffer.vertex(matrix, (float)(x - offset), (float)(y - offset), (float)(z - offset)).color(r, g, b, a);

		// Top face (Y = 1)
		buffer.vertex(matrix, (float)(x - offset), (float)(y + 1 + offset), (float)(z - offset)).color(r, g, b, a);
		buffer.vertex(matrix, (float)(x + 1 + offset), (float)(y + 1 + offset), (float)(z - offset)).color(r, g, b, a);

		buffer.vertex(matrix, (float)(x + 1 + offset), (float)(y + 1 + offset), (float)(z - offset)).color(r, g, b, a);
		buffer.vertex(matrix, (float)(x + 1 + offset), (float)(y + 1 + offset), (float)(z + 1 + offset)).color(r, g, b, a);

		buffer.vertex(matrix, (float)(x + 1 + offset), (float)(y + 1 + offset), (float)(z + 1 + offset)).color(r, g, b, a);
		buffer.vertex(matrix, (float)(x - offset), (float)(y + 1 + offset), (float)(z + 1 + offset)).color(r, g, b, a);

		buffer.vertex(matrix, (float)(x - offset), (float)(y + 1 + offset), (float)(z + 1 + offset)).color(r, g, b, a);
		buffer.vertex(matrix, (float)(x - offset), (float)(y + 1 + offset), (float)(z - offset)).color(r, g, b, a);

		// Vertical edges
		buffer.vertex(matrix, (float)(x - offset), (float)(y - offset), (float)(z - offset)).color(r, g, b, a);
		buffer.vertex(matrix, (float)(x - offset), (float)(y + 1 + offset), (float)(z - offset)).color(r, g, b, a);

		buffer.vertex(matrix, (float)(x + 1 + offset), (float)(y - offset), (float)(z - offset)).color(r, g, b, a);
		buffer.vertex(matrix, (float)(x + 1 + offset), (float)(y + 1 + offset), (float)(z - offset)).color(r, g, b, a);

		buffer.vertex(matrix, (float)(x + 1 + offset), (float)(y - offset), (float)(z + 1 + offset)).color(r, g, b, a);
		buffer.vertex(matrix, (float)(x + 1 + offset), (float)(y + 1 + offset), (float)(z + 1 + offset)).color(r, g, b, a);

		buffer.vertex(matrix, (float)(x - offset), (float)(y - offset), (float)(z + 1 + offset)).color(r, g, b, a);
		buffer.vertex(matrix, (float)(x - offset), (float)(y + 1 + offset), (float)(z + 1 + offset)).color(r, g, b, a);
	}
}
