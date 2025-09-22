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
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.client.CyborgModel;
import tyaplyap.cyberbop.extension.LivingEntityRendererExtension;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.parts.CyborgParts;

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
		this.cyborgModel = new CyborgModel(ctx.getPart(CyberbopMod.CYBORG_LAYER));
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
			CyborgParts.BASIC_HEAD.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
			CyborgParts.ADVANCED_ARM.render(cyborgModel, matrixStack, vertexConsumerProvider, i, livingEntity);
		}
	}

	@Override
	public CyborgModel getCyborgModel() {
		return cyborgModel;
	}
}
