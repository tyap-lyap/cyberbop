package tyaplyap.cyberbop.client.model.module;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public abstract class ModuleModel extends Model {


	public ModuleModel() {
		super(RenderLayer::getEntityCutoutNoCull);
	}

	public abstract ModelPart getRoot();

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {

	}
}
