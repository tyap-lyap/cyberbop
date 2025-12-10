package tyaplyap.cyberbop.client.render.module;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.client.model.DefaultedModuleGeoModel;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.item.FlightModule;
import tyaplyap.cyberbop.util.RenderUtils;

public class FlightModuleRenderer extends AnimatableModuleRenderer<FlightModule> {


	public FlightModuleRenderer() {
		super(new DefaultedModuleGeoModel<>(Identifier.of(CyberbopMod.MOD_ID, "flight_module")));
	}

	@Override
	public void renderModule(ItemStack stack, PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float tickDelta) {
		this.animatable = (FlightModule) stack.getItem();
		RenderUtils.setPositionGeoBone(this.getGeoModel().getBone("root"), 0f, 2.5f, 2, 24, contextModel.sneaking ? 3.2f : 0f, contextModel.body, 180, 180, 0, this.getGeoModel().getBone("local_root"));

		super.renderModule(stack, contextModel, matrices, vertexConsumers, light, entity, tickDelta);
	}

	@Override
	public void renderModuleAssembler(AssemblerBlockEntity assembler, BlockState state, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		this.animatable = (FlightModule) CyberbopItems.FLIGHT_MODULE;
		RenderUtils.setPositionGeoBoneAssembler(this.getGeoModel().getBone("root"), 0, 24-3, 2, 0, 0, 0);
		super.renderModuleAssembler(assembler, state, tickDelta, matrices, vertexConsumers, light, overlay);
	}

	@Override
	public Identifier getTexture() {
		return CyberbopMod.id("textures/module/flight_module.png");
	}
}
