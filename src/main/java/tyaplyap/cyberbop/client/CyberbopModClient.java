package tyaplyap.cyberbop.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.client.render.AssemblerRenderer;
import tyaplyap.cyberbop.client.render.WiresRenderer;
import tyaplyap.cyberbop.extension.PlayerExtension;

public class CyberbopModClient implements ClientModInitializer {

	public static final EntityModelLayer WIRES_LAYER = new EntityModelLayer(CyberbopMod.id("wires"), "main");

	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(WIRES_LAYER, WiresRenderer::getTexturedModelData);

		BlockEntityRendererFactories.register(CyberbopBlockEntities.ASSEMBLER, ctx -> new AssemblerRenderer<>());
		BlockEntityRendererFactories.register(CyberbopBlockEntities.ENERGY_WIRE_BLOCK_ENTITY, ctx -> new WiresRenderer<>(ctx));

		HudRenderCallback.EVENT.register((context, tickDeltaManager) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			if(client.world != null && client.player instanceof PlayerExtension ex && ex.isCyborg()) {
				context.drawText(MinecraftClient.getInstance().textRenderer, "Cyborg Energy: " + ex.getCyborgEnergy(), 10, 200, 0xFFFFFFFF, true);
			}

		});
	}
}
