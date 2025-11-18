package tyaplyap.cyberbop.util;

import net.minecraft.client.model.ModelPart;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.Optional;

public class RenderUtils {
	public static void setPositionGeoBone(Optional<GeoBone> bone, float x, float y, float z, float originalY, float sneakingOffset, ModelPart referenceModel, boolean mirror) {
		setPositionGeoBone(bone, x, y, z, originalY, sneakingOffset, referenceModel, mirror, 0, 0, 0, Optional.empty());
	}
	public static void setPositionGeoBone(Optional<GeoBone> bone, float x, float y, float z, float originalY, float sneakingOffset, ModelPart referenceModel, boolean mirror, float xr, float yr, float zr, Optional<GeoBone> bone_local) {
		if (bone.isPresent()) {
			bone.get().updatePosition(8f - (referenceModel.pivotX +  (mirror ? 0.5f : -0.5f)  - x), 16f - (originalY - 0.5f - sneakingOffset - y), -8 - (-referenceModel.pivotZ + (mirror ? -0.5f : 0.5f) - z));
			bone.get().updatePivot(x,y,z);
			bone.get().updateRotation(referenceModel.pitch, referenceModel.yaw, referenceModel.roll);
		}
		if (bone_local.isPresent()) {
			bone_local.get().updateRotation(xr, yr, zr);
		}
	}
}
