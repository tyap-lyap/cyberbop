package tyaplyap.cyberbop.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.model.DefaultedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.animation.module.AnimatableModule;
import tyaplyap.cyberbop.animation.module.LongArmModuleAnimation;
import tyaplyap.cyberbop.client.render.CyborgPartRenderers;
import tyaplyap.cyberbop.client.render.ModelTestRenderer;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.util.CyborgPartType;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	@Unique
	LongArmModuleAnimation animatable;
	@Unique
	DefaultedGeoModel<LongArmModuleAnimation> geoModel = new DefaultedGeoModel<>(Identifier.of(CyberbopMod.MOD_ID, "long_arm")) {
		@Override
		protected String subtype() {
			return "module";
		}
	};;
	@Shadow
	protected abstract void setModelPose(AbstractClientPlayerEntity player);

	public PlayerRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}
	@Inject(method = "<init>", at = @At("TAIL"))
	void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
		animatable = new LongArmModuleAnimation();
	}

	@Inject(method = "renderLeftArm", at = @At("HEAD"), cancellable = true)
	void renderLeftArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo ci) {
		if(player instanceof PlayerExtension ex && ex.isCyborg()) {
			var modelPart = CyborgPartRenderers.get(ex.getCyborgLeftArm(), CyborgPartType.LEFT_ARM);
			if(modelPart != null) {
				PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = this.getModel();
				this.setModelPose(player);
				playerEntityModel.handSwingProgress = 0.0F;
				playerEntityModel.sneaking = false;
				playerEntityModel.leaningPitch = 0.0F;
				playerEntityModel.setAngles(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
				modelPart.render(this.model, matrices, vertexConsumers, light, player);

				ci.cancel();
			}
		}
	}

	@Inject(method = "renderRightArm", at = @At("HEAD"), cancellable = true)
	void renderRightArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo ci) {
		if(player instanceof PlayerExtension ex && ex.isCyborg()) {
			var modelPart = CyborgPartRenderers.get(ex.getCyborgRightArm(), CyborgPartType.RIGHT_ARM);
			if(modelPart != null) {
				PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = this.getModel();
				this.setModelPose(player);
				playerEntityModel.handSwingProgress = 0.0F;
				playerEntityModel.sneaking = false;
				playerEntityModel.leaningPitch = 0.0F;
				playerEntityModel.setAngles(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

				modelPart.render(this.model, matrices, vertexConsumers, light, player);
				VertexConsumer vertexConsumer1 = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(CyberbopMod.id("textures/module/long_arm.png")));
				ModelTestRenderer testRenderer = new ModelTestRenderer(geoModel);

				//matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
				//matrices.translate(-0.9,0.2,-1.6);
				//matrices.scale(2,2,2);
				//matrices.translate(modelPart.model.get().getRightArm().pivotX, modelPart.model.get().getRightArm().pivotY,modelPart.model.get().getRightArm().pivotZ);

				if (player.handSwinging) {
					if (player.getWorld().isClient()) {
						animatable.getAnimatableInstanceCache().getManagerForId(player.getId()).tryTriggerAnimation("hook", "hook");
					}
				}
				testRenderer.render(matrices, animatable,  vertexConsumers, null, vertexConsumer1, light, 0);
//				if (geoModel.getBone("root").isPresent()) {
//					geoModel.getBone("root").get().setPivotX(5);
//					geoModel.getBone("root").get().setPivotY(22);
//					geoModel.getBone("root").get().setPivotZ(0);
//					geoModel.getBone("root").get().setRotX(this.model.rightArm.yaw);
//					geoModel.getBone("root").get().setRotY(this.model.rightArm.pitch);
//					geoModel.getBone("root").get().setRotZ(this.model.rightArm.roll);
//					geoModel.getBone("root").get().setModelPosition(new Vector3d(this.model.rightArm.pivotX, this.model.rightArm.pivotY, this.model.rightArm.pivotZ));
//				}
				ci.cancel();
			}
		}
	}


//	@ModifyVariable(method = "renderArm", at = @At("STORE"), ordinal = 0)
//	private Identifier renderArmTexture(Identifier identifier,MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve) {
//		return player instanceof PlayerExtension ex && ex.isCyborg() ? CyberbopMod.id("textures/entity/cyborg.png") : player.getSkinTextures().texture();
//	}
//
//	@ModifyVariable(method = "renderArm", at = @At("STORE"), ordinal = 0)
//	private PlayerEntityModel<AbstractClientPlayerEntity> renderArmModel(PlayerEntityModel<AbstractClientPlayerEntity> model,MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve) {
//		return player instanceof PlayerExtension ex && ex.isCyborg() ? ((LivingEntityRendererExtension)this).getCyborgModel() : getModel();
//	}
//
//	@ModifyArgs(method = "renderRightArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;)V"))
//	public void renderRightArm(Args args, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player) {
//		if(player instanceof PlayerExtension ex && ex.isCyborg()) {
//			args.set(4, ((LivingEntityRendererExtension)this).getCyborgModel().rightArm);
//			args.set(5, ((LivingEntityRendererExtension)this).getCyborgModel().rightSleeve);
//		} else {
//			args.set(4, this.model.rightArm);
//			args.set(5, this.model.rightSleeve);
//		}
//	}
//
//	@ModifyArgs(method = "renderLeftArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;)V"))
//	public void renderLeftArm(Args args, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player) {
//		if (player instanceof PlayerExtension ex && ex.isCyborg()) {
//			args.set(4, ((LivingEntityRendererExtension) this).getCyborgModel().leftArm);
//			args.set(5, ((LivingEntityRendererExtension) this).getCyborgModel().leftSleeve);
//		} else {
//			args.set(4, this.model.leftArm);
//			args.set(5, this.model.leftSleeve);
//		}
//	}
//
//		@Inject(method = "setModelPose", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isSpectator()Z"))
//	void setModelPose(AbstractClientPlayerEntity player, CallbackInfo ci, @Local LocalRef<PlayerEntityModel<AbstractClientPlayerEntity>> model) {
//		if(player instanceof PlayerExtension ex && ex.isCyborg()) {
//			model.set(((LivingEntityRendererExtension)this).getCyborgModel());
//		} else {
//			model.set(this.getModel());
//		}
//	}
//
//	@Inject(method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
//	void getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity, CallbackInfoReturnable<Identifier> cir) {
//		if(abstractClientPlayerEntity instanceof PlayerExtension ex && ex.isCyborg()) {
//			cir.setReturnValue(CyberbopMod.id("textures/entity/cyborg.png"));
//		}
//	}
}
