package tyaplyap.cyberbop.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import tyaplyap.cyberbop.block.ControllerBlock;
import tyaplyap.cyberbop.extension.PlayerExtension;

public class LongArmModule extends AnimatableCyborgModule {

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	protected static final RawAnimation HOOK_ANIM = RawAnimation.begin().thenPlay("hook");

	public LongArmModule(Settings settings) {
		super(settings);
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
	}

	@Override
	public void controllerLogic(ControllerBlock controllerBlock, BlockPos pos, World world, PlayerEntity player, ItemStack itemStack) {
		if (world instanceof ServerWorld serverWorld) {
			GeoItem.getOrAssignId(itemStack, serverWorld);

		}
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension extension, ItemStack stack) {
			if (world instanceof ServerWorld serverWorld) {
				if (player.handSwinging) {
					triggerAnim(player, getOrAssignIdUpdate(stack, serverWorld, player), "hook", "hook");
				} else {
					stopTriggeredAnim(player, getOrAssignIdUpdate(stack, serverWorld, player), "hook", "hook");
				}
			}
		super.tick(world, player, extension, stack);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "hook", 0, state -> PlayState.STOP)
			.triggerableAnim("hook", HOOK_ANIM));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoCache;
	}

}
