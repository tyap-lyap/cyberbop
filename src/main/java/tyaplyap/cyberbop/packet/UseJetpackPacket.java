package tyaplyap.cyberbop.packet;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.util.JetpackUseTracker;

public record UseJetpackPacket(boolean useJetpack) implements CustomPayload {
	public static final CustomPayload.Id<UseJetpackPacket> ID = new CustomPayload.Id<>(CyberbopMod.id("use_jetpack"));
	public static final PacketCodec<RegistryByteBuf, UseJetpackPacket> CODEC = PacketCodec.of(
		(value, buf) -> buf.writeBoolean(value.useJetpack()),
		buf -> new UseJetpackPacket(buf.readBoolean())
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void registerC2SPackets() {
		PayloadTypeRegistry.playC2S().register(UseJetpackPacket.ID, UseJetpackPacket.CODEC);
	}

	public static void registerS2CPackets() {
		PayloadTypeRegistry.playS2C().register(UseJetpackPacket.ID, UseJetpackPacket.CODEC);
	}

	public static void registerServerReceivers() {
		ServerPlayNetworking.registerGlobalReceiver(UseJetpackPacket.ID, (payload, context) -> {
			JetpackUseTracker.setUseJetpack(context.player().getUuid(), payload.useJetpack());
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			JetpackUseTracker.removePlayer(handler.player.getUuid());
		});
	}

	public static void registerClientReceivers() {
	}
}
