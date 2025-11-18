package tyaplyap.cyberbop.client.render.module;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.model.DefaultedGeoModel;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.client.animation.module.LongArmModuleAnimation;
import tyaplyap.cyberbop.block.AssemblerBlock;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.client.model.module.ModuleModel;
import tyaplyap.cyberbop.client.render.ModelTestRenderer;
import tyaplyap.cyberbop.util.RenderUtils;

//TODO прикрепить к левой руке если игрок левша
public class LongArmModuleRenderer extends ModuleRenderer {
	ModelTestRenderer testRenderer;
	LongArmModuleAnimation animatable;
	LongArmModuleAnimation animatableAssembler;
	DefaultedGeoModel<LongArmModuleAnimation> geoModel;

	public LongArmModuleRenderer(String texture, ModuleModel model) {
		super(texture, model);

		animatable = new LongArmModuleAnimation();
		animatableAssembler = new LongArmModuleAnimation();

		geoModel = new DefaultedGeoModel<>(Identifier.of(CyberbopMod.MOD_ID, "long_arm")) {
			@Override
			protected String subtype() {
				return "module";
			}
		};
		testRenderer = new ModelTestRenderer(geoModel);
	}

	@Override
	public void render(PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity) {
		if (entity instanceof PlayerEntity player) {
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(CyberbopMod.id(this.texture)));
			if (player.handSwinging) {
				if (player.getWorld().isClient()) {
					animatable.getAnimatableInstanceCache().getManagerForId(player.getId()).tryTriggerAnimation("hook", "hook");
				}
			}

			if (player.getMainArm() == Arm.RIGHT) {
				RenderUtils.setPositionGeoBone(geoModel.getBone("root"), 0, 0, 0, 22, contextModel.sneaking ? 3.2f : 0, contextModel.rightArm, false);
			} else {
				RenderUtils.setPositionGeoBone(geoModel.getBone("root"), 0, 0, 0, 22, contextModel.sneaking ? 3.2f : 0, contextModel.leftArm, true);
			}
			testRenderer.render(matrices, animatable,  vertexConsumers, null, vertexConsumer, light, 0);
		}
	}



	@Override
	public void renderAssembler(AssemblerBlockEntity assembler, BlockState state, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();

		matrices.translate(0.5, 1, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(AssemblerBlock.FACING).asRotation()));
		matrices.scale(0.95f,0.95f,0.95f);

		if (geoModel.getBone("root").isPresent()) {
			geoModel.getBone("root").get().updatePivot(0, 0, 0);
			geoModel.getBone("root").get().updatePosition(15, -30, -8);
		}

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(CyberbopMod.id(this.texture)));
		testRenderer.render(matrices, animatableAssembler,  vertexConsumers, null, vertexConsumer, light, 0);
		matrices.pop();
	}
}
