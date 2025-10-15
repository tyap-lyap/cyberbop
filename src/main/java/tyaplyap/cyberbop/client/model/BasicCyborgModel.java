package tyaplyap.cyberbop.client.model;

import net.minecraft.client.model.*;

public class BasicCyborgModel extends CyborgPartsModel {
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart right_arm;
	public final ModelPart left_arm;
	public final ModelPart right_leg;
	public final ModelPart left_leg;

	public BasicCyborgModel(ModelPart root) {
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
		ModelPartData root = modelData.getRoot();

		ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
			.uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(36, 22).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 5.0F, 4.0F, new Dilation(0.0F))
			.uv(48, 16).cuboid(1.0F, 5.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
			.uv(40, 16).cuboid(-3.0F, 5.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
			.uv(36, 31).cuboid(-4.0F, 9.0F, -2.0F, 8.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData right_arm = root.addChild("right_arm", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
			.uv(14, 16).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F))
			.uv(0, 23).cuboid(-2.0F, 7.0F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
			.uv(22, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData left_arm = root.addChild("left_arm", ModelPartBuilder.create().uv(0, 30).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
			.uv(14, 30).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F))
			.uv(0, 37).cuboid(-1.0F, 7.0F, -2.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
			.uv(22, 30).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData right_leg = root.addChild("right_leg", ModelPartBuilder.create().uv(0, 54).cuboid(-0.9F, 6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
			.uv(0, 44).cuboid(-1.9F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
			.uv(16, 48).cuboid(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData left_leg = root.addChild("left_leg", ModelPartBuilder.create().uv(32, 54).cuboid(-1.1F, 6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
			.uv(32, 44).cuboid(-2.1F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
			.uv(48, 48).cuboid(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
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

