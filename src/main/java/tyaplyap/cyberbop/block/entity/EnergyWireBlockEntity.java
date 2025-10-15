package tyaplyap.cyberbop.block.entity;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import tyaplyap.cyberbop.packet.DebugCablePacket;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

import java.util.*;

public class EnergyWireBlockEntity extends BlockEntity {
	public static final List<EnergyWireBlockEntity> wires = new ArrayList<>();
	public static final List<EnergyStorage> storages = new ArrayList<>();
	private static final Deque<EnergyWireBlockEntity> QueueWires = new ArrayDeque<>();
	public static int age = 0;
	public int lastage = 0;
	private EnergyWireBlockEntity ownerCable;

	public EnergyWireBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.ENERGY_WIRE, pos, state);
	}

	public void addNeighborsStorages(EnergyWireBlockEntity wire, List<EnergyStorage> storages) {
		for (var directionNeighbor : Direction.values()) {
			EnergyStorage energyStorage = EnergyStorage.SIDED.find(wire.getWorld(), wire.getPos().offset(directionNeighbor), directionNeighbor.getOpposite());
			if (energyStorage != null && energyStorage.type() != null) {
				storages.add(energyStorage);
			}
		}
	}

	public static void tick(World world, BlockPos pos, BlockState state, EnergyWireBlockEntity blockEntity) {
		List<Direction> directions = new ArrayList<>(Arrays.stream(Direction.values()).toList());
		Collections.shuffle(directions);
			if ((blockEntity.lastage == age)) return;
			QueueWires.add(blockEntity);
			blockEntity.lastage = age;
			wires.add(blockEntity);

			while (!QueueWires.isEmpty()) {
				EnergyWireBlockEntity current = QueueWires.removeFirst();

				for (Direction direction : Direction.values()) {
					if (current.getWorld().getBlockEntity(current.getPos().offset(direction)) instanceof EnergyWireBlockEntity wire && !wires.contains(current.getWorld().getBlockEntity(current.getPos().offset(direction)))) {
						if (wire.lastage != age) {
							QueueWires.add(wire);
							wire.lastage = age;
							wires.add(wire);

							if (!wire.equals(blockEntity)) {
								wire.ownerCable = blockEntity;
							}
						}
					}
				}
			}
			if (wires.isEmpty()) return;

			List<EnergyStorage> BATTERY = new ArrayList<>();
			List<EnergyStorage> GENERATOR = new ArrayList<>();
			List<EnergyStorage> RECEIVER = new ArrayList<>();

			for (EnergyWireBlockEntity wire : wires) {


				wire.addNeighborsStorages(wire, storages);
				for (var storage : storages) {
					if (storage.type().equals(IEnergyStorage.Type.GENERATOR) && !GENERATOR.contains(storage)) {
						GENERATOR.add(storage);
					} else if (storage.type().equals(IEnergyStorage.Type.BATTERY) && !BATTERY.contains(storage)) {
						BATTERY.add(storage);
					} else if (storage.type().equals(IEnergyStorage.Type.RECEIVER) && !RECEIVER.contains(storage)) {
						RECEIVER.add(storage);
					}
				}
			}

			for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, blockEntity.getPos())) {
				HitResult hitResult = player.raycast(player.getBlockInteractionRange(), 0.0F, false);
				if (hitResult.getType() == HitResult.Type.BLOCK) {
					BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();

					if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
					for (EnergyWireBlockEntity wire : wires) {
						if (world.getBlockEntity(blockPos) instanceof EnergyWireBlockEntity cableSearch) {
							if (cableSearch.ownerCable != null && cableSearch.ownerCable.equals(wire.ownerCable)) {
								ServerPlayNetworking.send(player, new DebugCablePacket(wire.getPos(), false, false));
								if (wires.getFirst().ownerCable == null) {
									ServerPlayNetworking.send(player, new DebugCablePacket(wires.getFirst().getPos(), false, true));
								}

							}
						}
					}
				}
			}
				if (!GENERATOR.isEmpty()) {
					if (!RECEIVER.isEmpty()) {
						blockEntity.transferEnergy(GENERATOR, RECEIVER);
					} else if (!BATTERY.isEmpty()) {
						blockEntity.transferEnergy(GENERATOR, BATTERY);
					}
				}
				 if (!BATTERY.isEmpty()) {
					if (!RECEIVER.isEmpty()) {
						blockEntity.transferEnergy(BATTERY, RECEIVER);

					}
				}
			wires.clear();
			storages.clear();
			QueueWires.clear();
			blockEntity.ownerCable = null;
		}
	}

	private boolean isStoragesFull(List<EnergyStorage> storages) {
		for (var rec : storages) {
			if (!rec.isFull()) {
				return false;
			}
		}
		return true;
	}

	private void transferEnergy(List<EnergyStorage> sources, List<EnergyStorage> targets) {
		for (var source : sources) {
				for (var target : targets) {
					EnergyStorage.transfer(source, target, source.transferRate());
				}
		}
	}

	static {
		ServerTickEvents.START_SERVER_TICK.register(server -> age++);
	}
}
