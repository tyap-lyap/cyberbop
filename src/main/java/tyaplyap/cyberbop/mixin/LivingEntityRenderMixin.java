package tyaplyap.cyberbop.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tyaplyap.cyberbop.client.render.module.ModuleRenderer;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.client.render.CyborgPartRenderer;
import tyaplyap.cyberbop.client.render.CyborgPartRenderers;
import tyaplyap.cyberbop.item.CyborgModuleItem;
import tyaplyap.cyberbop.util.CyborgPartType;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRenderMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {

	@Shadow
	protected M model;

	protected LivingEntityRenderMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	void init(EntityRendererFactory.Context ctx, EntityModel model, float shadowRadius, CallbackInfo ci) {
		CyborgPartRenderers.init(ctx);
		ModuleRenderer.init(ctx);
	}

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
	public void render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (livingEntity instanceof PlayerExtension ex && ex.isCyborg()) {
			((BipedEntityModel<?>)this.model).setVisible(false);
		}
	}

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.BEFORE))
	void afterRender(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (livingEntity instanceof PlayerExtension ex && ex.isCyborg()) {

			CyborgPartType.forEach(partType -> {
				CyborgPartRenderer renderer = CyborgPartRenderers.get(ex.getCyborgPart(partType), partType);
				if(renderer != null) renderer.render((PlayerEntityModel<?>)model, matrixStack, vertexConsumerProvider, i, livingEntity);
			});

			for(ItemStack stack : ex.getModules()) {
				if(stack.getItem() instanceof CyborgModuleItem module) {
					if(module.getModuleRenderer() != null) module.getModuleRenderer().render((PlayerEntityModel<?>)model, matrixStack, vertexConsumerProvider, i, livingEntity);
				}
			}
		}
	}
}
