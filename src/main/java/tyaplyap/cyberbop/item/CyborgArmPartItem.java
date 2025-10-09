package tyaplyap.cyberbop.item;

import tyaplyap.cyberbop.util.CyborgPartType;

public class CyborgArmPartItem extends CyborgPartItem {

	String right;
	String left;

	public CyborgArmPartItem(String right, String left, int energyCapacity, Settings settings) {
		super("", energyCapacity, settings);
		this.right = right;
		this.left = left;
	}

	@Override
	public String getPartName(CyborgPartType partType) {
		if(partType.equals(CyborgPartType.LEFT_ARM)) return left;
		if(partType.equals(CyborgPartType.RIGHT_ARM)) return right;
		return null;
	}
}
