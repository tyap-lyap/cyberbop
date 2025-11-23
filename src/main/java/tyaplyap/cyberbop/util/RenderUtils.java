package tyaplyap.cyberbop.util;

import net.minecraft.client.model.ModelPart;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.Optional;

public class RenderUtils {
	public static void setPositionGeoBone(Optional<GeoBone> bone, float x, float y, float z, float originalY, float sneakingOffset, ModelPart referenceModel, Optional<GeoBone> bone_local) {
		setPositionGeoBone(bone, x, y, z, originalY, sneakingOffset, referenceModel, 0, 0, 0, bone_local);
	}
	public static void setPositionGeoBone(Optional<GeoBone> bone, float x, float y, float z, float originalY, float sneakingOffset, ModelPart referenceModel, float xr, float yr, float zr, Optional<GeoBone> bone_local) {
		if (bone.isPresent()) {
			bone.get().updatePosition(8f - (referenceModel.pivotX), 16f - (originalY - sneakingOffset), -8 - (-referenceModel.pivotZ));
			bone.get().updateRotation(referenceModel.pitch, referenceModel.yaw, referenceModel.roll);
		}
		if (bone_local.isPresent()) {
			bone_local.get().updatePosition((-x),y,z);
			bone_local.get().updateRotation((float)Math.toRadians(xr), (float)Math.toRadians(yr), (float)Math.toRadians(zr));
		}
	}
}
