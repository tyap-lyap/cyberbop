package tyaplyap.cyberbop.mixin;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.item.LongArmModule;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
	@Shadow
	@Final
	protected ServerPlayerEntity player;

//	@Inject(
//		method = "finishMining",
//		at = @At(
//			value = "INVOKE",
//			target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;tryBreakBlock(Lnet/minecraft/util/math/BlockPos;)Z",
//			shift = At.Shift.AFTER
//		),
//		cancellable = true
//	)
//	private void takeEnergyBreakBlock(BlockPos pos, int sequence, String reason, CallbackInfo ci) {
//		if (player instanceof ServerPlayerEntity && player instanceof PlayerExtension ex && ex.isCyborg() && ex.containsModule(CyberbopItems.LONG_ARM_MODULE) && LongArmModule.ENERGY_CONSUME <= ex.getEnergyStored()) {
//			ex.setEnergyStored(ex.getEnergyStored() - LongArmModule.ENERGY_CONSUME);
//		}
//	}

}
