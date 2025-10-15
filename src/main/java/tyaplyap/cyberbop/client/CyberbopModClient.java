package tyaplyap.cyberbop.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.CyberbopBlocks;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.client.model.AssemblerModel;
import tyaplyap.cyberbop.client.model.AdvancedCyborgModel;
import tyaplyap.cyberbop.client.model.BasicCyborgModel;
import tyaplyap.cyberbop.client.model.ControllerModel;
import tyaplyap.cyberbop.client.model.debug.ErrorModel;
import tyaplyap.cyberbop.client.render.*;
import tyaplyap.cyberbop.client.screen.AssemblerClientScreen;
import tyaplyap.cyberbop.client.screen.FurnaceGeneratorClientScreen;
import tyaplyap.cyberbop.client.util.ClientOreHighlightData;
import tyaplyap.cyberbop.client.util.EnergySynchronization;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.packet.DebugCablePacket;
import tyaplyap.cyberbop.packet.EnergyGuiUpdatePacket;
import tyaplyap.cyberbop.packet.UseJetpackPacket;

import tyaplyap.cyberbop.client.render.debug.DebugEnergyRenderer;

public class CyberbopModClient implements ClientModInitializer {

	public static final EntityModelLayer WIRES_LAYER = new EntityModelLayer(CyberbopMod.id("wires"), "main");
	public static final EntityModelLayer ASSEMBLER_LAYER = new EntityModelLayer(CyberbopMod.id("assembler"), "main");
	public static final EntityModelLayer CONTROLLER_LAYER = new EntityModelLayer(CyberbopMod.id("controller"), "main");
	public static final EntityModelLayer ERROR_LAYER = new EntityModelLayer(CyberbopMod.id("error"), "main");
	public static final EntityModelLayer ADVANCED_CYBORG_LAYER = new EntityModelLayer(CyberbopMod.id("advanced_cyborg"), "main");
	public static final EntityModelLayer BASIC_CYBORG_LAYER = new EntityModelLayer(CyberbopMod.id("basic_cyborg"), "main");

	public static final Identifier ENERGY_BACKGROUND = CyberbopMod.id("textures/gui/energy_bar_background.png");
	public static final Identifier BLUE_ENERGY_OVERLAY = CyberbopMod.id("textures/gui/energy_bar_overlay.png");
	public static final Identifier GREEN_ENERGY_OVERLAY = CyberbopMod.id("textures/gui/energy_bar_overlay_green.png");

	@Override
	public void onInitializeClient() {
		//TEST//
		BlockEntityRendererFactories.register(CyberbopBlockEntities.ENERGY_GENERATOR, (BlockEntityRendererFactory.Context ctx) -> new DebugEnergyRenderer(CyberbopBlocks.ENERGY_GENERATOR, ctx));
		BlockEntityRendererFactories.register(CyberbopBlockEntities.ENERGY_RECEIVER, (BlockEntityRendererFactory.Context ctx) -> new DebugEnergyRenderer(CyberbopBlocks.ENERGY_RECEIVER, ctx));
		BlockEntityRendererFactories.register(CyberbopBlockEntities.SOLAR_PANEL, (BlockEntityRendererFactory.Context ctx) -> new DebugEnergyRenderer(CyberbopBlocks.SOLAR_PANEL, ctx));
		BlockEntityRendererFactories.register(CyberbopBlockEntities.BATTERY_TEST, (BlockEntityRendererFactory.Context ctx) -> new DebugEnergyRenderer(CyberbopBlocks.BATTERY_TEST, ctx));
		BlockEntityRendererFactories.register(CyberbopBlockEntities.CHARGING_PAD, (BlockEntityRendererFactory.Context ctx) -> new DebugEnergyRenderer(CyberbopBlocks.CHARGING_PAD, ctx));
		BlockEntityRendererFactories.register(CyberbopBlockEntities.FURNACE_GENERATOR, (BlockEntityRendererFactory.Context ctx) -> new DebugEnergyRenderer(CyberbopBlocks.FURNACE_GENERATOR, ctx));
		//TEST//
		ClientPlayNetworking.registerGlobalReceiver(EnergyGuiUpdatePacket.ID, (payload, context) -> {
			context.client().execute(() -> {
				EnergySynchronization.updateEnergyInGui(payload.storedEnergy(), payload.capacity());
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(DebugCablePacket.ID, (payload, context) -> {
			context.client().execute(() -> {
				DebugCablePacket.getCables(payload.blockPos(), payload.clean(), payload.isOwner());
			});
		});
		UseJetpackPacket.registerClientReceivers();

		HandledScreens.register(CyberbopMod.FURNACE_GENERATOR_SCREEN, FurnaceGeneratorClientScreen::new);
		HandledScreens.register(CyberbopMod.ASSEMBLER_SCREEN, AssemblerClientScreen::new);

		EntityModelLayerRegistry.registerModelLayer(WIRES_LAYER, WiresRenderer::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ASSEMBLER_LAYER, AssemblerModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CONTROLLER_LAYER, ControllerModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ERROR_LAYER, ErrorModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ADVANCED_CYBORG_LAYER, AdvancedCyborgModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(BASIC_CYBORG_LAYER, BasicCyborgModel::getTexturedModelData);

		BlockEntityRendererFactories.register(CyberbopBlockEntities.ASSEMBLER, ctx -> new AssemblerRenderer<>(ctx));
		BlockEntityRendererFactories.register(CyberbopBlockEntities.CONTROLLER, ctx -> new ControllerRenderer<>(ctx));
		BlockEntityRendererFactories.register(CyberbopBlockEntities.ENERGY_WIRE, ctx -> new WiresRenderer<>(ctx));

		HudRenderCallback.EVENT.register((context, tickDeltaManager) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			if(client.world != null) {
				renderDebug(client.world, client.player, context, tickDeltaManager);
				renderHud(client.world, client.player, context, tickDeltaManager);
			}

		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				if(client.player instanceof PlayerExtension ex && !ex.isCyborg() && !ClientOreHighlightData.isEmpty()) {
					ClientOreHighlightData.clearHighlights();
				}
			}
		});

		WorldRenderEvents.LAST.register((context) -> {
			OreHighlightRenderer.renderOutlines(context);
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
			else {
				context.drawTexture(BLUE_ENERGY_OVERLAY, x, y, 0, 0, width, 8, 81, 8);
			}
		}
	}
}
