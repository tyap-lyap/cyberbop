package tyaplyap.cyberbop.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.BatteryModule;
import tyaplyap.cyberbop.item.CyborgModuleItem;
import tyaplyap.cyberbop.item.CyborgPartItem;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.CyborgPartType;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerExtension {

	@Shadow
	public abstract void playSound(SoundEvent sound, float volume, float pitch);

	public final EnergyStorage energyStorage = new EnergyStorage() {
		@Override
		public int getEnergy() {
			return storedEnergy = getEnergyStored();
		}

		@Override
		public boolean canInsert(EnergyStorage target, Type sourceType) {
			return true;
		}

		@Override
		public boolean canExtract(EnergyStorage source, Type sourceType) {
			return true;
		}

		@Override
		public void setEnergy(int energy) {
			setEnergyStored(energy);

		}

		@Override
		public Type type() {
			return Type.CYBORG;
		}

		@Override
		public int capacity() {
			return getCapacity();
		}
	};

	private static final TrackedData<Boolean> IS_CYBORG = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private static final TrackedData<ItemStack> HEAD = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private static final TrackedData<ItemStack> BODY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private static final TrackedData<ItemStack> RIGHT_ARM = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private static final TrackedData<ItemStack> LEFT_ARM = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private static final TrackedData<ItemStack> RIGHT_LEG = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private static final TrackedData<ItemStack> LEFT_LEG = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

	private static final TrackedData<Integer> CYBORG_ENERGY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

	private static final TrackedData<ItemStack> MODULE_1 = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private static final TrackedData<ItemStack> MODULE_2 = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private static final TrackedData<ItemStack> MODULE_3 = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private static final TrackedData<ItemStack> MODULE_4 = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

//	private Vec3d assemblePos = null;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initDataTracker", at = @At("HEAD"))
	void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(IS_CYBORG, false);
		builder.add(HEAD, ItemStack.EMPTY);
		builder.add(BODY, ItemStack.EMPTY);
		builder.add(RIGHT_ARM, ItemStack.EMPTY);
		builder.add(LEFT_ARM, ItemStack.EMPTY);
		builder.add(RIGHT_LEG, ItemStack.EMPTY);
		builder.add(LEFT_LEG, ItemStack.EMPTY);
		builder.add(CYBORG_ENERGY, 0);

		builder.add(MODULE_1, ItemStack.EMPTY);
		builder.add(MODULE_2, ItemStack.EMPTY);
		builder.add(MODULE_3, ItemStack.EMPTY);
		builder.add(MODULE_4, ItemStack.EMPTY);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putBoolean("isCyborg", isCyborg());

		var registry = getRegistryManager();
		if (!getCyborgHead().isEmpty()) nbt.put("cyborgHead", getCyborgHead().encode(registry));
		if (!getCyborgBody().isEmpty()) nbt.put("cyborgBody", getCyborgBody().encode(registry));
		if (!getCyborgRightArm().isEmpty()) nbt.put("cyborgRightArm", getCyborgRightArm().encode(registry));
		if (!getCyborgLeftArm().isEmpty()) nbt.put("cyborgLeftArm", getCyborgLeftArm().encode(registry));
		if (!getCyborgRightLeg().isEmpty()) nbt.put("cyborgRightLeg", getCyborgRightLeg().encode(registry));
		if (!getCyborgLeftLeg().isEmpty()) nbt.put("cyborgLeftLeg", getCyborgLeftLeg().encode(registry));

		nbt.putInt("cyborgEnergy", getEnergyStored());

		if (!getModule1().isEmpty()) nbt.put("module1", getModule1().encode(registry));
		if (!getModule2().isEmpty()) nbt.put("module2", getModule2().encode(registry));
		if (!getModule3().isEmpty()) nbt.put("module3", getModule3().encode(registry));
		if (!getModule3().isEmpty()) nbt.put("module4", getModule4().encode(registry));

//		if (getAssemblePos() != null) {
//			nbt.putDouble("assemblePosX", getAssemblePos().x);
//			nbt.putDouble("assemblePosY", getAssemblePos().y);
//			nbt.putDouble("assemblePosZ", getAssemblePos().z);
//		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		if(nbt.contains("isCyborg")) setCyborg(nbt.getBoolean("isCyborg"));

		var registry = getRegistryManager();

		if (nbt.contains("cyborgHead", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("cyborgHead");
			setCyborgHead(ItemStack.fromNbt(registry, nbtCompound).orElse(ItemStack.EMPTY));
		}
		else setCyborgHead(ItemStack.EMPTY);

		if (nbt.contains("cyborgBody", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("cyborgBody");
			setCyborgBody(ItemStack.fromNbt(registry, nbtCompound).orElse(ItemStack.EMPTY));
		}
		else setCyborgBody(ItemStack.EMPTY);

		if (nbt.contains("cyborgRightArm", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("cyborgRightArm");
			setCyborgRightArm(ItemStack.fromNbt(registry, nbtCompound).orElse(ItemStack.EMPTY));
		}
		else setCyborgRightArm(ItemStack.EMPTY);

		if (nbt.contains("cyborgLeftArm", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("cyborgLeftArm");
			setCyborgLeftArm(ItemStack.fromNbt(registry, nbtCompound).orElse(ItemStack.EMPTY));
		}
		else setCyborgLeftArm(ItemStack.EMPTY);

		if (nbt.contains("cyborgRightLeg", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("cyborgRightLeg");
			setCyborgRightLeg(ItemStack.fromNbt(registry, nbtCompound).orElse(ItemStack.EMPTY));
		}
		else setCyborgRightLeg(ItemStack.EMPTY);

		if (nbt.contains("cyborgLeftLeg", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("cyborgLeftLeg");
			setCyborgLeftLeg(ItemStack.fromNbt(registry, nbtCompound).orElse(ItemStack.EMPTY));
		}
		else setCyborgLeftLeg(ItemStack.EMPTY);

		if(nbt.contains("cyborgEnergy")) setEnergyStored(nbt.getInt("cyborgEnergy"));


		if (nbt.contains("module1", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("module1");
			setModule1(ItemStack.fromNbt(registry, nbtCompound).orElse(ItemStack.EMPTY));
		}
		else setModule1(ItemStack.EMPTY);

		if (nbt.contains("module2", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("module2");
			setModule2(ItemStack.fromNbt(registry, nbtCompound).orElse(ItemStack.EMPTY));
		}
		else setModule2(ItemStack.EMPTY);

		if (nbt.contains("module3", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("module3");
			setModule3(ItemStack.fromNbt(registry, nbtCompound).orElse(ItemStack.EMPTY));
		}
		else setModule3(ItemStack.EMPTY);

		if (nbt.contains("module4", NbtElement.COMPOUND_TYPE)) {
			NbtCompound nbtCompound = nbt.getCompound("module4");
			setModule4(ItemStack.fromNbt(registry, nbtCompound).orElse(ItemStack.EMPTY));
		}
		else setModule4(ItemStack.EMPTY);
//		if(nbt.contains("assemblePosX") && nbt.contains("assemblePosY") && nbt.contains("assemblePosZ")) {
//			setAssemblePos(new Vec3d(nbt.getDouble("assemblePosX"), nbt.getDouble("assemblePosY"), nbt.getDouble("assemblePosZ")));
//		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		if((Object)this instanceof ServerPlayerEntity player) {
			if(isCyborg()) {
				if(!player.isCreative() && !player.isSpectator()) {
					if (getEnergyStored() > 0) {
						setEnergyStored(Math.max(getEnergyStored() - 1, 0));
					}
					else {
						if (player.getWorld().getTime() % 20L == 0L) {
							player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 40, 0, false, false));
							player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 40, 0, false, false));
						}
					}
				}

				getModules().forEach(stack -> {
					if(stack.getItem() instanceof CyborgModuleItem moduleItem) moduleItem.tick(player.getServerWorld(), player, this, stack);
				});
			}
		}
	}

	public int getCapacity() {
		int capacity = 0;

		for(ItemStack stack : getModules()) {
			if(stack.getItem() instanceof BatteryModule batteryModule) {
				capacity = capacity + batteryModule.getEnergyCapacity();
			}
		}

		for(CyborgPartType partType : CyborgPartType.values()) {
			if(getCyborgPart(partType).getItem() instanceof CyborgPartItem partItem) {
				capacity = capacity + partItem.getCapacity();
			}
		}

		return capacity;
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
	public void setCyborgHead(ItemStack head) {
		PlayerEntity.class.cast(this).getDataTracker().set(HEAD, head);
	}

	@Override
	public void setCyborgBody(ItemStack body) {
		PlayerEntity.class.cast(this).getDataTracker().set(BODY, body);
	}

	@Override
	public void setCyborgRightArm(ItemStack rightArm) {
		PlayerEntity.class.cast(this).getDataTracker().set(RIGHT_ARM, rightArm);
	}

	@Override
	public void setCyborgLeftArm(ItemStack leftArm) {
		PlayerEntity.class.cast(this).getDataTracker().set(LEFT_ARM, leftArm);
	}

	@Override
	public void setCyborgRightLeg(ItemStack rightLeg) {
		PlayerEntity.class.cast(this).getDataTracker().set(RIGHT_LEG, rightLeg);
	}

	@Override
	public void setCyborgLeftLeg(ItemStack leftLeg) {
		PlayerEntity.class.cast(this).getDataTracker().set(LEFT_LEG, leftLeg);
	}

	@Override
	public ItemStack getCyborgHead() {
		return PlayerEntity.class.cast(this).getDataTracker().get(HEAD);
	}

	@Override
	public ItemStack getCyborgBody() {
		return PlayerEntity.class.cast(this).getDataTracker().get(BODY);
	}

	@Override
	public ItemStack getCyborgRightArm() {
		return PlayerEntity.class.cast(this).getDataTracker().get(RIGHT_ARM);
	}

	@Override
	public ItemStack getCyborgLeftArm() {
		return PlayerEntity.class.cast(this).getDataTracker().get(LEFT_ARM);
	}

	@Override
	public ItemStack getCyborgRightLeg() {
		return PlayerEntity.class.cast(this).getDataTracker().get(RIGHT_LEG);
	}

	@Override
	public ItemStack getCyborgLeftLeg() {
		return PlayerEntity.class.cast(this).getDataTracker().get(LEFT_LEG);
	}

	@Override
	public boolean containsModule(Item module) {
		return (getModule1().getItem().equals(module) || getModule2().getItem().equals(module) || getModule3().getItem().equals(module) || getModule4().getItem().equals(module));
	}

	@Override
	public ItemStack getModule1() {
		return PlayerEntity.class.cast(this).getDataTracker().get(MODULE_1);
	}

	@Override
	public ItemStack getModule2() {
		return PlayerEntity.class.cast(this).getDataTracker().get(MODULE_2);
	}

	@Override
	public ItemStack getModule3() {
		return PlayerEntity.class.cast(this).getDataTracker().get(MODULE_3);
	}

	@Override
	public ItemStack getModule4() {
		return PlayerEntity.class.cast(this).getDataTracker().get(MODULE_4);
	}

	@Override
	public void setModule1(ItemStack module) {
		PlayerEntity.class.cast(this).getDataTracker().set(MODULE_1, module);
	}

	@Override
	public void setModule2(ItemStack module) {
		PlayerEntity.class.cast(this).getDataTracker().set(MODULE_2, module);
	}

	@Override
	public void setModule3(ItemStack module) {
		PlayerEntity.class.cast(this).getDataTracker().set(MODULE_3, module);
	}

	@Override
	public void setModule4(ItemStack module) {
		PlayerEntity.class.cast(this).getDataTracker().set(MODULE_4, module);
	}

	@Override
	public List<ItemStack> getModules() {
		return List.of(getModule1(), getModule2(), getModule3(), getModule4());
	}

	@Override
	public int getEnergyStored() {
		return PlayerEntity.class.cast(this).getDataTracker().get(CYBORG_ENERGY);
	}

	@Override
	public void setEnergyStored(int cyborgEnergy) {
		energyStorage.storedEnergy = cyborgEnergy;
		PlayerEntity.class.cast(this).getDataTracker().set(CYBORG_ENERGY, cyborgEnergy);
	}

	@Override
	public EnergyStorage getEnergyStorage() {
		return energyStorage;
	}

//	@Override
//	public Vec3d getAssemblePos() {
//		return assemblePos;
//	}
//
//	@Override
//	public void setAssemblePos(Vec3d assemblePos) {
//		this.assemblePos = assemblePos;
//	}
}
