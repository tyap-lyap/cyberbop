package tyaplyap.cyberbop.item;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import tyaplyap.cyberbop.client.util.ClientJetpackHandler;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.util.JetpackUseTracker;

import java.util.List;

public class JetpackModule extends CyborgModuleItem {

	public JetpackModule(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension ex) {
		if (ex.isCyborg() && ex.containsModule(CyberbopItems.JETPACK_MODULE) && !ex.containsModule(CyberbopItems.FLIGHT_MODULE) && !player.isCreative() && !player.isSpectator()) {

			if (JetpackUseTracker.usesJetpack(player.getUuid())) {
				if (ex.getEnergyStored() > 0) {
					int energyConsume = 4;
					if (player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ElytraItem && player.isFallFlying()) {
						energyConsume = energyConsume * 3;
					}
					ex.setEnergyStored(Math.max(ex.getEnergyStored() - energyConsume, 0));
					JetpackModule.useJetpack(player);
					spawnJetpackEffects(world, player);
					player.fallDistance = 0;
				}
			}
		}
	}

	public static void useJetpack(PlayerEntity player) {
		if (player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ElytraItem && player.isFallFlying()) {
			updateElytraVelocity(player);
		}
		else {
			updateVelocity(player);
		}
	}

	public static void updateVelocity(PlayerEntity player) {
		double newVerticalVelocity = player.getVelocity().y + 0.1;

		if (newVerticalVelocity < 0.5) {
			player.addVelocity(0, 0.1, 0);
		}
	}

	private static void updateElytraVelocity(PlayerEntity player) {
		Vec3d velocity = player.getVelocity();
		Vec3d lookVector = player.getRotationVector();
		Vec3d boostVector = new Vec3d(
			lookVector.x * 0.1D,
			0.05D + (lookVector.y * 0.05D),
			lookVector.z * 0.1D
		);

		Vec3d newVelocity = velocity.add(boostVector);

		double speedLimit = 1.5D;
		if (newVelocity.length() > speedLimit) {
			newVelocity = newVelocity.normalize().multiply(speedLimit);
		}

		player.setVelocity(newVelocity);
	}

	@Override
	public void clientTick(ClientWorld world, PlayerEntity player, PlayerExtension extension) {
		ClientJetpackHandler.tick();
	}

	private static void spawnJetpackEffects(ServerWorld world, PlayerEntity player) {
		if(player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ElytraItem && player.isFallFlying()) {
			if (world.getRandom().nextFloat() < 0.1F) {
				world.playSound(
					null,
					player.getX(),
					player.getY(),
					player.getZ(),
					SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH,
					SoundCategory.PLAYERS,
					0.2F,
					1.5F
				);
			}
		}
		else {
			if (world.getRandom().nextFloat() < 0.2F) {
				world.playSound(
					null,
					player.getX(),
					player.getY(),
					player.getZ(),
					SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE,
					SoundCategory.PLAYERS,
					0.4F,
					1.0F + (player.getRandom().nextFloat() * 0.3F - 0.15F)
				);
			}
		}

		Vec3d lookVector = player.getRotationVector(0, player.bodyYaw);

		spawnParticles(world, player, ParticleTypes.FLAME,
			player.getX() -(lookVector.x * 0.35),
			player.getY() + 1,
			player.getZ() -(lookVector.z * 0.35), 1,
			0.0, 0.0, 0.0, 0.03);
	}

	static void spawnParticles(ServerWorld world, PlayerEntity jetpackUser, ParticleEffect particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed) {
		ParticleS2CPacket packet = new ParticleS2CPacket(particle, false, x, y, z, (float)deltaX, (float)deltaY, (float)deltaZ, (float)speed, count);

		for(ServerPlayerEntity player : world.getPlayers()) {
			if(!player.getUuid().equals(jetpackUser.getUuid())) {
				world.sendToPlayerIfNearby(player, false, x, y, z, packet);
			}
		}
	}

	// TODO translatable tooltips
	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Double jump to start ascending").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("and hold to continue flying.").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("Consumes 4/t of energy.").formatted(Formatting.GRAY));
	}
}
