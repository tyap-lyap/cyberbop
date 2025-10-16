package tyaplyap.cyberbop.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.item.NightVisionModule;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@ModifyExpressionValue(
		method = "update",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z")
	)
	private boolean hasNightVisionModule(boolean original) {
		if(client.player instanceof PlayerExtension ex && ex.isCyborg() && ex.containsModule(CyberbopItems.NIGHT_VISION_MODULE) && ex.getEnergyStored() > 0) return true;
		return original;
	}

	@WrapOperation(
		method = "update",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getNightVisionStrength(Lnet/minecraft/entity/LivingEntity;F)F")
	)
	private float maxStrengthWithNightVisionModule(LivingEntity entity, float tickDelta, Operation<Float> original) {
		if(client.player instanceof PlayerExtension ex && ex.isCyborg() && ex.containsModule(CyberbopItems.NIGHT_VISION_MODULE) && ex.getEnergyStored() > 0) return NightVisionModule.getNightVisionStrength();
		return original.call(entity, tickDelta);
	}
}
