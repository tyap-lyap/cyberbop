package tyaplyap.cyberbop.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.client.model.AssemblerModel;
import tyaplyap.cyberbop.client.render.AssemblerRenderer;
import tyaplyap.cyberbop.client.render.WiresRenderer;
import tyaplyap.cyberbop.extension.PlayerExtension;

public class CyberbopModClient implements ClientModInitializer {

	public static final EntityModelLayer WIRES_LAYER = new EntityModelLayer(CyberbopMod.id("wires"), "main");
	public static final EntityModelLayer ASSEMBLER_LAYER = new EntityModelLayer(CyberbopMod.id("assembler"), "main");

	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(WIRES_LAYER, WiresRenderer::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ASSEMBLER_LAYER, AssemblerModel::getTexturedModelData);

		BlockEntityRendererFactories.register(CyberbopBlockEntities.ASSEMBLER, ctx -> new AssemblerRenderer<>());
		BlockEntityRendererFactories.register(CyberbopBlockEntities.ENERGY_WIRE_BLOCK_ENTITY, ctx -> new WiresRenderer<>(ctx));

		HudRenderCallback.EVENT.register((context, tickDeltaManager) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			if(client.world != null && client.player instanceof PlayerExtension ex && ex.isCyborg()) {
				if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
					context.drawText(MinecraftClient.getInstance().textRenderer, "Cyborg Energy: " + ex.getCyborgEnergy(), 10, 200, 0xFFFFFFFF, true);
				}

				renderHud(client.world, client.player, context, tickDeltaManager);
			}

		});
	}

	static void renderHud(ClientWorld world, ClientPlayerEntity player, DrawContext context, RenderTickCounter tickDeltaManager) {
		int x = context.getScaledWindowWidth() / 2 + 10;
		int y = context.getScaledWindowHeight() - 39;
		Identifier background = CyberbopMod.id("textures/gui/energy_bar_background.png");
		Identifier overlay = CyberbopMod.id("textures/gui/energy_bar_overlay.png");
		Identifier overlayGreen = CyberbopMod.id("textures/gui/energy_bar_overlay_green.png");

		if(player instanceof PlayerExtension ex && ex.isCyborg()) {
			context.drawTexture(background, x, y, 0, 0, 81, 8, 81, 8);

			int width = (int)(80.0F * ((float)ex.getCyborgEnergy() / (float)ex.getCyborgMaxEnergy()));

			context.drawTexture(overlay, x, y, 0, 0, 1 + width, 8, 81, 8);
		}
	}
}
