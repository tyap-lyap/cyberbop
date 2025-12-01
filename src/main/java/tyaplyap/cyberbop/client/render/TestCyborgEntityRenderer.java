package tyaplyap.cyberbop.client.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.client.render.layer.CorruptedCyborgEyesLayer;
import tyaplyap.cyberbop.entity.CyborgAnimationTestEntity;

public class TestCyborgEntityRenderer extends GeoEntityRenderer<CyborgAnimationTestEntity> {

	public TestCyborgEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new DefaultedEntityGeoModel<>(Identifier.of(CyberbopMod.MOD_ID, "cyborg"), true));
		this.addRenderLayer(new CorruptedCyborgEyesLayer<>(this));
	}
}

