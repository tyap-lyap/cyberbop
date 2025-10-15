package tyaplyap.cyberbop.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyborgModuleItem;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerMixin implements PlayerExtension {

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		if((Object)this instanceof ClientPlayerEntity player) {
			if(isCyborg()) {

				getModules().forEach(stack -> {
					if(stack.getItem() instanceof CyborgModuleItem moduleItem) moduleItem.clientTick(player.clientWorld, player, this);
				});
			}
		}
	}
}
