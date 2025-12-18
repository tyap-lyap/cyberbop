package tyaplyap.cyberbop.client.model.module;

import net.minecraft.client.model.*;

public class ExtraBatteryModuleModel extends ModuleModel {
	public final ModelPart root;

	public ExtraBatteryModuleModel(ModelPart root) {
		this.root = root;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData root = modelData.getRoot();

		ModelPartData battery = root.addChild("battery", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, 0.01F, 0.01F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 16, 16);
	}

	@Override
	public ModelPart getRoot() {
		return this.root;
	}
}
