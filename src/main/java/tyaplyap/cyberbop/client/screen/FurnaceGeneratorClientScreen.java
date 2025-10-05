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
import tyaplyap.cyberbop.screen.FurnaceGeneratorScreenHandler;

public class FurnaceGeneratorClientScreen extends HandledScreen<FurnaceGeneratorScreenHandler> {

	public FurnaceGeneratorClientScreen(FurnaceGeneratorScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}
	// A path to the gui texture. In this example we use the texture from the dispenser

	private static final Identifier TEXTURE = CyberbopMod.id("textures/gui/container/furnace_generator.png");
	// For versions before 1.21:
	// private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/dispenser.png");

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
		int i = this.x;
		int j = this.y;
		if (this.handler.isBurning()) {
			int l = MathHelper.ceil(this.handler.getFuelProgress() * 13.0F) + 1;
			context.drawGuiTexture(CyberbopMod.id("container/furnace_generator_lit_progress"), 14, 14, 0, 14 - l, i + 80, j + 36 + 14 - l, 14, l);
		}



		int height = (int)this.handler.getEnergyHeight() + 1;
		context.drawGuiTexture(CyberbopMod.id("container/energy_bar"), 12, 69, 0, 69 - height, i + 154, j + 78 - height, 12, height);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);
		drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void init() {
		super.init();
		// Center the title
		titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
	}
}
