package tyaplyap.cyberbop.item.parts;

import java.util.ArrayList;

public class CyborgParts {
	public static final ArrayList<CyborgPart> PARTS = new ArrayList<>();

	public static final CyborgPart BASIC_HEAD = register(new HeadCyborgPart("basic_cyborg_head", "textures/entity/robot.png"));
	public static final CyborgPart ADVANCED_HEAD = register(new HeadCyborgPart("advanced_cyborg_head", "textures/entity/cyborg.png"));
	public static final CyborgPart ADVANCED_ARM = register(new RightArmCyborgPart("advanced_cyborg_arm", "textures/entity/advanced.png"));

	public static CyborgPart register(CyborgPart part) {
		PARTS.add(part);
		return part;
	}

	public static void init() {
		PARTS.forEach(part -> part.register());
	}

	public static CyborgPart getPart(String name) {
		for(CyborgPart part : PARTS) {
			if(part.name.equals(name)) return part;
		}
		return null;
	}
}
