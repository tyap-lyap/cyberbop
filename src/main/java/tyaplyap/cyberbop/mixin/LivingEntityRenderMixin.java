package tyaplyap.cyberbop.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tyaplyap.cyberbop.client.CyberbopModClient;
import tyaplyap.cyberbop.client.CyborgModel;
import tyaplyap.cyberbop.extension.LivingEntityRendererExtension;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.client.render.parts.CyborgPartRenderer;
import tyaplyap.cyberbop.client.render.parts.CyborgPartRenderers;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRenderMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M>, LivingEntityRendererExtension {

	@Shadow
	protected M model;
	protected M backModel;
	protected CyborgModel cyborgModel;

	protected LivingEntityRenderMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(EntityRendererFactory.Context ctx, EntityModel entityModel, float shadowRadius, CallbackInfo ci) {
		this.cyborgModel = new CyborgModel(ctx.getPart(CyberbopModClient.CYBORG_LAYER));
		this.backModel = model;
	}

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))

	public void render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (livingEntity instanceof PlayerExtension ex && ex.isCyborg()) {
			this.model = (M) cyborgModel;
			((BipedEntityModel<?>)this.model).setVisible(false);

		} else {
			this.model = backModel;
		}
	}

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.BEFORE))
	void afterRender(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (livingEntity instanceof PlayerExtension ex && ex.isCyborg()) {
//			CyborgParts.ADVANCED_HEAD.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
//			CyborgParts.ADVANCED_RIGHT_ARM.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
//			CyborgParts.ADVANCED_LEFT_ARM.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
//			CyborgParts.ADVANCED_BODY.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
//			CyborgParts.ADVANCED_RIGHT_LEG.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
//			CyborgParts.ADVANCED_LEFT_LEG.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);

			if(!ex.geCyborgHead().isBlank()) {
				CyborgPartRenderer part = CyborgPartRenderers.getPart(ex.geCyborgHead());
				if(part != null) part.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
			}
			if(!ex.getCyborgBody().isBlank()) {
				CyborgPartRenderer part = CyborgPartRenderers.getPart(ex.getCyborgBody());
				if(part != null) part.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
			}
			if(!ex.getCyborgRightArm().isBlank()) {
				CyborgPartRenderer part = CyborgPartRenderers.getPart(ex.getCyborgRightArm());
				if(part != null) part.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
			}
			if(!ex.getCyborgLeftArm().isBlank()) {
				CyborgPartRenderer part = CyborgPartRenderers.getPart(ex.getCyborgLeftArm());
				if(part != null) part.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
			}
			if(!ex.getCyborgRightLeg().isBlank()) {
				CyborgPartRenderer part = CyborgPartRenderers.getPart(ex.getCyborgRightLeg());
				if(part != null) part.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
			}
			if(!ex.getCyborgLeftLeg().isBlank()) {
				CyborgPartRenderer part = CyborgPartRenderers.getPart(ex.getCyborgLeftLeg());
				if(part != null) part.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
			}
		}
	}

	@Override
	public CyborgModel getCyborgModel() {
		return cyborgModel;
	}
}
