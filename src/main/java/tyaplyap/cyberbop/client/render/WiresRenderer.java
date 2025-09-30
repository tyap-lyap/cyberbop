package tyaplyap.cyberbop.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.EnergyWireBlock;
import tyaplyap.cyberbop.block.entity.EnergyWireBlockEntity;
import tyaplyap.cyberbop.client.CyberbopModClient;

public class WiresRenderer<T extends EnergyWireBlockEntity> implements BlockEntityRenderer<T> {

	public final ModelPart mid;
	public final ModelPart up;
	public final ModelPart down;
	public final ModelPart north;
	public final ModelPart west;
	public final ModelPart east;
	public final ModelPart south;

	public WiresRenderer(BlockEntityRendererFactory.Context ctx) {
		ModelPart modelPart = ctx.getLayerModelPart(CyberbopModClient.WIRES_LAYER);
		this.mid = modelPart.getChild("mid");
		this.up = modelPart.getChild("up");
		this.down = modelPart.getChild("down");
		this.north = modelPart.getChild("north");
		this.west = modelPart.getChild("west");
		this.east = modelPart.getChild("east");
		this.south = modelPart.getChild("south");

	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("mid", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -11.0F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		modelPartData.addChild("up", ModelPartBuilder.create().uv(0, 12).cuboid(-3.0F, -16.0F, -3.0F, 6.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		modelPartData.addChild("down", ModelPartBuilder.create().uv(0, 23).cuboid(-3.0F, -5.0F, -3.0F, 6.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		modelPartData.addChild("north", ModelPartBuilder.create().uv(24, 24).cuboid(-3.0F, -11.0F, -8.0F, 6.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		modelPartData.addChild("west", ModelPartBuilder.create().uv(24, 0).cuboid(3.0F, -11.0F, -3.0F, 5.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		modelPartData.addChild("east", ModelPartBuilder.create().uv(24, 12).cuboid(-8.0F, -11.0F, -3.0F, 5.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		modelPartData.addChild("south", ModelPartBuilder.create().uv(0, 34).cuboid(-3.0F, -11.0F, 3.0F, 6.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

		if (entity.getWorld() != null && !entity.isRemoved()) {
			BlockState state = entity.getWorld().getBlockState(entity.getPos());

			matrices.push();
			matrices.translate(0.5, 1.5, 0.5);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));



			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(CyberbopMod.id("textures/entity/wires.png")));

			mid.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

			if(state.get(EnergyWireBlock.UP)) {
				up.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
			};
			if(state.get(EnergyWireBlock.DOWN)) {
				down.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
			}
			if(state.get(EnergyWireBlock.NORTH)) {
				north.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
			}
			if(state.get(EnergyWireBlock.WEST)) {
				west.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
			}
			if(state.get(EnergyWireBlock.EAST)) {
				east.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
			}
			if(state.get(EnergyWireBlock.SOUTH)) {
				south.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
			}
			matrices.pop();

		}


	}
}
