package tyaplyap.cyberbop.client.render.module;

import net.minecraft.block.BlockState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.model.DefaultedGeoModel;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.AssemblerBlock;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.item.LongArmModule;
import tyaplyap.cyberbop.util.RenderUtils;

public class LongArmModuleRenderer extends AnimatableModuleRenderer<LongArmModule> implements FirstPersonRender {

	public LongArmModuleRenderer() {
		super(new DefaultedGeoModel<>(Identifier.of(CyberbopMod.MOD_ID, "long_arm")) {
			@Override
			protected String subtype() {
				return "module";
			}
		});
	}

	@Override
	public void renderModule(ItemStack stack, PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float tickDelta) {
		this.animatable = (LongArmModule) stack.getItem();
		if (entity instanceof PlayerEntity player) {
			if (player.getMainArm() == Arm.RIGHT) {
				RenderUtils.setPositionGeoBone(this.getGeoModel().getBone("root"), -1f, 0, 0.0f, 22, contextModel.sneaking ? 3.2f : 0, contextModel.rightArm, 0, 90, 0, this.getGeoModel().getBone("local_root"));
			} else {
				RenderUtils.setPositionGeoBone(this.getGeoModel().getBone("root"), 1, 0, 0, 22, contextModel.sneaking ? 3.2f : 0, contextModel.leftArm, 0, 90, 0, this.getGeoModel().getBone("local_root"));
			}
			super.renderModule(stack, contextModel, matrices, vertexConsumers, light, entity, tickDelta);
		}
	}

	@Override
	public void renderModuleAssembler(ItemStack stack, AssemblerBlockEntity assembler, BlockState state, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		this.animatable = (LongArmModule) stack.getItem();
		matrices.push();
		matrices.translate(0.5, 1, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(AssemblerBlock.FACING).asRotation()));
		matrices.scale(0.95f,0.95f,0.95f);

		if (this.getGeoModel().getBone("root").isPresent()) {
			this.getGeoModel().getBone("root").get().updatePivot(0, 0, 0);
			this.getGeoModel().getBone("root").get().updatePosition(15, -30, -8);
		}

		super.renderModuleAssembler(stack, assembler, state, tickDelta, matrices, vertexConsumers, light, overlay);
		matrices.pop();
	}

	@Override
	public Identifier getTexture() {
		return CyberbopMod.id("textures/module/long_arm.png");
	}

	@Override
	public void renderLeftArm(ModelPart referenceModel, ItemStack stack, PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float tickDelta) {
		this.animatable = (LongArmModule) stack.getItem();
		this.currentItemStack = stack;
		RenderUtils.setPositionGeoBone(this.getGeoModel().getBone("root"),1f,0,0, 22, 0, referenceModel, this.getGeoModel().getBone("local_root"));
		this.render(matrices, animatable,  vertexConsumers, null, vertexConsumers.getBuffer(RenderLayer.getEntityCutout((this.getTexture()))), light, tickDelta);

	}

	@Override
	public void renderRightArm(ModelPart referenceModel, ItemStack stack, PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float tickDelta) {
		this.animatable = (LongArmModule) stack.getItem();
		this.currentItemStack = stack;
		RenderUtils.setPositionGeoBone(this.getGeoModel().getBone("root"),-1f,0,0, 22, 0, referenceModel, 0, 180, 0, this.getGeoModel().getBone("local_root"));
		this.render(matrices, animatable,  vertexConsumers, null, vertexConsumers.getBuffer(RenderLayer.getEntityCutout((this.getTexture()))), light, tickDelta);
	}
}
