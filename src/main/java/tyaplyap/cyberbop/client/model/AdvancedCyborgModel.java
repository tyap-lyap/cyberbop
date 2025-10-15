package tyaplyap.cyberbop.client.model;

import net.minecraft.client.model.*;

public class AdvancedCyborgModel extends CyborgPartsModel {
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart right_arm;
	public final ModelPart left_arm;
	public final ModelPart right_leg;
	public final ModelPart left_leg;

	public AdvancedCyborgModel(ModelPart root) {
		super();
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
			.uv(0, 16).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 6.0F, 4.0F, new Dilation(0.0F))
			.uv(40, 55).cuboid(-3.0F, 6.0F, -1.0F, 6.0F, 3.0F, 2.0F, new Dilation(0.0F))
			.uv(24, 32).cuboid(-4.0F, 9.0F, -2.0F, 8.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(16, 42).cuboid(-0.5F, -2.0F, -1.5F, 1.0F, 12.0F, 3.0F, new Dilation(0.0F))
			.uv(48, 9).cuboid(-2.5F, -2.0F, -1.5F, 1.0F, 12.0F, 3.0F, new Dilation(0.0F))
			.uv(48, 31).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
			.uv(24, 55).cuboid(-3.0F, 3.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
			.uv(40, 48).cuboid(-3.0F, 7.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(16, 42).cuboid(1.5F, -2.0F, -1.5F, 1.0F, 12.0F, 3.0F, new Dilation(0.0F))
			.uv(48, 9).cuboid(-0.5F, -2.0F, -1.5F, 1.0F, 12.0F, 3.0F, new Dilation(0.0F))
			.uv(24, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
			.uv(0, 51).cuboid(-1.0F, 3.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
			.uv(48, 24).cuboid(-1.0F, 7.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 42).cuboid(-1.9F, 7.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
			.uv(48, 0).cuboid(-1.9F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
			.uv(56, 13).cuboid(-0.9F, 5.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
			.uv(32, 16).cuboid(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(24, 39).cuboid(-2.1F, 7.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
			.uv(40, 39).cuboid(-2.1F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
			.uv(56, 9).cuboid(-1.1F, 5.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
			.uv(32, 0).cuboid(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	public ModelPart getHead() {
		return head;
	}

	public ModelPart getBody() {
		return body;
	}

	public ModelPart getRightArm() {
		return right_arm;
	}

	public ModelPart getLeftArm() {
		return left_arm;
	}

	public ModelPart getRightLeg() {
		return right_leg;
	}

	public ModelPart getLeftLeg() {
		return left_leg;
	}

}

