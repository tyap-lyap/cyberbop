package tyaplyap.cyberbop.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.util.CyborgPartType;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends PlayerEntity implements PlayerExtension {


	public ServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "onDeath", at = @At("HEAD"))
	void onDeath(DamageSource damageSource, CallbackInfo ci) {
		if (!this.isSpectator() && this.getWorld() instanceof ServerWorld serverWorld) {
			CyborgPartType.forEach(partType -> {
				this.dropStack(getCyborgPart(partType));
			});

			getModules().forEach(this::dropStack);
		}
	}
}
