package tyaplyap.cyberbop.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.item.LongArmModule;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

	@Shadow
	public ServerPlayerEntity player;

	@Inject(method = "getMaxAllowedFloatingTicks", at = @At("HEAD"), cancellable = true)
	void getMaxAllowedFloatingTicks(Entity entity, CallbackInfoReturnable<Integer> cir) {
		if(entity instanceof ServerPlayerEntity player && player instanceof PlayerExtension ex && ex.isCyborg() && ex.containsModule(CyberbopItems.JETPACK_MODULE)) {
			cir.setReturnValue(Integer.MAX_VALUE);
		}
	}

	@Inject(
		method = "onPlayerInteractEntity",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/network/ServerPlayerEntity;canInteractWithEntityIn(Lnet/minecraft/util/math/Box;D)Z",
			shift = At.Shift.AFTER
		),
		cancellable = true
	)
	private void takeEnergyInteractEntity(PlayerInteractEntityC2SPacket packet, CallbackInfo ci) {
		if (player instanceof ServerPlayerEntity && player instanceof PlayerExtension ex && ex.isCyborg() && ex.containsModule(CyberbopItems.LONG_ARM_MODULE) && LongArmModule.ENERGY_CONSUME <= ex.getEnergyStored()) {
			ex.setEnergyStored(ex.getEnergyStored() - LongArmModule.ENERGY_CONSUME);
		}
	}

	@Inject(
		method = "onPlayerInteractBlock",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/ActionResult;isAccepted()Z",
			ordinal = 0,
			shift = At.Shift.AFTER
		),
		cancellable = true
	)
	private void takeEnergyInteractBlock(PlayerInteractBlockC2SPacket packet, CallbackInfo ci) {
		if (player instanceof ServerPlayerEntity && player instanceof PlayerExtension ex && ex.isCyborg() && ex.containsModule(CyberbopItems.LONG_ARM_MODULE) && LongArmModule.ENERGY_CONSUME <= ex.getEnergyStored()) {
			ex.setEnergyStored(ex.getEnergyStored() - LongArmModule.ENERGY_CONSUME);
		}
	}
}
