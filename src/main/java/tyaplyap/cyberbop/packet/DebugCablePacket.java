package tyaplyap.cyberbop.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import tyaplyap.cyberbop.CyberbopMod;

import java.util.ArrayList;
import java.util.List;

public record DebugCablePacket(BlockPos blockPos, boolean clean, boolean isOwner) implements CustomPayload {

	public static BlockPos ownerCable = null;
	public static List<BlockPos> debugCables = new ArrayList<>();

	public static final Identifier PACKET_ID = Identifier.of(CyberbopMod.MOD_ID, "debug_cable");
	public static final Id<DebugCablePacket> ID = new Id<>(PACKET_ID);
	public static final PacketCodec<ByteBuf, BlockPos> PACKET_CODEC1 = new PacketCodec<>() {
		@Override
		public void encode(ByteBuf buf, BlockPos value) {
				PacketByteBuf.writeBlockPos(buf, value);
		}

		public BlockPos decode(ByteBuf byteBuf) {
			return PacketByteBuf.readBlockPos(byteBuf);
		}
	};

	public static void getCables(BlockPos blockPos, boolean clean, boolean isOwner) {
		if (!debugCables.contains(blockPos)) {
			debugCables.add(blockPos);
		}
		if (clean){ debugCables.clear(); return;}
		if (isOwner) {
			ownerCable = blockPos;
		}
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static final PacketCodec<RegistryByteBuf, DebugCablePacket> PACKET_CODEC = PacketCodec.tuple(PACKET_CODEC1, DebugCablePacket::blockPos, PacketCodecs.BOOL, DebugCablePacket::clean,PacketCodecs.BOOL, DebugCablePacket::isOwner, DebugCablePacket::new);

}
