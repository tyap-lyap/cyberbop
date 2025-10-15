package tyaplyap.cyberbop.client.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyberbopItems;
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
			boolean isOnGround = player.isOnGround();
			boolean isInAir = !isOnGround && !player.isTouchingWater() && !player.isClimbing();
			boolean hasElytra = player.getEquippedStack(EquipmentSlot.CHEST).getItem().equals(Items.ELYTRA) && player.isFallFlying();

			if (isOnGround) {
				hasActivatedThisFlight = false;
				jetpackActive = false;
			}

			if (isHoldingJump && !jumped) {
				handleJumpPress(player, isInAir, hasElytra);
			}

			if (jetpackActive && isInAir && isHoldingJump) {
				if(ex.getEnergyStored() > 0) {
					spawnJetpackEffects(player, hasElytra);
					useJetpack(player, hasElytra);
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

	private static void handleJumpPress(ClientPlayerEntity player, boolean isInAir, boolean hasElytra) {
		long currentTime = System.currentTimeMillis();

		if (isInAir) {
			if (currentTime - lastJumpTime < DOUBLE_JUMP_THRESHOLD) {
				if (!hasActivatedThisFlight && activateJetpack(player, hasElytra)) {
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

	private static boolean activateJetpack(ClientPlayerEntity player, boolean hasElytra) {
		if (((PlayerExtension)player).getEnergyStored() <= 0) {
			player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.5F, 1.0F);
			return false;
		}
		if (hasElytra) {
			elytraHeadStart(player);
			player.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.4F, 1.2F);
		}
		else {
			Vec3d velocity = player.getVelocity();
			double boostVelocity = 0.3;

			if (velocity.y < 0.5) {
				double newVerticalVelocity = Math.max(velocity.y + boostVelocity, boostVelocity);
				player.setVelocity(velocity.x, newVerticalVelocity, velocity.z);
			}
			player.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 0.4F, 1.5F);
		}

		spawnActivationParticles(player, hasElytra);
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

		double speedLimit = 2.0;
		if (newVelocity.length() > speedLimit) {
			newVelocity = newVelocity.normalize().multiply(speedLimit);
		}

		player.setVelocity(newVelocity);
	}

	private static void useJetpack(ClientPlayerEntity player, boolean hasElytra) {
		if (hasElytra) {
			updateElytraVelocity(player);
		} else {
			updateVelocity(player);
		}
	}

	private static void updateVelocity(PlayerEntity player) {
		double newVerticalVelocity = player.getVelocity().y + 0.1;

		if (newVerticalVelocity < 0.5) {
			player.addVelocity(0, 0.1, 0);
		}
	}

	private static void updateElytraVelocity(PlayerEntity player) {
		Vec3d velocity = player.getVelocity();
		Vec3d lookVector = player.getRotationVec(1.0F);
		Vec3d boostVector = new Vec3d(
			lookVector.x * 0.1,
			0.05 + (lookVector.y * 0.05),
			lookVector.z * 0.1
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

	private static void spawnActivationParticles(ClientPlayerEntity player, boolean hasElytra) {
		if (hasElytra) {
			Vec3d lookVector = player.getRotationVec(1.0F);
			player.getWorld().addParticle(ParticleTypes.FIREWORK,
				player.getX(), player.getY() + 0.5, player.getZ(),
				lookVector.x * 0.5, lookVector.y * 0.5, lookVector.z * 0.5);
		}
	}

	private static void spawnJetpackEffects(ClientPlayerEntity player, boolean hasElytra) {
		if (hasElytra) {
			if (player.getWorld().random.nextFloat() < 0.1F) {
				player.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.2F, 1.5F);
			}
			Vec3d lookVector = player.getRotationVec(1.0F);

			player.getWorld().addParticle(ParticleTypes.FLAME,
				player.getX() - (lookVector.x * 2),
				player.getY() - (lookVector.y * 0.5),
				player.getZ() - (lookVector.z * 2),
				-lookVector.x * 0.3, -lookVector.y * 0.3, -lookVector.z * 0.3);
		}
		else {
			if (player.getWorld().random.nextFloat() < 0.2F) {
				player.playSound(SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, 0.2F,
					1.0F + (player.getWorld().random.nextFloat() * 0.3F - 0.15F));
			}
			Vec3d lookVector = player.getRotationVector(0, player.bodyYaw);

			player.getWorld().addParticle(ParticleTypes.FLAME,
				player.getX() -(lookVector.x * 0.35),
				player.getY() + 1,
				player.getZ() -(lookVector.z * 0.35),
				0.0, -0.1, 0.0);
		}
	}
}
