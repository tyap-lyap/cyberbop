package tyaplyap.cyberbop.client.model.module;

import net.minecraft.client.model.*;

public class FlightModuleModel extends ModuleModel {
	public final ModelPart root;

	public FlightModuleModel(ModelPart root) {
		this.root = root;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData root = modelData.getRoot();
		ModelPartData wings = root.addChild("wings", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = wings.addChild("cube_r1", ModelPartBuilder.create().uv(0, 8).cuboid(-16.0F, -4.0F, 0.0F, 16.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 3.0F, 2.0F, 0.0F, 0.6981F, 0.0F));

		ModelPartData cube_r2 = wings.addChild("cube_r2", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -4.0F, 0.0F, 16.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 3.0F, 2.0F, 0.0F, -0.6981F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public ModelPart getRoot() {
		return this.root;
	}
}
