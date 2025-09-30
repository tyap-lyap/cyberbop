package tyaplyap.cyberbop.mixin;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tyaplyap.cyberbop.extension.PlayerExtension;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerExtension {

	private static final TrackedData<Boolean> IS_CYBORG = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private static final TrackedData<String> HEAD = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<String> BODY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<String> RIGHT_ARM = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<String> LEFT_ARM = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<String> RIGHT_LEG = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<String> LEFT_LEG = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);

	private static final TrackedData<Integer> CYBORG_ENERGY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

	@Inject(method = "initDataTracker", at = @At("HEAD"))
	void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(IS_CYBORG, false);
		builder.add(HEAD, "");
		builder.add(BODY, "");
		builder.add(RIGHT_ARM, "");
		builder.add(LEFT_ARM, "");
		builder.add(RIGHT_LEG, "");
		builder.add(LEFT_LEG, "");
		builder.add(CYBORG_ENERGY, 0);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putBoolean("isCyborg", isCyborg());

		nbt.putString("cyborgHead", geCyborgHead());
		nbt.putString("cyborgBody", getCyborgBody());
		nbt.putString("cyborgRightArm", getCyborgRightArm());
		nbt.putString("cyborgLeftArm", getCyborgLeftArm());
		nbt.putString("cyborgRightLeg", getCyborgRightLeg());
		nbt.putString("cyborgLeftLeg", getCyborgLeftLeg());

		nbt.putInt("cyborgEnergy", getCyborgEnergy());
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		if(nbt.contains("isCyborg")) setCyborg(nbt.getBoolean("isCyborg"));

		if(nbt.contains("cyborgHead")) setCyborgHead(nbt.getString("cyborgHead"));
		if(nbt.contains("cyborgBody")) setCyborgBody(nbt.getString("cyborgBody"));
		if(nbt.contains("cyborgRightArm")) setCyborgRightArm(nbt.getString("cyborgRightArm"));
		if(nbt.contains("cyborgLeftArm")) setCyborgLeftArm(nbt.getString("cyborgLeftArm"));
		if(nbt.contains("cyborgRightLeg")) setCyborgRightLeg(nbt.getString("cyborgRightLeg"));
		if(nbt.contains("cyborgLeftLeg")) setCyborgLeftLeg(nbt.getString("cyborgLeftLeg"));

		if(nbt.contains("cyborgEnergy")) setCyborgEnergy(nbt.getInt("cyborgEnergy"));
	}

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		if((Object)this instanceof ServerPlayerEntity player) {
			if(player.interactionManager.getGameMode().equals(GameMode.SURVIVAL)) {
				if(isCyborg()) {
					if(getCyborgEnergy() != 0) {
						setCyborgEnergy(getCyborgEnergy() - 1);
					}
					else {
						if (player.getWorld().getTime() % 20L == 0L) {
							player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 40, 0, false, false));
							player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 40, 0, false, false));
						}
					}
				}
			}
		}
	}

	@Override
	public int getCyborgEnergy() {
		return PlayerEntity.class.cast(this).getDataTracker().get(CYBORG_ENERGY);
	}

	@Override
	public void setCyborgEnergy(int cyborgEnergy) {
		PlayerEntity.class.cast(this).getDataTracker().set(CYBORG_ENERGY, cyborgEnergy);
	}

	@Override
	public boolean isCyborg() {
		return PlayerEntity.class.cast(this).getDataTracker().get(IS_CYBORG);
	}

	@Override
	public void setCyborg(boolean isCyborg) {
		PlayerEntity.class.cast(this).getDataTracker().set(IS_CYBORG, isCyborg);
	}

	@Override
	public void setCyborgHead(String head) {
		PlayerEntity.class.cast(this).getDataTracker().set(HEAD, head);
	}

	@Override
	public void setCyborgBody(String body) {
		PlayerEntity.class.cast(this).getDataTracker().set(BODY, body);
	}

	@Override
	public void setCyborgRightArm(String rightArm) {
		PlayerEntity.class.cast(this).getDataTracker().set(RIGHT_ARM, rightArm);
	}

	@Override
	public void setCyborgLeftArm(String leftArm) {
		PlayerEntity.class.cast(this).getDataTracker().set(LEFT_ARM, leftArm);
	}

	@Override
	public void setCyborgRightLeg(String rightLeg) {
		PlayerEntity.class.cast(this).getDataTracker().set(RIGHT_LEG, rightLeg);
	}

	@Override
	public void setCyborgLeftLeg(String leftLeg) {
		PlayerEntity.class.cast(this).getDataTracker().set(LEFT_LEG, leftLeg);
	}

	@Override
	public String geCyborgHead() {
		return PlayerEntity.class.cast(this).getDataTracker().get(HEAD);
	}

	@Override
	public String getCyborgBody() {
		return PlayerEntity.class.cast(this).getDataTracker().get(BODY);
	}

	@Override
	public String getCyborgRightArm() {
		return PlayerEntity.class.cast(this).getDataTracker().get(RIGHT_ARM);
	}

	@Override
	public String getCyborgLeftArm() {
		return PlayerEntity.class.cast(this).getDataTracker().get(LEFT_ARM);
	}

	@Override
	public String getCyborgRightLeg() {
		return PlayerEntity.class.cast(this).getDataTracker().get(RIGHT_LEG);
	}

	@Override
	public String getCyborgLeftLeg() {
		return PlayerEntity.class.cast(this).getDataTracker().get(LEFT_LEG);
	}


}
