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
import tyaplyap.cyberbop.item.CyberbopItems;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends PlayerEntity implements PlayerExtension {


	public ServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "onDeath", at = @At("HEAD"))
	void onDeath(DamageSource damageSource, CallbackInfo ci) {
		if (!this.isSpectator() && this.getWorld() instanceof ServerWorld serverWorld) {
			this.dropItem(CyberbopItems.partToItem(getCyborgHead()), 0);
			this.dropItem(CyberbopItems.partToItem(getCyborgBody()), 0);
			this.dropItem(CyberbopItems.partToItem(getCyborgRightArm()), 0);
			this.dropItem(CyberbopItems.partToItem(getCyborgLeftArm()), 0);
			this.dropItem(CyberbopItems.partToItem(getCyborgRightLeg()), 0);
			this.dropItem(CyberbopItems.partToItem(getCyborgLeftLeg()), 0);
		}
	}
}
