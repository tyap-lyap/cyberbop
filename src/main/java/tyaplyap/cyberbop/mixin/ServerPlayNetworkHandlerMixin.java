package tyaplyap.cyberbop.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyberbopItems;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

	@Inject(method = "getMaxAllowedFloatingTicks", at = @At("HEAD"), cancellable = true)
	void getMaxAllowedFloatingTicks(Entity entity, CallbackInfoReturnable<Integer> cir) {
		if(entity instanceof ServerPlayerEntity player && player instanceof PlayerExtension ex && ex.isCyborg() && ex.containsModule(CyberbopItems.JETPACK_MODULE)) {
			cir.setReturnValue(Integer.MAX_VALUE);
		}
	}
}
