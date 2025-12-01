package tyaplyap.cyberbop.client.render.module;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.item.AnimatableCyborgModule;

public abstract class AnimatableModuleRenderer<M extends AnimatableCyborgModule> extends GeoObjectRenderer<M> {

	protected ItemStack currentItemStack;

	public abstract Identifier getTexture();

	public AnimatableModuleRenderer(GeoModel<M> model) {
		super(model);
	}

	@Override
	public long getInstanceId(M animatable) {
		return GeoItem.getId(this.currentItemStack);
	}

	@Override
	public void doPostRenderCleanup() {
		this.animatable = null;
		this.currentItemStack = null;
	}

	@Override
	public void actuallyRender(MatrixStack poseStack, M animatable, BakedGeoModel model, @Nullable RenderLayer renderType,
							   VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick,
							   int packedLight, int packedOverlay, int colour) {

		if (!isReRender) {
			AnimationState<M> animationState = new AnimationState<>(animatable, 0, 0, partialTick, false);
			long instanceId = getInstanceId(animatable);
			GeoModel<M> currentModel = getGeoModel();

			animationState.setData(DataTickets.TICK, animatable.getTick(animatable));
			animationState.setData(DataTickets.ITEMSTACK, this.currentItemStack);
			currentModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
			currentModel.handleAnimations(animatable, instanceId, animationState, partialTick);
		}

		this.modelRenderTranslations = new Matrix4f(poseStack.peek().getPositionMatrix());

		if (buffer != null)
			if (buffer == null) {
				if (renderType == null)
					return;

				buffer = bufferSource.getBuffer(renderType);
			}

		updateAnimatedTextureFrame(animatable);

		for (GeoBone group : model.topLevelBones()) {
			renderRecursively(poseStack, animatable, group, renderType, bufferSource, buffer, isReRender, partialTick, packedLight,
				packedOverlay, colour);
		}
	}

	public void renderModule(ItemStack stack, PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float g) {
		this.currentItemStack = stack;
		super.render(matrices, animatable,  vertexConsumers, null, vertexConsumers.getBuffer(RenderLayer.getEntityCutout((this.getTexture()))), light, g);
	}

	public void renderModuleAssembler(ItemStack stack, AssemblerBlockEntity assembler, BlockState state, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		this.currentItemStack = stack;
		super.render(matrices, animatable,  vertexConsumers, null, vertexConsumers.getBuffer(RenderLayer.getEntityCutout((this.getTexture()))), light, tickDelta);
	}
}
