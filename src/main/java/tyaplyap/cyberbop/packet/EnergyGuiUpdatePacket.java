package tyaplyap.cyberbop.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import tyaplyap.cyberbop.CyberbopMod;


public record EnergyGuiUpdatePacket(int storedEnergy, int capacity) implements CustomPayload {

	public static final Identifier PACKET_ID = Identifier.of(CyberbopMod.MOD_ID, "energy_gui_update");
	public static final Id<EnergyGuiUpdatePacket> ID = new Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, EnergyGuiUpdatePacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, EnergyGuiUpdatePacket::storedEnergy, PacketCodecs.INTEGER, EnergyGuiUpdatePacket::capacity, EnergyGuiUpdatePacket::new);


	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
