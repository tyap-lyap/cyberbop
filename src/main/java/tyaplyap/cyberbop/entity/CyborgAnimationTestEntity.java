package tyaplyap.cyberbop.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CyborgAnimationTestEntity extends LivingEntity implements GeoEntity {

	//protected static final RawAnimation SLEEP_ANIM = RawAnimation.begin().thenLoop("sleep");

	//protected static final RawAnimation WAKE_ANIM = RawAnimation.begin().thenPlay("wake_default");
	//protected static final RawAnimation WAKE_ATTACK_ANIM = RawAnimation.begin().thenPlay("wake_attack");
	protected static final RawAnimation FAN = RawAnimation.begin().thenPlayAndHold("fan");
	//protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle_cd");

	private AnimationController test = new AnimationController<>(this, "wake", state -> PlayState.STOP)
		/*.triggerableAnim("wake_default", WAKE_ANIM)*/.triggerableAnim("dead", FAN);

	private static final TrackedData<Boolean> OFF = DataTracker.registerData(CyborgAnimationTestEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> WAKE = DataTracker.registerData(CyborgAnimationTestEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	public int tickEye;
	public boolean reverse;
	//public boolean wake;
	//public boolean off;

	public CyborgAnimationTestEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
		//off = true;
	}

	@Override
	public Iterable<ItemStack> getArmorItems() {
		return null;
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(OFF, true);
		builder.add(WAKE, false);
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {

	}

	@Override
	public boolean shouldRenderName() {
		return false;
	}

//	public void animateEyesColor() {
//		if (tickEye <= 255 && !reverse) {
//			tickEye += 40;
//		} else {
//			reverse = true;
//		}
//		if (tickEye > 0 && reverse) {
//			tickEye -= 40;
//		} else {
//			reverse = false;
//		}
//	}

	@Override
	public boolean isDead() {
		return super.isDead();
	}

	@Override
	protected void updatePostDeath() {
		super.updatePostDeath();
	}

	@Override
	public void onDeath(DamageSource damageSource) {

	}

	@Override
	public void tick() {
		if (!this.getWorld().isClient && !this.getDataTracker().get(WAKE)) {
			var list = this.getWorld().getNonSpectatingEntities(PlayerEntity.class, this.getBoundingBox().expand(3, -1, 3));
			if (!list.isEmpty()) {
				var player = list.get(this.getRandom().nextInt(list.size()));
				this.setOff(false);
				if (this.canSee(player)) {
					triggerAnim("wake", "fan");
					player.damage(getWorld().getDamageSources().mobAttack(this), 1);
					this.setWake(true);

				} else {
					triggerAnim("wake", "wake_default");
					this.setWake(true);

				}
			}
		}else{
			//triggerAnim("idle", "idle_cd");
		}
		if (getWorld().isClient) {
			tickEye = 255;
			//	animateEyesColor();
		}
		super.tick();
	}

	private void setOff(boolean state) {
		this.getDataTracker().set(OFF,state);
	}

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}

	public boolean isOff() {
		return this.getDataTracker().get(OFF);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		controllerRegistrar.add(test);
	}

	private void setWake(boolean state) {
		this.getDataTracker().set(WAKE,state);;
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoCache;
	}
}
