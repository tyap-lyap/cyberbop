package tyaplyap.cyberbop.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tyaplyap.cyberbop.extension.PlayerExtension;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
	void drawHeart(DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, CallbackInfo ci) {
		if(MinecraftClient.getInstance().player instanceof PlayerExtension ex && ex.isCyborg()) {
			RenderSystem.enableBlend();
			context.drawGuiTexture(type.getTexture(true, half, blinking), x, y, 9, 9);
			RenderSystem.disableBlend();
			ci.cancel();
		}
	}

	@Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
	void renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
		if(MinecraftClient.getInstance().player instanceof PlayerExtension ex && ex.isCyborg()) {
			ci.cancel();
		}
	}
}
