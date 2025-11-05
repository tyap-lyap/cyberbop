package tyaplyap.cyberbop.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.client.render.CyborgPartRenderer;
import tyaplyap.cyberbop.client.render.CyborgPartRenderers;
import tyaplyap.cyberbop.client.util.EnergySynchronization;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.screen.AssemblerScreenHandler;
import tyaplyap.cyberbop.util.CyborgPartType;

import java.util.List;

public class AssemblerClientScreen extends HandledScreen<AssemblerScreenHandler> {

	public AssemblerClientScreen(AssemblerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	private static final Identifier TEXTURE = CyberbopMod.id("textures/gui/container/assembler.png");
	private static final Identifier ENERGY_BAR = CyberbopMod.id("container/energy_bar");
	private static final Identifier SLOT = CyberbopMod.id("container/container_slot");

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawCyborg(context, x + 26, y + 8, x + 20, y + 78);

		context.drawTexture(TEXTURE, x, y,20, 0, 0, 179, backgroundHeight, 256, 256);
		context.getMatrices().translate(0,0,100);
		context.drawGuiTexture(SLOT, 18, 18, 0, 0, this.x + 79, this.y + 12,50, 18, 18);
		context.drawGuiTexture(SLOT, 18, 18, 0, 0, this.x + 79, this.y + 34,50, 18, 18);
		context.drawGuiTexture(SLOT, 18, 18, 0, 0, this.x + 101, this.y + 24,50, 18, 18);
		context.drawGuiTexture(SLOT, 18, 18, 0, 0, this.x + 57, this.y + 24,50, 18, 18);
		context.drawGuiTexture(SLOT, 18, 18, 0, 0, this.x + 95, this.y + 56,50, 18, 18);
		context.drawGuiTexture(SLOT, 18, 18, 0, 0, this.x + 63, this.y + 56,50, 18, 18);
		int height_bar = (int)(68 * (MathHelper.clamp((float) EnergySynchronization.getEnergy()[0] / EnergySynchronization.getEnergy()[1], 0.0F, 1.0F))) +1;

		context.drawGuiTexture(ENERGY_BAR, 12, 69, 0, 69 - height_bar, this.x + 156, this.y + 78 - height_bar, 12, height_bar);

	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);

		if (this.isPointWithinBounds(154, 9, 12, 68, mouseX, mouseY)) {
			context.drawTooltip(this.textRenderer, List.of(Text.of("Energy Stored: " ), Text.of(EnergySynchronization.getEnergy()[0] + "/" + EnergySynchronization.getEnergy()[1])), mouseX, mouseY);
		}

		drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void init() {
		super.init();
		titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
		titleY = 4;
	}
	public static void drawCyborg(DrawContext context, int x1, int y1, int x2, int y2) {
		float x = (float)(x1 + x2) / 2.0F;
		float y = (float)(y1 + y2) / 2.0F;
		context.getMatrices().push();
		context.getMatrices().translate((double)x, (double)y+31.6, 50);
		context.getMatrices().scale(30, 30, -30);

		DiffuseLighting.method_34742();

		CyborgPartRenderer renderer = CyborgPartRenderers.get(CyberbopItems.BASIC_HEAD.getDefaultStack(), CyborgPartType.HEAD);

		renderPart(context.getMatrices(), renderer.model.get().getHead(), context, 0, 0,new Vector3f(0,0,0));
		renderPart(context.getMatrices(), renderer.model.get().getBody(), context, 0, 0,new Vector3f());
		renderPart(context.getMatrices(), renderer.model.get().getLeftArm(), context, 5, 2,new Vector3f(0,0,(float) Math.toRadians(-125)));
		renderPart(context.getMatrices(), renderer.model.get().getLeftArm(), context, 5, 2,new Vector3f(0,0,(float) Math.toRadians(-90)));
		renderPart(context.getMatrices(), renderer.model.get().getRightArm(), context, -5, 2,new Vector3f(0,0,(float) Math.toRadians(125)));
		renderPart(context.getMatrices(), renderer.model.get().getRightArm(), context, -5, 2,new Vector3f(0,0,(float) Math.toRadians(90)));
		renderPart(context.getMatrices(), renderer.model.get().getLeftLeg(), context, 1.9f, 10,new Vector3f(0,0,(float) Math.toRadians(-35)));
		renderPart(context.getMatrices(), renderer.model.get().getLeftLeg(), context, 1.9f, 10,new Vector3f());
		renderPart(context.getMatrices(), renderer.model.get().getRightLeg(), context, -1.9f, 10,new Vector3f(0,0,(float) Math.toRadians(35)));
		renderPart(context.getMatrices(), renderer.model.get().getRightLeg(), context, -1.9f, 10,new Vector3f());
		context.draw();
		context.getMatrices().pop();
		DiffuseLighting.enableGuiDepthLighting();
	}

	private static void renderPart(MatrixStack matrices, ModelPart model, DrawContext context, float x2, float y2, Vector3f rotate) {
		matrices.push();
		model.resetTransform();
		model.translate(new Vector3f(0, 0, 0));
		matrices.translate(2.17, -1.51, 0.5);
		model.translate(new Vector3f(x2, y2, 0));
		model.rotate(rotate);
		matrices.scale(1.3f,1.3f,1.3f);

		VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(RenderLayer.getEntityCutoutNoCull(CyberbopMod.id("textures/entity/cyborg_gui.png")));
		model.render(matrices, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}
}
