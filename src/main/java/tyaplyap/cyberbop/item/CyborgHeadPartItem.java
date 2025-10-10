package tyaplyap.cyberbop.item;

import tyaplyap.cyberbop.util.CyborgPartType;

public class CyborgHeadPartItem extends CyborgPartItem {

	public CyborgHeadPartItem(String partName, int energyCapacity, double health, Settings settings) {
		super(partName, energyCapacity, health, settings);
	}

	@Override
	public String getPartName(CyborgPartType partType) {
		if(partType.equals(CyborgPartType.HEAD)) return partName;
		return null;
	}
}
