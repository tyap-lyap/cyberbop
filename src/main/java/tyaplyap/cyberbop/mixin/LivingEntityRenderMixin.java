package tyaplyap.cyberbop.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.client.CyborgModel;
import tyaplyap.cyberbop.extension.LivingEntityRendererExtension;
import tyaplyap.cyberbop.extension.PlayerExtension;

import java.util.List;


@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRenderMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M>, LivingEntityRendererExtension {

	@Shadow
	protected abstract float getHandSwingProgress(T entity, float tickDelta);

	@Shadow
	public static boolean shouldFlipUpsideDown(LivingEntity entity) {
		return false;
	}

	@Shadow
	protected abstract float getAnimationProgress(T entity, float tickDelta);

	@Shadow
	protected abstract void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale);

	@Shadow
	protected abstract void scale(T entity, MatrixStack matrices, float amount);

	@Shadow
	protected abstract boolean isVisible(T entity);

	@Shadow
	public static int getOverlay(LivingEntity entity, float whiteOverlayProgress) {
		return 0;
	}

	@Shadow
	protected abstract float getAnimationCounter(T entity, float tickDelta);

	@Shadow
	@Final
	protected List<FeatureRenderer<T, M>> features;
	protected CyborgModel cyborgModel;

	protected LivingEntityRenderMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(EntityRendererFactory.Context ctx, EntityModel model, float shadowRadius, CallbackInfo ci) {
		this.cyborgModel = new CyborgModel(ctx.getPart(CyberbopMod.CYBORG_LAYER));
	}

	@Inject(method = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
	void render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if(livingEntity instanceof PlayerExtension ex && ex.isCyborg()) {
			altRender(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
			ci.cancel();
		}
	}

	@Override
	public CyborgModel getCyborgModel() {
		return cyborgModel;
	}

	public void altRender(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		cyborgModel.handSwingProgress = this.getHandSwingProgress(livingEntity, g);
		cyborgModel.riding = livingEntity.hasVehicle();
		cyborgModel.child = livingEntity.isBaby();
		float h = MathHelper.lerpAngleDegrees(g, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
		float j = MathHelper.lerpAngleDegrees(g, livingEntity.prevHeadYaw, livingEntity.headYaw);
		float k = j - h;
		if (livingEntity.hasVehicle() && livingEntity.getVehicle() instanceof LivingEntity livingEntity2) {
			h = MathHelper.lerpAngleDegrees(g, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
			k = j - h;
			float l = MathHelper.wrapDegrees(k);
			if (l < -85.0F) {
				l = -85.0F;
			}

			if (l >= 85.0F) {
				l = 85.0F;
			}

			h = j - l;
			if (l * l > 2500.0F) {
				h += l * 0.2F;
			}

			k = j - h;
		}

		float m = MathHelper.lerp(g, livingEntity.prevPitch, livingEntity.getPitch());
		if (shouldFlipUpsideDown(livingEntity)) {
			m *= -1.0F;
			k *= -1.0F;
		}

		k = MathHelper.wrapDegrees(k);
		if (livingEntity.isInPose(EntityPose.SLEEPING)) {
			Direction direction = livingEntity.getSleepingDirection();
			if (direction != null) {
				float n = livingEntity.getEyeHeight(EntityPose.STANDING) - 0.1F;
				matrixStack.translate(-direction.getOffsetX() * n, 0.0F, -direction.getOffsetZ() * n);
			}
		}

		float lx = livingEntity.getScale();
		matrixStack.scale(lx, lx, lx);
		float n = this.getAnimationProgress(livingEntity, g);
		this.setupTransforms(livingEntity, matrixStack, n, h, g, lx);
		matrixStack.scale(-1.0F, -1.0F, 1.0F);
		this.scale(livingEntity, matrixStack, g);
		matrixStack.translate(0.0F, -1.501F, 0.0F);
		float o = 0.0F;
		float p = 0.0F;
		if (!livingEntity.hasVehicle() && livingEntity.isAlive()) {
			o = livingEntity.limbAnimator.getSpeed(g);
			p = livingEntity.limbAnimator.getPos(g);
			if (livingEntity.isBaby()) {
				p *= 3.0F;
			}

			if (o > 1.0F) {
				o = 1.0F;
			}
		}

		cyborgModel.animateModel((AbstractClientPlayerEntity) livingEntity, p, o, g);
		cyborgModel.setAngles((AbstractClientPlayerEntity) livingEntity, p, o, n, k, m);
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		boolean bl = isVisible(livingEntity);
		boolean bl2 = !bl && !livingEntity.isInvisibleTo(minecraftClient.player);
		boolean bl3 = minecraftClient.hasOutline(livingEntity);
		RenderLayer renderLayer = altGetRenderLayer(livingEntity, bl, bl2, bl3);
		if (renderLayer != null) {
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
			int q = getOverlay(livingEntity, getAnimationCounter(livingEntity, g));
			cyborgModel.render(matrixStack, vertexConsumer, i, q, bl2 ? 654311423 : -1);
		}

		if (!livingEntity.isSpectator()) {
			for (FeatureRenderer<T, M> featureRenderer : features) {
				featureRenderer.render(matrixStack, vertexConsumerProvider, i, livingEntity, p, o, g, n, k, m);
			}
		}

		matrixStack.pop();
		super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Nullable
	protected RenderLayer altGetRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline) {
		Identifier identifier = this.getTexture(entity);
		if (translucent) {
			return RenderLayer.getItemEntityTranslucentCull(identifier);
		} else if (showBody) {
			return cyborgModel.getLayer(identifier);
		} else {
			return showOutline ? RenderLayer.getOutline(identifier) : null;
		}
	}
}
