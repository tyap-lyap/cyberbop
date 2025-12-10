package tyaplyap.cyberbop.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import tyaplyap.cyberbop.extension.PlayerExtension;

import java.util.List;

public class FlightModule extends AnimatableCyborgModule {
	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	protected static final RawAnimation FLY = RawAnimation.begin().thenPlay("fly");
	protected static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");

	public FlightModule(Settings settings) {
		super(settings);
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension ex, ItemStack stack) {
		if (ex.isCyborg() && player.getAbilities().flying && !player.isSpectator()) {
			triggerAnim(player, GeoItem.getOrAssignId(stack, world), "flight_module", "fly");
			stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, world), "flight_module", "idle");
		} else {
			stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, world), "flight_module", "fly");
			triggerAnim(player, GeoItem.getOrAssignId(stack, world), "flight_module", "idle");
		}

		if(ex.isCyborg() && !player.isCreative() && !player.isSpectator()) {
			if (ex.getEnergyStored() >= 20) {
				if (!player.getAbilities().allowFlying) {
					player.getAbilities().allowFlying = true;
					player.sendAbilitiesUpdate();
				}
				if (player.getAbilities().flying) {
					ex.setEnergyStored(Math.max(ex.getEnergyStored() - 20, 0));
				} else {
					if (player.getAbilities().allowFlying) {
						player.getAbilities().allowFlying = false;
						player.getAbilities().flying = false;
						player.sendAbilitiesUpdate();
					}
				}
			}
		}
	}

	@Override
	public void onModuleRemoved(World world, PlayerEntity player) {
		if(!world.isClient() && !player.isCreative() && !player.isSpectator()) {
			player.getAbilities().allowFlying = false;
			player.getAbilities().flying = false;
			player.sendAbilitiesUpdate();
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Grants §fcreative flight §7but").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("consumes a lot of energy.").formatted(Formatting.GRAY));
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "flight_module", 5, state -> PlayState.STOP)
			.triggerableAnim("fly", FLY).triggerableAnim("idle", IDLE));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoCache;
	}
}
