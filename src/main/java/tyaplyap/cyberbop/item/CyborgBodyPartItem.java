package tyaplyap.cyberbop.item;

import tyaplyap.cyberbop.util.CyborgPartType;

public class CyborgBodyPartItem extends CyborgPartItem {

	public CyborgBodyPartItem(String partName, int energyCapacity, double health, Settings settings) {
		super(partName, energyCapacity, health, settings);
	}

	@Override
	public String getPartName(CyborgPartType partType) {
		if(partType.equals(CyborgPartType.BODY)) return partName;
		return null;
	}
}
