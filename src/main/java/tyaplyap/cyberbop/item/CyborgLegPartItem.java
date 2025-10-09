package tyaplyap.cyberbop.item;

import tyaplyap.cyberbop.util.CyborgPartType;

public class CyborgLegPartItem extends CyborgPartItem {

	String right;
	String left;

	public CyborgLegPartItem(String right, String left, int energyCapacity, Settings settings) {
		super("", energyCapacity, settings);
		this.right = right;
		this.left = left;
	}

	@Override
	public String getPartName(CyborgPartType partType) {
		if(partType.equals(CyborgPartType.LEFT_LEG)) return left;
		if(partType.equals(CyborgPartType.RIGHT_LEG)) return right;
		return null;
	}
}
