package tyaplyap.cyberbop.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Arm;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class FakePlayerEntity extends LivingEntity implements PolymerEntity {

	private final GameProfile ownerProfile;

	private Property cachedSkin = null;

	private int ownerModelParts;

	public FakePlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
		this.ownerProfile = null;
		this.ownerModelParts = 0x7f;
	}

	public FakePlayerEntity(EntityType<? extends LivingEntity> entityType, World world, GameProfile profile, int ownerModelParts) {
		super(entityType, world);
		this.ownerProfile = profile;
		this.ownerModelParts = ownerModelParts;
	}

	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putString("Skin", getSkin().value());
		nbt.putString("Sign", getSkin().signature());
		nbt.putInt("Parts", this.ownerModelParts);
	}

	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setSkin(new Property("textures",nbt.getString("Skin"), nbt.getString("Sign")));
		this.ownerModelParts = nbt.getInt("Parts");

	}

	private void setSkin(Property property) {
		if (!property.value().isEmpty() && !property.signature().isEmpty()) {
			this.cachedSkin = property;
		} else {
			this.cachedSkin = this.getSkin();
		}

	}

	public UUID getOwnerUUID(){
		return this.ownerProfile == null ? null : this.ownerProfile.getId();
	}

	public Property getSkin() {
		if (this.cachedSkin != null) {
			return this.cachedSkin;
		} else {
			if (this.ownerProfile != null && this.ownerProfile.getProperties().get("textures").stream().findAny().isPresent()) {
				return this.ownerProfile.getProperties().get("textures").stream().findAny().get();
			} else {
				return new Property("textures",
					"ewogICJ0aW1lc3RhbXAiIDogMTYxMzA4OTI4Mjg3NSwKICAicHJvZmlsZUlkIiA6ICIzZjM4YmViZGYwMWQ0MjNkYWI4MjczZjUwNGFiNGEyNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJjazM0Nzk0MjM1NzUzNzMxIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZkM2IwNmMzODUwNGZmYzAyMjliOTQ5MjE0N2M2OWZjZjU5ZmQyZWQ3ODg1Zjc4NTAyMTUyZjc3YjRkNTBkZTEiCiAgICB9CiAgfQp9",
					"dlKt7xkRL0L4RgrT1dtDmRYF26vrpAJEgzv4PpRN7rD4W5fZyt7MenACHIA6gEaNQeRlLtZV1D/IHZWMigjvXDOoePz9PyjvZ052zz+16HKhGBh3J0ecU/fWZmPTdzNoglwe3Ut3qmr8ClSKvvhlCx0ChXmEXZukMOsUOQh+imdBhXxS8ys+jIAeO2qwFWxJOqqnA6w95Yj3+nL0cMbko7KCiDU6luqgU6ddIBEYEyIyrYrhXMRugpJ6OJkMxVG6avhNETry11+rB10mEz9PFYESOu4BVRGAUV5ppRR4ax5rnPUqp3JeFjozxgF1VrdYQVghlF4r2fmEGBeM9nJNQHRYBoE9AG4AVVtC3pgekit5xY+bxxm9VgBXPif2+e9vDanrdKQltmSk6TRBc9ReheHElLREV1dORKEhJAW1FwieYB8Cl3gcYbtGyBSB57E1hG2TAsh1Kk5ZZLmAuglelhjV9N1HWM9vMuO2ldz+2hlIMTpXqcN8qad6C8R/0qdeoaGR/LxZLUQCx1g+vakzY9oc2N8gHtQxKbzk4OMcO+btHPuL43ZoLxULgnRiu56Yy2gW48J/UDaRSR+cYWxoNWSBXDsDh8zxSCdgf0nq8/s27nkAmxo5S8+G8L9W2Gg6oPhF5NeJ0mRlhIkaVYh1+ftbENPJmKulL+Y2DS0Hydw=");
			}
		}
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

		GameProfile gameProfile = new GameProfile(this.getUuid(), ownerProfile == null ? "entity.player" : this.ownerProfile.getName());

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


}
