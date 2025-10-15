package tyaplyap.cyberbop.client.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.item.ItemStack;
import tyaplyap.cyberbop.client.CyberbopModClient;
import tyaplyap.cyberbop.client.model.AdvancedCyborgModel;
import tyaplyap.cyberbop.client.model.BasicCyborgModel;
import tyaplyap.cyberbop.client.render.parts.*;
import tyaplyap.cyberbop.item.CyborgPartItem;
import tyaplyap.cyberbop.util.CyborgPartType;

import java.util.ArrayList;

public class CyborgPartRenderers {
	public static final ArrayList<CyborgPartRenderer> PARTS = new ArrayList<>();

	public static final CyborgPartRenderer BASIC_HEAD = register(new HeadPartRenderer("basic_head", "textures/entity/basic_cyborg.png", () -> CyborgPartRenderers.basic));
	public static final CyborgPartRenderer BASIC_RIGHT_ARM = register(new RightArmPartRenderer("basic_right_arm", "textures/entity/basic_cyborg.png", () -> CyborgPartRenderers.basic));
	public static final CyborgPartRenderer BASIC_LEFT_ARM = register(new LeftArmPartRenderer("basic_left_arm", "textures/entity/basic_cyborg.png", () -> CyborgPartRenderers.basic));
	public static final CyborgPartRenderer BASIC_BODY = register(new BodyPartRenderer("basic_body", "textures/entity/basic_cyborg.png", () -> CyborgPartRenderers.basic));
	public static final CyborgPartRenderer BASIC_RIGHT_LEG = register(new RightLegPartRenderer("basic_right_leg", "textures/entity/basic_cyborg.png", () -> CyborgPartRenderers.basic));
	public static final CyborgPartRenderer BASIC_LEFT_LEG = register(new LeftLegPartRenderer("basic_left_leg", "textures/entity/basic_cyborg.png", () -> CyborgPartRenderers.basic));

	public static final CyborgPartRenderer GOLDEN_HEAD = register(new HeadPartRenderer("golden_head", "textures/entity/golden_cyborg.png", () -> CyborgPartRenderers.basic));
	public static final CyborgPartRenderer GOLDEN_RIGHT_ARM = register(new RightArmPartRenderer("golden_right_arm", "textures/entity/golden_cyborg.png", () -> CyborgPartRenderers.basic));
	public static final CyborgPartRenderer GOLDEN_LEFT_ARM = register(new LeftArmPartRenderer("golden_left_arm", "textures/entity/golden_cyborg.png", () -> CyborgPartRenderers.basic));
	public static final CyborgPartRenderer GOLDEN_BODY = register(new BodyPartRenderer("golden_body", "textures/entity/golden_cyborg.png", () -> CyborgPartRenderers.basic));
	public static final CyborgPartRenderer GOLDEN_RIGHT_LEG = register(new RightLegPartRenderer("golden_right_leg", "textures/entity/golden_cyborg.png", () -> CyborgPartRenderers.basic));
	public static final CyborgPartRenderer GOLDEN_LEFT_LEG = register(new LeftLegPartRenderer("golden_left_leg", "textures/entity/golden_cyborg.png", () -> CyborgPartRenderers.basic));

	public static final CyborgPartRenderer ADVANCED_HEAD = register(new HeadPartRenderer("advanced_head", "textures/entity/advanced_cyborg.png", () -> CyborgPartRenderers.advanced));
	public static final CyborgPartRenderer ADVANCED_RIGHT_ARM = register(new RightArmPartRenderer("advanced_right_arm", "textures/entity/advanced_cyborg.png", () -> CyborgPartRenderers.advanced));
	public static final CyborgPartRenderer ADVANCED_LEFT_ARM = register(new LeftArmPartRenderer("advanced_left_arm", "textures/entity/advanced_cyborg.png", () -> CyborgPartRenderers.advanced));
	public static final CyborgPartRenderer ADVANCED_BODY = register(new BodyPartRenderer("advanced_body", "textures/entity/advanced_cyborg.png", () -> CyborgPartRenderers.advanced));
	public static final CyborgPartRenderer ADVANCED_RIGHT_LEG = register(new RightLegPartRenderer("advanced_right_leg", "textures/entity/advanced_cyborg.png", () -> CyborgPartRenderers.advanced));
	public static final CyborgPartRenderer ADVANCED_LEFT_LEG = register(new LeftLegPartRenderer("advanced_left_leg", "textures/entity/advanced_cyborg.png", () -> CyborgPartRenderers.advanced));

	public static CyborgPartRenderer register(CyborgPartRenderer part) {
		PARTS.add(part);
		return part;
	}

	public static AdvancedCyborgModel advanced;
	public static BasicCyborgModel basic;
	public static boolean initialized = false;

	public static void init(EntityRendererFactory.Context context) {
		if(!initialized) {
			advanced = new AdvancedCyborgModel(context.getPart(CyberbopModClient.ADVANCED_CYBORG_LAYER));
			basic = new BasicCyborgModel(context.getPart(CyberbopModClient.BASIC_CYBORG_LAYER));
			initialized = true;
		}
	}

	public static CyborgPartRenderer get(ItemStack item, CyborgPartType partType) {
		if(item.getItem() instanceof CyborgPartItem partItem && partItem.getPartName(partType) != null) {
			for(CyborgPartRenderer renderer : PARTS) {
				if(renderer.name.equals(partItem.getPartName(partType))) return renderer;
			}
		}

		return null;
	}
}
