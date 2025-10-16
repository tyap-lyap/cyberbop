package tyaplyap.cyberbop.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyberbopItems;

public class CyberbopHud {
	public static final Identifier ENERGY_BACKGROUND = CyberbopMod.id("textures/gui/energy_bar_background.png");
	public static final Identifier BLUE_ENERGY_OVERLAY = CyberbopMod.id("textures/gui/energy_bar_overlay.png");
	public static final Identifier GREEN_ENERGY_OVERLAY = CyberbopMod.id("textures/gui/energy_bar_overlay_green.png");
	public static final Identifier YELLOW_ENERGY_OVERLAY = CyberbopMod.id("textures/gui/energy_bar_overlay_yellow.png");

	public static void init() {
		HudRenderCallback.EVENT.register((context, tickDeltaManager) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			if(client.world != null) {
				renderDebug(client.world, client.player, context, tickDeltaManager);
				renderHud(client.world, client.player, context, tickDeltaManager);
			}

		});
	}

	static void renderDebug(ClientWorld world, ClientPlayerEntity player, DrawContext context, RenderTickCounter tickDeltaManager) {
		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			if(player instanceof PlayerExtension cyborg && cyborg.isCyborg()) {
				context.drawText(MinecraftClient.getInstance().textRenderer, "Cyborg Energy: " + cyborg.getEnergyStored(), 10, context.getScaledWindowHeight() / 2, 0xFFFFFFFF, true);
			}
		}
	}

	static void renderHud(ClientWorld world, ClientPlayerEntity player, DrawContext context, RenderTickCounter tickDeltaManager) {
		int x = context.getScaledWindowWidth() / 2 + 10;
		int y = context.getScaledWindowHeight() - 39;
		var interactionManager = MinecraftClient.getInstance().interactionManager;

		if(player instanceof PlayerExtension ex && ex.isCyborg() && interactionManager != null && interactionManager.hasStatusBars()) {
			context.drawTexture(ENERGY_BACKGROUND, x, y, 0, 0, 81, 8, 81, 8);

			int width = (int)(80.0F * Math.clamp((float)ex.getEnergyStored() / (float)ex.getCapacity(), 0, 1));

			if(ex.containsModule(CyberbopItems.EXTRA_BATTERY_MODULE)) {
				context.drawTexture(GREEN_ENERGY_OVERLAY, x, y, 0, 0, width, 8, 81, 8);
			}
			else if(ex.containsModule(CyberbopItems.LARGE_BATTERY_MODULE)) {
				context.drawTexture(YELLOW_ENERGY_OVERLAY, x, y, 0, 0, width, 8, 81, 8);
			}
			else {
				context.drawTexture(BLUE_ENERGY_OVERLAY, x, y, 0, 0, width, 8, 81, 8);
			}
		}
	}
}
