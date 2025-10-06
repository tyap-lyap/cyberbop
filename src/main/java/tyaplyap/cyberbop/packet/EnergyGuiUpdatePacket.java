package tyaplyap.cyberbop.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import tyaplyap.cyberbop.CyberbopMod;


public class EnergyGuiUpdatePacket implements CustomPayload {

	public final int storedEnergy;
	public final int capacity;

	public static final Identifier PACKET_ID = Identifier.of(CyberbopMod.MOD_ID,"energy_gui_update");
	public static final CustomPayload.Id<EnergyGuiUpdatePacket> ID = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, EnergyGuiUpdatePacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, EnergyGuiUpdatePacket::getStoredEnergy, PacketCodecs.INTEGER, EnergyGuiUpdatePacket::getCapacity, EnergyGuiUpdatePacket::new);


	public EnergyGuiUpdatePacket(int storedEnergy, int capacity) {
		this.storedEnergy = storedEnergy;
		this.capacity = capacity;
	}

	public int getStoredEnergy() {
		return storedEnergy;
	}

	public int getCapacity() {
		return capacity;
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
