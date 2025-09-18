package tyaplyap.cyberbop.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.extension.LivingEntityRendererExtension;
import tyaplyap.cyberbop.extension.PlayerExtension;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public PlayerRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@ModifyVariable(method = "renderArm", at = @At("STORE"), ordinal = 0)
	private Identifier renderArmTexture(Identifier identifier,MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve) {
		return player instanceof PlayerExtension ex && ex.isCyborg() ? CyberbopMod.id("textures/entity/cyborg.png") : player.getSkinTextures().texture();
	}

	@ModifyVariable(method = "renderArm", at = @At("STORE"), ordinal = 0)
	private PlayerEntityModel<AbstractClientPlayerEntity> renderArmModel(PlayerEntityModel<AbstractClientPlayerEntity> model,MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve) {
		return player instanceof PlayerExtension ex && ex.isCyborg() ? ((LivingEntityRendererExtension)this).getCyborgModel() : getModel();
	}

	@ModifyArgs(method = "renderRightArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;)V"))
	public void renderRightArm(Args args, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player) {
		if(player instanceof PlayerExtension ex && ex.isCyborg()) {
			args.set(4, ((LivingEntityRendererExtension)this).getCyborgModel().rightArm);
			args.set(5, ((LivingEntityRendererExtension)this).getCyborgModel().rightSleeve);
		} else {
			args.set(4, this.model.rightArm);
			args.set(5, this.model.rightSleeve);
		}
	}

	@ModifyArgs(method = "renderLeftArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;)V"))
	public void renderLeftArm(Args args, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player) {
		if (player instanceof PlayerExtension ex && ex.isCyborg()) {
			args.set(4, ((LivingEntityRendererExtension) this).getCyborgModel().leftArm);
			args.set(5, ((LivingEntityRendererExtension) this).getCyborgModel().leftSleeve);
		} else {
			args.set(4, this.model.leftArm);
			args.set(5, this.model.leftSleeve);
		}
	}

		@Inject(method = "setModelPose", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isSpectator()Z"))
	void setModelPose(AbstractClientPlayerEntity player, CallbackInfo ci, @Local LocalRef<PlayerEntityModel<AbstractClientPlayerEntity>> model) {
		if(player instanceof PlayerExtension ex && ex.isCyborg()) {
			model.set(((LivingEntityRendererExtension)this).getCyborgModel());
		} else {
			model.set(this.getModel());
		}
	}

	@Inject(method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
	void getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity, CallbackInfoReturnable<Identifier> cir) {
		if(abstractClientPlayerEntity instanceof PlayerExtension ex && ex.isCyborg()) {
			cir.setReturnValue(CyberbopMod.id("textures/entity/cyborg.png"));
		}
	}
}
