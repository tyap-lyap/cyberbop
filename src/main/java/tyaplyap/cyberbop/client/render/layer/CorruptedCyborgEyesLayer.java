package tyaplyap.cyberbop.client.render.layer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.ClientUtil;
import tyaplyap.cyberbop.entity.CyborgAnimationTestEntity;

import java.awt.*;

public class CorruptedCyborgEyesLayer <T extends CyborgAnimationTestEntity> extends GeoRenderLayer<T> {
	public CorruptedCyborgEyesLayer(GeoRenderer<T> renderer) {
		super(renderer);
	}

	/** @deprecated */
	@Deprecated(
		forRemoval = true
	)
	protected RenderLayer getRenderType(T animatable) {
		return this.getRenderType(animatable, (VertexConsumerProvider)null);
	}

	protected @Nullable RenderLayer getRenderType(T animatable, @Nullable VertexConsumerProvider bufferSource) {
		if (animatable instanceof Entity entity) {
			boolean invisible = entity.isInvisible();
			Identifier texture = AutoGlowingTexture.getEmissiveResource(this.getTextureResource(animatable));
			if (invisible && !entity.isInvisibleTo(ClientUtil.getClientPlayer())) {
				return RenderLayer.getItemEntityTranslucentCull(texture);
			} else if (MinecraftClient.getInstance().hasOutline(entity)) {
				return invisible ? RenderLayer.getOutline(texture) : AutoGlowingTexture.getOutlineRenderType(this.getTextureResource(animatable));
			} else {
				return invisible ? null : AutoGlowingTexture.getRenderType(this.getTextureResource(animatable));
			}
		} else {
			return AutoGlowingTexture.getRenderType(this.getTextureResource(animatable));
		}
	}

	public void render(MatrixStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		renderType = this.getRenderType(animatable);
		if (renderType != null) {

			this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType, bufferSource.getBuffer(renderType), partialTick, 15728640, packedOverlay, animatable.isOff() ? new Color(MathHelper.clamp(animatable.tickEye, 200, 255), MathHelper.clamp(animatable.tickEye, 0, 255),MathHelper.clamp(animatable.tickEye, 0, 255),255).getRGB() : this.getRenderer().getRenderColor(animatable, partialTick, packedLight).argbInt());
		}

	}
	}
