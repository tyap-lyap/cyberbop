package tyaplyap.cyberbop.item;

import tyaplyap.cyberbop.util.CyborgPartType;

public class CyborgHeadPartItem extends CyborgPartItem {

	public CyborgHeadPartItem(String partName, int energyCapacity, Settings settings) {
		super(partName, energyCapacity, settings);
	}

	@Override
	public String getPartName(CyborgPartType partType) {
		if(partType.equals(CyborgPartType.HEAD)) return partName;
		return null;
	}
}
