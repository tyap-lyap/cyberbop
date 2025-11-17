package tyaplyap.cyberbop.client.model.module;

import net.minecraft.client.model.*;

public class JetpackModuleModel extends ModuleModel {
	public final ModelPart root;

	public JetpackModuleModel(ModelPart root) {
		this.root = root;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData jetpack = modelPartData.addChild("jetpack", ModelPartBuilder.create().uv(0, 0).cuboid(1.5F, 0.0F, 2.0F, 3.0F, 9.0F, 2.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-4.5F, 0.0F, 2.0F, 3.0F, 9.0F, 2.0F, new Dilation(0.0F))
			.uv(10, 0).cuboid(-1.5F, 2.0F, 2.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public ModelPart getRoot() {
		return this.root;
	}
}
