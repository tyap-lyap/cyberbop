package tyaplyap.cyberbop.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.CyberbopBlocks;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.client.hud.CyberbopHud;
import tyaplyap.cyberbop.client.model.*;
import tyaplyap.cyberbop.client.model.debug.ErrorModel;
import tyaplyap.cyberbop.client.render.*;
import tyaplyap.cyberbop.client.screen.AssemblerClientScreen;
import tyaplyap.cyberbop.client.screen.FurnaceGeneratorClientScreen;
import tyaplyap.cyberbop.client.util.ClientOreHighlightData;
import tyaplyap.cyberbop.client.util.EnergySynchronization;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.packet.DebugCablePacket;
import tyaplyap.cyberbop.packet.EnergyGuiUpdatePacket;
import tyaplyap.cyberbop.packet.UseJetpackPacket;

import tyaplyap.cyberbop.client.render.debug.DebugEnergyRenderer;

public class CyberbopModClient implements ClientModInitializer {

	public static final EntityModelLayer BATTERY_BLOCK_LAYER = new EntityModelLayer(CyberbopMod.id("battery_block"), "main");
	public static final EntityModelLayer WIRES_LAYER = new EntityModelLayer(CyberbopMod.id("wires"), "main");
	public static final EntityModelLayer ASSEMBLER_LAYER = new EntityModelLayer(CyberbopMod.id("assembler"), "main");
	public static final EntityModelLayer CONTROLLER_LAYER = new EntityModelLayer(CyberbopMod.id("controller"), "main");
	public static final EntityModelLayer SOLID_FUEL_GENERATOR_LAYER = new EntityModelLayer(CyberbopMod.id("solid_fuel_generator"), "main");
	public static final EntityModelLayer ERROR_LAYER = new EntityModelLayer(CyberbopMod.id("error"), "main");
	public static final EntityModelLayer ADVANCED_CYBORG_LAYER = new EntityModelLayer(CyberbopMod.id("advanced_cyborg"), "main");
	public static final EntityModelLayer BASIC_CYBORG_LAYER = new EntityModelLayer(CyberbopMod.id("basic_cyborg"), "main");

	@Override
	public void onInitializeClient() {
		//TEST//
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			BlockEntityRendererFactories.register(CyberbopBlockEntities.ENERGY_GENERATOR, (BlockEntityRendererFactory.Context ctx) -> new DebugEnergyRenderer(CyberbopBlocks.ENERGY_GENERATOR, ctx));
			BlockEntityRendererFactories.register(CyberbopBlockEntities.ENERGY_RECEIVER, (BlockEntityRendererFactory.Context ctx) -> new DebugEnergyRenderer(CyberbopBlocks.ENERGY_RECEIVER, ctx));
			BlockEntityRendererFactories.register(CyberbopBlockEntities.SOLAR_PANEL, (BlockEntityRendererFactory.Context ctx) -> new DebugEnergyRenderer(CyberbopBlocks.SOLAR_PANEL, ctx));
			BlockEntityRendererFactories.register(CyberbopBlockEntities.BATTERY_TEST, (BlockEntityRendererFactory.Context ctx) -> new DebugEnergyRenderer(CyberbopBlocks.BATTERY_TEST, ctx));
			BlockEntityRendererFactories.register(CyberbopBlockEntities.CHARGING_PAD, (BlockEntityRendererFactory.Context ctx) -> new DebugEnergyRenderer(CyberbopBlocks.CHARGING_PAD, ctx));
			BlockEntityRendererFactories.register(CyberbopBlockEntities.SOLID_FUEL_GENERATOR, (BlockEntityRendererFactory.Context ctx) -> new DebugEnergyRenderer(CyberbopBlocks.SOLID_FUEL_GENERATOR, ctx));
		}
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

		EntityModelLayerRegistry.registerModelLayer(BATTERY_BLOCK_LAYER, BatteryModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WIRES_LAYER, WiresRenderer::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ASSEMBLER_LAYER, AssemblerModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CONTROLLER_LAYER, ControllerModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SOLID_FUEL_GENERATOR_LAYER, CubeModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ERROR_LAYER, ErrorModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ADVANCED_CYBORG_LAYER, AdvancedCyborgModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(BASIC_CYBORG_LAYER, BasicCyborgModel::getTexturedModelData);

		BlockEntityRendererFactories.register(CyberbopBlockEntities.ASSEMBLER, ctx -> new AssemblerRenderer<>(ctx));
		BlockEntityRendererFactories.register(CyberbopBlockEntities.CONTROLLER, ctx -> new ControllerRenderer<>(ctx));
		BlockEntityRendererFactories.register(CyberbopBlockEntities.SOLID_FUEL_GENERATOR, ctx -> new SolidFuelGeneratorRenderer<>(ctx));
		BlockEntityRendererFactories.register(CyberbopBlockEntities.ENERGY_WIRE, ctx -> new WiresRenderer<>(ctx));
		BlockEntityRendererFactories.register(CyberbopBlockEntities.BATTERY_BLOCK, ctx -> new BatteryRenderer<>(ctx));

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

		CyberbopHud.init();
	}
}
