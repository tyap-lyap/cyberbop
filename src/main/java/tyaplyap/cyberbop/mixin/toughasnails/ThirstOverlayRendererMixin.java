package tyaplyap.cyberbop.mixin.toughasnails;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.thirst.ThirstOverlayRenderer;
import tyaplyap.cyberbop.extension.PlayerExtension;

@Mixin(ThirstOverlayRenderer.class)
public class ThirstOverlayRendererMixin {

	@Inject(method = "drawThirst", at = @At("HEAD"), cancellable = true)
	private static void drawThirst(DrawContext guiGraphics, int screenWidth, int rowTop, int thirstLevel, float thirstHydrationLevel, CallbackInfo ci) {
		if(MinecraftClient.getInstance().player instanceof PlayerExtension ex && ex.isCyborg()) ci.cancel();
	}
}
