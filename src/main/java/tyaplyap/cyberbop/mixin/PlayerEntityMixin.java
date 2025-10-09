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
import tyaplyap.cyberbop.item.CyberbopItems;

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

	private static final TrackedData<String> MODULE_1 = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<String> MODULE_2 = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<String> MODULE_3 = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);

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

		builder.add(MODULE_1, "");
		builder.add(MODULE_2, "");
		builder.add(MODULE_3, "");
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putBoolean("isCyborg", isCyborg());

		nbt.putString("cyborgHead", getCyborgHead());
		nbt.putString("cyborgBody", getCyborgBody());
		nbt.putString("cyborgRightArm", getCyborgRightArm());
		nbt.putString("cyborgLeftArm", getCyborgLeftArm());
		nbt.putString("cyborgRightLeg", getCyborgRightLeg());
		nbt.putString("cyborgLeftLeg", getCyborgLeftLeg());

		nbt.putInt("cyborgEnergy", getEnergyStored());

		nbt.putString("module1", getModule1());
		nbt.putString("module2", getModule2());
		nbt.putString("module3", getModule3());
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

		if(nbt.contains("cyborgEnergy")) setEnergyStored(nbt.getInt("cyborgEnergy"));

		if(nbt.contains("module1")) setModule1(nbt.getString("module1"));
		if(nbt.contains("module2")) setModule2(nbt.getString("module2"));
		if(nbt.contains("module3")) setModule3(nbt.getString("module3"));
	}

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		if((Object)this instanceof ServerPlayerEntity player) {
			if(isCyborg()) {
				if(player.interactionManager.getGameMode().equals(GameMode.SURVIVAL)) {
					if(getEnergyStored() != 0) {
						setEnergyStored(getEnergyStored() - 1);
					}
					else {
						if (player.getWorld().getTime() % 20L == 0L) {
							player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 40, 0, false, false));
							player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 40, 0, false, false));
						}
					}
				}

				var moduleItem1 = CyberbopItems.getModule(getModule1());
				if(moduleItem1 != null) moduleItem1.tick(player.getServerWorld(), player);

				var moduleItem2 = CyberbopItems.getModule(getModule2());
				if(moduleItem2 != null) moduleItem2.tick(player.getServerWorld(), player);

				var moduleItem3 = CyberbopItems.getModule(getModule3());
				if(moduleItem3 != null) moduleItem3.tick(player.getServerWorld(), player);
			}
		}
	}

	@Override
	public int capacity() {
		if(containsModule("extra_battery")) return 15000;
		return 10000;
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
	public String getCyborgHead() {
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

	@Override
	public boolean containsModule(String module) {
		return (getModule1().equals(module) || getModule2().equals(module) || getModule3().equals(module));
	}

	@Override
	public String getModule1() {
		return PlayerEntity.class.cast(this).getDataTracker().get(MODULE_1);
	}

	@Override
	public String getModule2() {
		return PlayerEntity.class.cast(this).getDataTracker().get(MODULE_2);
	}

	@Override
	public String getModule3() {
		return PlayerEntity.class.cast(this).getDataTracker().get(MODULE_3);
	}

	@Override
	public void setModule1(String module) {
		PlayerEntity.class.cast(this).getDataTracker().set(MODULE_1, module);
	}

	@Override
	public void setModule2(String module) {
		PlayerEntity.class.cast(this).getDataTracker().set(MODULE_2, module);
	}

	@Override
	public void setModule3(String module) {
		PlayerEntity.class.cast(this).getDataTracker().set(MODULE_3, module);
	}

	@Override
	public void setEnergyStored(int cyborgEnergy) {
		PlayerEntity.class.cast(this).getDataTracker().set(CYBORG_ENERGY, cyborgEnergy);
	}

	@Override
	public Type type() {
		return Type.BATTERY;
	}

	@Override
	public int getEnergyStored() {
		return PlayerEntity.class.cast(this).getDataTracker().get(CYBORG_ENERGY);
	}
}
