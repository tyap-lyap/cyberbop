package tyaplyap.cyberbop.client.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.item.JetpackModule;
import tyaplyap.cyberbop.packet.UseJetpackPacket;

public class ClientJetpackHandler {
	private static boolean jumped = false;
	private static long lastJumpTime = 0;
	private static boolean jetpackActive = false;
	private static boolean hasActivatedThisFlight = false;
	private static boolean usingJetpack = false;

	private static final long DOUBLE_JUMP_THRESHOLD = 300;

	public static void tick() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return;

		if (player instanceof PlayerExtension ex && ex.isCyborg() && ex.containsModule(CyberbopItems.JETPACK_MODULE) && !ex.containsModule(CyberbopItems.FLIGHT_MODULE) && !player.isCreative() && !player.isSpectator()) {
			boolean isHoldingJump = MinecraftClient.getInstance().options.jumpKey.isPressed();

			if (player.isOnGround()) {
				hasActivatedThisFlight = false;
				jetpackActive = false;
			}

			if (isHoldingJump && !jumped) {
				handleJumpPress(player);
			}

			if (jetpackActive && isInAir(player) && isHoldingJump) {
				if(ex.getEnergyStored() > 0) {
					spawnJetpackEffects(player);
					JetpackModule.useJetpack(player);
					player.fallDistance = 0;

					if(!usingJetpack) {
						ClientPlayNetworking.send(new UseJetpackPacket(true));
						usingJetpack = true;
					}
				}
				else {
					deactivateJetpack(player);
				}
			}
			else {
				if(usingJetpack) {
					ClientPlayNetworking.send(new UseJetpackPacket(false));
					usingJetpack = false;
				}
			}

			jumped = isHoldingJump;
		}
	}

	private static boolean isUsingElytra(ClientPlayerEntity player) {
		return player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ElytraItem && player.isFallFlying();
	}

	private static boolean isInAir(ClientPlayerEntity player) {
		return !player.isOnGround() && !player.isTouchingWater() && !player.isClimbing();
	}

	private static void handleJumpPress(ClientPlayerEntity player) {
		long currentTime = System.currentTimeMillis();

		if (isInAir(player)) {
			if (currentTime - lastJumpTime < DOUBLE_JUMP_THRESHOLD) {
				if (!hasActivatedThisFlight && activateJetpack(player)) {
					hasActivatedThisFlight = true;
					jetpackActive = true;
				}
			}
			else if (hasActivatedThisFlight) {
				jetpackActive = true;
			}
		}

		lastJumpTime = currentTime;
	}

	private static boolean activateJetpack(ClientPlayerEntity player) {
		if (((PlayerExtension)player).getEnergyStored() <= 0) {
			player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.3F, 1.0F);
			return false;
		}
		if (isUsingElytra(player)) {
			elytraHeadStart(player);
			player.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.3F, 1.2F);
		}
		else {
			Vec3d velocity = player.getVelocity();
			double boostVelocity = 0.3;

			if (velocity.y < 0.5) {
				double newVerticalVelocity = Math.max(velocity.y + boostVelocity, boostVelocity);
				player.setVelocity(velocity.x, newVerticalVelocity, velocity.z);
			}
			player.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 0.3F, 1.5F);
		}

		spawnActivationParticles(player);
		return true;
	}

	private static void elytraHeadStart(PlayerEntity player) {
		Vec3d velocity = player.getVelocity();
		Vec3d lookVector = player.getRotationVec(1.0F);

		Vec3d boostVector = new Vec3d(
			lookVector.x * 0.5,
			0.2 + (lookVector.y * 0.3),
			lookVector.z * 0.5
		);

		Vec3d newVelocity = velocity.add(boostVector);

		double speedLimit = 1.5;
		if (newVelocity.length() > speedLimit) {
			newVelocity = newVelocity.normalize().multiply(speedLimit);
		}

		player.setVelocity(newVelocity);
	}

	private static void deactivateJetpack(ClientPlayerEntity player) {
		jetpackActive = false;
		player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.3F, 1.2F);
	}

	private static void spawnActivationParticles(ClientPlayerEntity player) {
		if (isUsingElytra(player)) {
			Vec3d lookVector = player.getRotationVec(1.0F);
			player.getWorld().addParticle(ParticleTypes.FIREWORK,
				player.getX(), player.getY() + 0.5, player.getZ(),
				lookVector.x * 0.5, lookVector.y * 0.5, lookVector.z * 0.5);
		}
	}

	private static void spawnJetpackEffects(ClientPlayerEntity player) {
		if (isUsingElytra(player)) {
			Vec3d lookVector = player.getRotationVec(1.0F);

			player.getWorld().addParticle(ParticleTypes.FLAME,
				player.getX() - (lookVector.x * 2),
				player.getY() - (lookVector.y * 0.5),
				player.getZ() - (lookVector.z * 2),
				-lookVector.x * 0.3, -lookVector.y * 0.3, -lookVector.z * 0.3);
		}
		else {
			Vec3d lookVector = player.getRotationVector(0, player.bodyYaw);

			player.getWorld().addParticle(ParticleTypes.FLAME,
				player.getX() -(lookVector.x * 0.35),
				player.getY() + 1,
				player.getZ() -(lookVector.z * 0.35),
				0.0, -0.1, 0.0);
		}
	}
}
