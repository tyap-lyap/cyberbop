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
import tyaplyap.cyberbop.util.transfer.BlockEnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

import java.util.*;

public class EnergyWireBlockEntity extends BlockEntity {
	public static final List<EnergyWireBlockEntity> wires = new ArrayList<>();
	public static final Map<BlockEnergyStorage, List<Direction>> storages = new HashMap<>();
	public static final Map<BlockEnergyStorage, List<Direction>> receivers = new HashMap<>();
	private static final Deque<EnergyWireBlockEntity> QueueWires = new ArrayDeque<>();
	public static int age = 0;
	public int lastage = 0;
	private EnergyWireBlockEntity ownerCable;

	public EnergyWireBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.ENERGY_WIRE, pos, state);
	}

	public void addNeighborsStorages(EnergyWireBlockEntity wire) {
		for (var directionNeighbor : Direction.values()) {
			BlockEnergyStorage energyStorage = BlockEnergyStorage.SIDED.find(wire.getWorld(), wire.getPos().offset(directionNeighbor), directionNeighbor.getOpposite());
			if (energyStorage != null && energyStorage.type() != null) {
				if (energyStorage.type().equals(IEnergyStorage.Type.RECEIVER) && !energyStorage.isFull()) {
					addStorages(receivers, energyStorage, directionNeighbor);
				}
				if (energyStorage.type().equals(IEnergyStorage.Type.BATTERY) || energyStorage.type().equals(IEnergyStorage.Type.GENERATOR)) {
					addStorages(storages, energyStorage, directionNeighbor);
				}
			}
		}
	}

	 private void addStorages(Map<BlockEnergyStorage, List<Direction>> blockEnergyMap, BlockEnergyStorage storage, Direction direction) {
		 if (blockEnergyMap.containsKey(storage)) {
			 if (!blockEnergyMap.get(storage).isEmpty()) {
				 List<Direction> directions = blockEnergyMap.get(storage);
				 directions.add(direction.getOpposite());
				 blockEnergyMap.put(storage, directions);
			 }
		 } else {
			 List<Direction> directions = new ArrayList<>();
			 directions.add(direction.getOpposite());
			 blockEnergyMap.put(storage, directions);
		 }

	 }

	public static void tick(World world, BlockPos pos, BlockState state, EnergyWireBlockEntity blockEntity) {
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

			for (EnergyWireBlockEntity wire : wires) {
				wire.addNeighborsStorages(wire);
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
			}
			blockEntity.transferEnergy(storages, receivers);

			blockEntity.transferEnergy(storages, storages);
			wires.clear();
			storages.clear();
			receivers.clear();
			QueueWires.clear();
			blockEntity.ownerCable = null;
	}

	public boolean canIO (BlockEnergyStorage storage ,List<Direction> directions, boolean input) {
		for (var direction : directions) {
			if (storage.directionIO.containsKey(direction) && input && (storage.directionIO.get(direction).equals(BlockEnergyStorage.TypeIO.INPUT) || storage.directionIO.get(direction).equals(BlockEnergyStorage.TypeIO.IO))) {
				return true;
			}
			if (storage.directionIO.containsKey(direction) && !input && (storage.directionIO.get(direction).equals(BlockEnergyStorage.TypeIO.OUTPUT) || storage.directionIO.get(direction).equals(BlockEnergyStorage.TypeIO.IO))) {
				return true;
			}
		}
		return false;
	}

	private void transferEnergy(Map<BlockEnergyStorage, List<Direction>> sources, Map<BlockEnergyStorage, List<Direction>> targets) {
		if (!sources.isEmpty() && !targets.isEmpty()) {
			for (var source : sources.keySet()) {
				int balancedEnergy = Math.min(source.transferRate(), source.storedEnergy) / targets.size();
				for (var target : targets.keySet()) {
					if (canIO(source, sources.get(source), false) && canIO(target, targets.get(target), true)) {
						BlockEnergyStorage.transfer(source, target, Math.clamp(balancedEnergy, 1, source.transferRate()), IEnergyStorage.Type.WIRE);
					}
				}
			}
		}
	}

	static {
		ServerTickEvents.START_SERVER_TICK.register(server -> age++);
	}
}
