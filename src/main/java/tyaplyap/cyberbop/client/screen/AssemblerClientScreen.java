package tyaplyap.cyberbop.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.AssemblerBlock;
import tyaplyap.cyberbop.client.render.CyborgPartRenderer;
import tyaplyap.cyberbop.client.render.CyborgPartRenderers;
import tyaplyap.cyberbop.client.util.EnergySynchronization;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.screen.AssemblerScreenHandler;
import tyaplyap.cyberbop.screen.FurnaceGeneratorScreenHandler;
import tyaplyap.cyberbop.util.CyborgPartType;

import java.awt.*;
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
		drawEntity(context, x + 26, y + 8, x + 20, y + 78, 30, 0.0625F, mouseX, mouseY, this.client.player);

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
	public static void drawEntity(DrawContext context, int x1, int y1, int x2, int y2, int size, float f, float mouseX, float mouseY, LivingEntity entity) {
		float g = (float)(x1 + x2) / 2.0F;
		float h = (float)(y1 + y2) / 2.0F;
		//context.enableScissor(x1, y1, x2, y2);
		float i = (float)Math.atan((double)((g - mouseX) / 40.0F));
		float j = (float)Math.atan((double)((h - mouseY) / 40.0F));
		Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
		Quaternionf quaternionf2 = (new Quaternionf()).rotateX(j * 20.0F * 0.017453292F);
		quaternionf.mul(quaternionf2);
		float k = entity.bodyYaw;
		float l = entity.getYaw();
		float m = entity.getPitch();
		float n = entity.prevHeadYaw;
		float o = entity.headYaw;
		entity.bodyYaw = 180.0F + i * 20.0F;
		entity.setYaw(180.0F + i * 40.0F);
		entity.setPitch(-j * 20.0F);
		entity.headYaw = entity.getYaw();
		entity.prevHeadYaw = entity.getYaw();
		float p = entity.getScale();
		Vector3f vector3f = new Vector3f(0.0F, entity.getHeight() / 2.0F + f * p, 0.0F);
		float q = (float)size / p;
		drawEntity(context, g, h, q, vector3f, quaternionf, quaternionf2, entity);
		entity.bodyYaw = k;
		entity.setYaw(l);
		entity.setPitch(m);
		entity.prevHeadYaw = n;
		entity.headYaw = o;
		//context.disableScissor();
	}

	public static void drawEntity(DrawContext context, float x, float y, float size, Vector3f vector3f, Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, LivingEntity entity) {
		context.getMatrices().push();
		context.getMatrices().translate((double)x, (double)y, 50);
		context.getMatrices().scale(size, size, -size);
		context.getMatrices().translate(vector3f.x, vector3f.y, vector3f.z);
		//context.getMatrices().multiply(quaternionf);
		DiffuseLighting.method_34742();
		EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
		if (quaternionf2 != null) {
			//entityRenderDispatcher.setRotation(quaternionf2.conjugate(new Quaternionf()).rotateY(3.1415927F));
		}

		//entityRenderDispatcher.setRenderShadows(false);
		//RenderSystem.run(() -> {
			CyborgPartRenderer renderer = CyborgPartRenderers.get(CyberbopItems.BASIC_HEAD.getDefaultStack(), CyborgPartType.HEAD);
			CyborgPartRenderer renderer1 = CyborgPartRenderers.get(CyberbopItems.BASIC_HEAD.getDefaultStack(), CyborgPartType.HEAD);
			//CyborgPartRenderer body = CyborgPartRenderers.get(CyberbopItems.BASIC_BODY.getDefaultStack(), CyborgPartType.BODY);
			renderPart(context.getMatrices(), renderer.model.get().getHead(), renderer, context, 0, 0,new Vector3f(0,0,0));
			renderPart(context.getMatrices(), renderer.model.get().getBody(), renderer, context, 0, 0,new Vector3f());
			renderPart(context.getMatrices(), renderer1.model.get().getLeftArm(), renderer1, context, 5, 2,new Vector3f(0,0,(float) Math.toRadians(-125)));
			renderPart(context.getMatrices(), renderer.model.get().getLeftArm(), renderer, context, 5, 2,new Vector3f(0,0,(float) Math.toRadians(-90)));
			renderPart(context.getMatrices(), renderer1.model.get().getRightArm(), renderer1, context, -5, 2,new Vector3f(0,0,(float) Math.toRadians(125)));
			renderPart(context.getMatrices(), renderer.model.get().getRightArm(), renderer, context, -5, 2,new Vector3f(0,0,(float) Math.toRadians(90)));
			renderPart(context.getMatrices(), renderer1.model.get().getLeftLeg(), renderer1, context, 1.9f, 10,new Vector3f(0,0,(float) Math.toRadians(-35)));
			renderPart(context.getMatrices(), renderer.model.get().getLeftLeg(), renderer, context, 1.9f, 10,new Vector3f());
			renderPart(context.getMatrices(), renderer1.model.get().getRightLeg(), renderer1, context, -1.9f, 10,new Vector3f(0,0,(float) Math.toRadians(35)));
			renderPart(context.getMatrices(), renderer.model.get().getRightLeg(), renderer, context, -1.9f, 10,new Vector3f());

			//entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, context.getMatrices(), context.getVertexConsumers(), 15728880);
	//});
		context.draw();
		//entityRenderDispatcher.setRenderShadows(true);
		context.getMatrices().pop();
		DiffuseLighting.enableGuiDepthLighting();

	}
	private static void renderPart(MatrixStack matrices, ModelPart model, CyborgPartRenderer renderer, DrawContext context, float x2, float y2, Vector3f rotate) {
		matrices.push();
		model.resetTransform();
		model.translate(new Vector3f(0, 0, 0));

		matrices.translate(2.17, -1.51, 0.5);

		model.translate(new Vector3f(x2, y2, 0));
		model.rotate(rotate);
		//matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(0));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(0));
		//matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(AssemblerBlock.FACING).asRotation()));
		matrices.scale(1.3f,1.3f,1.3f);

		VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(RenderLayer.getEntityCutoutNoCull(CyberbopMod.id("textures/entity/cyborg_gui.png")));
		//VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(RenderLayer.getEntityCutoutNoCull(CyberbopMod.id(renderer.texture)));;
		model.render(matrices, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}
}
