package tyaplyap.cyberbop.client.render.module;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.client.CyberbopModClient;
import tyaplyap.cyberbop.client.model.module.FlightModuleModel;
import tyaplyap.cyberbop.client.model.module.JetpackModuleModel;
import tyaplyap.cyberbop.client.model.module.ModuleModel;
import tyaplyap.cyberbop.item.*;

public abstract class ModuleRenderer {
	private static boolean initialized = false;

	public String texture;
	public ModuleModel model;

	public ModuleRenderer(String texture, ModuleModel model) {
		this.texture = texture;
		this.model = model;
	}

	abstract public void render(PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity);
	abstract public void renderAssembler(AssemblerBlockEntity assembler, BlockState state, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay);

	public static void init(EntityRendererFactory.Context context) {
		if(!initialized) {
			((JetpackModule)CyberbopItems.JETPACK_MODULE).setModuleRenderer(new JetpackModuleRenderer("textures/entity/jetpack_module.png", new JetpackModuleModel(context.getPart(CyberbopModClient.JETPACK_MODULE_LAYER))));
			//((FlightModule)CyberbopItems.FLIGHT_MODULE).setModuleRenderer(new FlightModuleRenderer("textures/entity/flight_module.png", new FlightModuleModel(context.getPart(CyberbopModClient.FLIGHT_MODULE_LAYER))));
			((LongArmModule)CyberbopItems.LONG_ARM_MODULE).setModuleRenderer(new LongArmModuleRenderer());
			((FlightModule)CyberbopItems.FLIGHT_MODULE).setModuleRenderer(new FlightModuleRenderer());

			initialized = true;
		}
	}

}
