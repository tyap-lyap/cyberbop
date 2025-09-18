package tyaplyap.cyberbop.mixin;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tyaplyap.cyberbop.extension.PlayerExtension;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerExtension {

	private static final TrackedData<Boolean> IS_CYBORG = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	@Inject(method = "initDataTracker", at = @At("HEAD"))
	void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(IS_CYBORG, false);
	}

	@Override
	public boolean isCyborg() {
		return PlayerEntity.class.cast(this).getDataTracker().get(IS_CYBORG);
	}

	@Override
	public void setCyborg(boolean isCyborg) {
		PlayerEntity.class.cast(this).getDataTracker().set(IS_CYBORG, isCyborg);
	}
}
