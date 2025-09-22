package tyaplyap.cyberbop.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.client.render.AssemblerRenderer;

public class CyberbopModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererFactories.register(CyberbopBlockEntities.ASSEMBLER, ctx -> new AssemblerRenderer<>());
	}
}
