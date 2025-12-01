package tyaplyap.cyberbop.client.render;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.AssemblerBlock;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.client.CyberbopModClient;
import tyaplyap.cyberbop.client.render.debug.DebugRender;
import tyaplyap.cyberbop.item.AnimatableCyborgModule;
import tyaplyap.cyberbop.item.CyborgModuleItem;
import tyaplyap.cyberbop.util.CyborgPartType;

import java.util.Map;

public class AssemblerRenderer<T extends AssemblerBlockEntity> implements BlockEntityRenderer<T> {

	ModelPart model;
	ModelPart overlayPart;
	ModelPart basePart;
	ModelPart errorModel;

	final Map<CyborgPartType, Vec3d> errorPosForPart = Map.of(
		CyborgPartType.HEAD, new Vec3d(0.5, 4.3, 0.5),
		CyborgPartType.BODY, new Vec3d(0.5, 3.7, 0.5),
		CyborgPartType.RIGHT_ARM, new Vec3d(1, 3.8, 0.5),
		CyborgPartType.LEFT_ARM, new Vec3d(0, 3.8, 0.5),
		CyborgPartType.RIGHT_LEG, new Vec3d(0.7, 3.1, 0.5),
		CyborgPartType.LEFT_LEG, new Vec3d(0.3, 3.1, 0.5)
	);

	public AssemblerRenderer(BlockEntityRendererFactory.Context ctx) {
		model = ctx.getLayerModelPart(CyberbopModClient.ASSEMBLER_LAYER);
		overlayPart = model.getChild("overlay");
		basePart = model.getChild("base");
		errorModel = ctx.getLayerModelPart(CyberbopModClient.ERROR_LAYER);
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var world = entity.getWorld();

		if(world != null && !entity.isRemoved()) {
			matrices.push();
			matrices.translate(0.5, 1.5, 0.5);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.getCachedState().get(AssemblerBlock.FACING).asRotation())));

			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(CyberbopMod.id("textures/entity/assembler.png")));

			basePart.visible = false;
			overlayPart.visible = false;
			model.render(matrices, vertexConsumer, light, overlay);

			matrices.pop();

			CyborgPartType.forEach(partType -> {
				CyborgPartRenderer renderer = CyborgPartRenderers.get(entity.getPartStack(partType), partType);
				if(renderer != null) {
					renderer.renderAssembler(entity, entity.getCachedState(), tickDelta, matrices, vertexConsumers, light, overlay);
				}
				else {
					if(!entity.getPartStack(partType).isEmpty()) {
						renderError(matrices, tickDelta, vertexConsumers, entity.tickError, overlay, errorPosForPart.get(partType));
					}
				}
			});

			for(ItemStack stack : entity.getItems()) {
				if(stack.getItem() instanceof CyborgModuleItem module) {
					if(module.getModuleRenderer() != null) module.getModuleRenderer().renderAssembler(entity, entity.getCachedState(), tickDelta, matrices, vertexConsumers, light, overlay);
				}
				if(stack.getItem() instanceof AnimatableCyborgModule module) {
					if(module.getModuleRenderer() != null) module.getModuleRenderer().renderModuleAssembler(stack, entity, entity.getCachedState(), tickDelta, matrices, vertexConsumers, light, overlay);
				}
			}

			renderOverlay(entity, tickDelta, matrices, vertexConsumers, light, overlay);
			if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
				DebugRender.DebugRender(entity, tickDelta, matrices, vertexConsumers,  light,  overlay);
			}
		}
	}

	public void renderError (MatrixStack matrices, float tickDelta, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d vec3d) {
		matrices.push();
		matrices.translate(vec3d.getX(), vec3d.getY(), vec3d.getZ());
		matrices.scale(1.6f,1.6f,1.6f);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MinecraftClient.getInstance().player.age * 5));

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(CyberbopMod.id("textures/entity/error_tex.png")));

		errorModel.render(matrices, vertexConsumer, MathHelper.clamp(light, 20, 255), overlay);
		matrices.pop();

	}

	public void renderOverlay(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		matrices.translate(0.5, 1.5, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.getCachedState().get(AssemblerBlock.FACING).asRotation())));

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(CyberbopMod.id("textures/entity/assembler_overlay.png")));
		basePart.visible = false;
		overlayPart.visible = true;
		overlayPart.render(matrices, vertexConsumer, light, overlay);

		matrices.pop();
	}
}
