package tyaplyap.cyberbop.client.render.parts;

import tyaplyap.cyberbop.client.render.parts.advanced.*;
import tyaplyap.cyberbop.client.render.parts.basic.*;

import java.util.ArrayList;

public class CyborgPartRenderers {
	public static final ArrayList<CyborgPartRenderer> PARTS = new ArrayList<>();

	public static final CyborgPartRenderer BASIC_HEAD = register(new BasicHeadCyborgPart("basic_head", "textures/entity/basic_cyborg.png"));
	public static final CyborgPartRenderer BASIC_RIGHT_ARM = register(new BasicRightArmCyborgPart("basic_right_arm", "textures/entity/basic_cyborg.png"));
	public static final CyborgPartRenderer BASIC_LEFT_ARM = register(new BasicLeftArmCyborgPart("basic_left_arm", "textures/entity/basic_cyborg.png"));
	public static final CyborgPartRenderer BASIC_BODY = register(new BasicBodyCyborgPart("basic_body", "textures/entity/basic_cyborg.png"));
	public static final CyborgPartRenderer BASIC_RIGHT_LEG = register(new BasicRightLegCyborgPart("basic_right_leg", "textures/entity/basic_cyborg.png"));
	public static final CyborgPartRenderer BASIC_LEFT_LEG = register(new BasicLeftLegCyborgPart("basic_left_leg", "textures/entity/basic_cyborg.png"));

	public static final CyborgPartRenderer GOLDEN_HEAD = register(new BasicHeadCyborgPart("golden_head", "textures/entity/golden_cyborg.png"));
	public static final CyborgPartRenderer GOLDEN_RIGHT_ARM = register(new BasicRightArmCyborgPart("golden_right_arm", "textures/entity/golden_cyborg.png"));
	public static final CyborgPartRenderer GOLDEN_LEFT_ARM = register(new BasicLeftArmCyborgPart("golden_left_arm", "textures/entity/golden_cyborg.png"));
	public static final CyborgPartRenderer GOLDEN_BODY = register(new BasicBodyCyborgPart("golden_body", "textures/entity/golden_cyborg.png"));
	public static final CyborgPartRenderer GOLDEN_RIGHT_LEG = register(new BasicRightLegCyborgPart("golden_right_leg", "textures/entity/golden_cyborg.png"));
	public static final CyborgPartRenderer GOLDEN_LEFT_LEG = register(new BasicLeftLegCyborgPart("golden_left_leg", "textures/entity/golden_cyborg.png"));

	public static final CyborgPartRenderer ADVANCED_HEAD = register(new HeadCyborgPart("advanced_head", "textures/entity/robot.png"));
	public static final CyborgPartRenderer ADVANCED_RIGHT_ARM = register(new RightArmCyborgPart("advanced_right_arm", "textures/entity/advanced.png"));
	public static final CyborgPartRenderer ADVANCED_LEFT_ARM = register(new LeftArmCyborgPart("advanced_left_arm", "textures/entity/advanced.png"));
	public static final CyborgPartRenderer ADVANCED_BODY = register(new BodyCyborgPart("advanced_body", "textures/entity/robot.png"));
	public static final CyborgPartRenderer ADVANCED_RIGHT_LEG = register(new RightLegCyborgPart("advanced_right_leg", "textures/entity/robot.png"));
	public static final CyborgPartRenderer ADVANCED_LEFT_LEG = register(new LeftLegCyborgPart("advanced_left_leg", "textures/entity/robot.png"));

	public static CyborgPartRenderer register(CyborgPartRenderer part) {
		PARTS.add(part);
		return part;
	}

	public static void init() {
		PARTS.forEach(part -> part.register());
	}

	public static CyborgPartRenderer getPart(String name) {
		for(CyborgPartRenderer part : PARTS) {
			if(part.name.equals(name)) return part;
		}
		return null;
	}
}
