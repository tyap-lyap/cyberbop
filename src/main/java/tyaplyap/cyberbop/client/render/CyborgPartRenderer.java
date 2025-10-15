package tyaplyap.cyberbop.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.client.model.CyborgPartsModel;

import java.util.function.Supplier;

public abstract class CyborgPartRenderer {

	public String name;
	public String texture;
	public Supplier<CyborgPartsModel> model;

	public CyborgPartRenderer(String name, String texture, Supplier<CyborgPartsModel> model) {
		this.name = name;
		this.texture = texture;
		this.model = model;
	}

	abstract public void render(PlayerEntityModel<?> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity);
	abstract public void renderAssembler(AssemblerBlockEntity assembler, BlockState state, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay);
}
