package tyaplyap.cyberbop.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Arm;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class FakePlayerEntity extends LivingEntity implements PolymerEntity {

	protected static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(FakePlayerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

	private String ownerName;

	private Property cachedSkin;

	private int ownerModelParts;

	public FakePlayerEntity(EntityType<? extends FakePlayerEntity> entityType, World world) {
		super(entityType, world);
	}

	public FakePlayerEntity(EntityType<? extends LivingEntity> entityType, World world, ServerPlayerEntity player) {
		super(entityType, world);
		GameProfile ownerProfile = player.getGameProfile();
		this.setOwnerUuid(ownerProfile.getId());
		this.ownerModelParts = player.getClientOptions().playerModelParts();
		if (ownerProfile.getProperties().get("textures").stream().findAny().isPresent()) {
			cachedSkin = ownerProfile.getProperties().get("textures").stream().findAny().get();
		}
		this.ownerName = ownerProfile.getName();
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(OWNER_UUID, Optional.empty());
	}

	@Nullable
	public LivingEntity getOwner() {
		UUID uUID = this.getOwnerUuid();
		return uUID == null ? null : this.getWorld().getPlayerByUuid(uUID);
	}

	@Nullable
	public UUID getOwnerUuid() {
		return this.dataTracker.get(OWNER_UUID).orElse(null);
	}

	public void setOwnerUuid(@Nullable UUID uuid) {
		this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
	}

	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putString("Skin", getSkin().value());
		nbt.putString("Sign", getSkin().signature());
		nbt.putInt("Parts", this.ownerModelParts);
		if (this.getOwnerUuid() != null) {
			nbt.putUuid("Owner", this.getOwnerUuid());
		}
		nbt.putString("OwnerName", this.getOwnerName());
	}

	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.cachedSkin = new Property("textures",nbt.getString("Skin"), nbt.getString("Sign"));
		this.ownerModelParts = nbt.getInt("Parts");
		this.ownerName = nbt.getString("OwnerName");
		UUID uUID;
		if (nbt.containsUuid("Owner")) {
			uUID = nbt.getUuid("Owner");
		} else {
			String string = nbt.getString("Owner");
			uUID = ServerConfigHandler.getPlayerUuidByName(this.getServer(), string);
		}
		if (uUID != null) {
			try {
				this.setOwnerUuid(uUID);
			} catch (Throwable var4) {
				this.remove(RemovalReason.DISCARDED);
			}
		}
	}

	public String getOwnerName() {
		return this.ownerName.isEmpty() ? "fake.player" : this.ownerName;
	}

	public Property getSkin() {
		return this.cachedSkin.signature().isEmpty() || this.cachedSkin.value().isEmpty() ? new Property("textures",
			"ewogICJ0aW1lc3RhbXAiIDogMTYxMzA4OTI4Mjg3NSwKICAicHJvZmlsZUlkIiA6ICIzZjM4YmViZGYwMWQ0MjNkYWI4MjczZjUwNGFiNGEyNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJjazM0Nzk0MjM1NzUzNzMxIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZkM2IwNmMzODUwNGZmYzAyMjliOTQ5MjE0N2M2OWZjZjU5ZmQyZWQ3ODg1Zjc4NTAyMTUyZjc3YjRkNTBkZTEiCiAgICB9CiAgfQp9",
			"dlKt7xkRL0L4RgrT1dtDmRYF26vrpAJEgzv4PpRN7rD4W5fZyt7MenACHIA6gEaNQeRlLtZV1D/IHZWMigjvXDOoePz9PyjvZ052zz+16HKhGBh3J0ecU/fWZmPTdzNoglwe3Ut3qmr8ClSKvvhlCx0ChXmEXZukMOsUOQh+imdBhXxS8ys+jIAeO2qwFWxJOqqnA6w95Yj3+nL0cMbko7KCiDU6luqgU6ddIBEYEyIyrYrhXMRugpJ6OJkMxVG6avhNETry11+rB10mEz9PFYESOu4BVRGAUV5ppRR4ax5rnPUqp3JeFjozxgF1VrdYQVghlF4r2fmEGBeM9nJNQHRYBoE9AG4AVVtC3pgekit5xY+bxxm9VgBXPif2+e9vDanrdKQltmSk6TRBc9ReheHElLREV1dORKEhJAW1FwieYB8Cl3gcYbtGyBSB57E1hG2TAsh1Kk5ZZLmAuglelhjV9N1HWM9vMuO2ldz+2hlIMTpXqcN8qad6C8R/0qdeoaGR/LxZLUQCx1g+vakzY9oc2N8gHtQxKbzk4OMcO+btHPuL43ZoLxULgnRiu56Yy2gW48J/UDaRSR+cYWxoNWSBXDsDh8zxSCdgf0nq8/s27nkAmxo5S8+G8L9W2Gg6oPhF5NeJ0mRlhIkaVYh1+ftbENPJmKulL+Y2DS0Hydw=")
			: this.cachedSkin;
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return source.getType().effects().getSound();
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_PLAYER_DEATH;
	}

	@Override
	public Iterable<ItemStack> getArmorItems() {
		return new HashSet<>();
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {

	}

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}

	@Override
	public EntityType<?> getPolymerEntityType(ServerPlayerEntity serverPlayerEntity) {
		return EntityType.PLAYER;
	}

	@Override
	public void onBeforeSpawnPacket(ServerPlayerEntity player, Consumer<Packet<?>> packetConsumer) {
		PolymerEntity.super.onBeforeSpawnPacket(player, packetConsumer);

		GameProfile gameProfile = new GameProfile(this.getUuid(), this.ownerName);

		gameProfile.getProperties().put("textures", this.getSkin());
		var packet = PolymerEntityUtils.createMutablePlayerListPacket(EnumSet.of(PlayerListS2CPacket.Action.ADD_PLAYER, PlayerListS2CPacket.Action.UPDATE_LISTED));
		packet.getEntries().add(new PlayerListS2CPacket.Entry(this.uuid, gameProfile, false, 0, GameMode.SURVIVAL, null, null));
		packetConsumer.accept(packet);
	}


	@Override
	public void modifyRawTrackedData(List<DataTracker.SerializedEntry<?>> data, ServerPlayerEntity player, boolean initial) {
		PolymerEntity.super.modifyRawTrackedData(data, player, initial);
		data.add(DataTracker.SerializedEntry.of(PlayerEntity.PLAYER_MODEL_PARTS,(byte) this.ownerModelParts));
	}

	@Override
	public boolean canMoveVoluntarily() {
		return false;
	}

	public boolean isPushable() {
		return false;
	}

	protected void pushAway(Entity entity) {
	}

	@Override
	public void onDeath(DamageSource damageSource) {
		super.onDeath(damageSource);
		if(getOwnerUuid() != null && getServer() != null) {
			var player = getServer().getPlayerManager().getPlayer(this.getOwnerUuid());
			if(player != null) {
				player.kill();
			}
		}

	}
}
