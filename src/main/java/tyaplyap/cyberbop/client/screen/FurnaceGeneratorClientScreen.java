package tyaplyap.cyberbop.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.client.util.EnergySynchronization;
import tyaplyap.cyberbop.screen.FurnaceGeneratorScreenHandler;

import java.util.List;

public class FurnaceGeneratorClientScreen extends HandledScreen<FurnaceGeneratorScreenHandler> {

	public FurnaceGeneratorClientScreen(FurnaceGeneratorScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	private static final Identifier TEXTURE = CyberbopMod.id("textures/gui/container/furnace_generator.png");
	private static final Identifier ENERGY_BAR = CyberbopMod.id("container/energy_bar");
	private static final Identifier LIT_PROGRESS = CyberbopMod.id("container/furnace_generator_lit_progress");

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

		if (this.handler.isBurning()) {
			int height_lit = MathHelper.ceil(this.handler.getFuelProgress() * 13.0F) + 1;
			context.drawGuiTexture(LIT_PROGRESS, 14, 14, 0, 14 - height_lit, this.x + 80, this.y + 36 + 14 - height_lit, 14, height_lit);
		}

		int height_bar = (int)(68 * (MathHelper.clamp((float) EnergySynchronization.getEnergy()[0] / EnergySynchronization.getEnergy()[1], 0.0F, 1.0F))) +1;

		context.drawGuiTexture(ENERGY_BAR, 12, 69, 0, 69 - height_bar, this.x + 154, this.y + 78 - height_bar, 12, height_bar);
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
	}
}
