package tyaplyap.cyberbop.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.extension.PlayerExtension;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	private static final Identifier HEART_FULL = CyberbopMod.id("hud/heart/cyborg_full");
	private static final Identifier HEART_FULL_BLINKING = CyberbopMod.id("hud/heart/cyborg_full_blinking");
	private static final Identifier HEART_HALF = CyberbopMod.id("hud/heart/cyborg_half");
	private static final Identifier HEART_HALF_BLINKING = CyberbopMod.id("hud/heart/cyborg_half_blinking");

	@Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
	void drawHeart(DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, CallbackInfo ci) {
		if(type == InGameHud.HeartType.NORMAL && MinecraftClient.getInstance().player instanceof PlayerExtension ex && ex.isCyborg()) {
			RenderSystem.enableBlend();
			context.drawGuiTexture(getHeartTexture(hardcore, half, blinking), x, y, 9, 9);
			RenderSystem.disableBlend();
			ci.cancel();
		}
	}

	private static Identifier getHeartTexture(boolean hardcore, boolean half, boolean blinking) {
		if (half) {
			return blinking ? HEART_HALF_BLINKING : HEART_HALF;
		}
		else {
			return blinking ? HEART_FULL_BLINKING : HEART_FULL;
		}
	}

	@Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
	void renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
		if(MinecraftClient.getInstance().player instanceof PlayerExtension ex && ex.isCyborg()) {
			ci.cancel();
		}
	}
}
