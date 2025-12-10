package tyaplyap.cyberbop.client.model;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class DefaultedModuleGeoModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {

	public DefaultedModuleGeoModel(Identifier assetSubpath) {
		super(assetSubpath);
	}

	@Override
	protected String subtype() {
		return "module";
	}
}
