package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.CyberbopBlocks;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;
import tyaplyap.cyberbop.block.SolarPanelBlock;

public class CyberbopBlockEntities {

	public static final BlockEntityType<EnergyGeneratorBlockEntity> ENERGY_GENERATOR = register(
		 "energy_generator",
		BlockEntityType.Builder.create(EnergyGeneratorBlockEntity::new, CyberbopBlocks.ENERGY_GENERATOR).build()
	);

	public static final BlockEntityType<EnergyWireBlockEntity> ENERGY_WIRE = register(
		"energy_wire",
		BlockEntityType.Builder.create(EnergyWireBlockEntity::new, CyberbopBlocks.ENERGY_WIRE).build()
	);

	public static final BlockEntityType<EnergyTestReceiverBlockEntity> ENERGY_RECEIVER = register(
		"energy_receiver",
		BlockEntityType.Builder.create(EnergyTestReceiverBlockEntity::new, CyberbopBlocks.ENERGY_RECEIVER).build()
	);

	public static final BlockEntityType<BatteryTestBlockEntity> BATTERY_TEST = register(
		"battery_test",
		BlockEntityType.Builder.create(BatteryTestBlockEntity::new, CyberbopBlocks.BATTERY_TEST).build()
	);

	public static final BlockEntityType<SolarPanelBlockEntity> SOLAR_PANEL = register(
		"solar_panel",
		BlockEntityType.Builder.create((pos, state) -> ((SolarPanelBlock)state.getBlock()).createBlockEntity(pos, state), CyberbopBlocks.SOLAR_PANEL, CyberbopBlocks.ADVANCED_SOLAR_PANEL).build()
	);

	public static final BlockEntityType<ChargingPadBlockEntity> CHARGING_PAD = register(
		"charging_pad",
		BlockEntityType.Builder.create(ChargingPadBlockEntity::new, CyberbopBlocks.CHARGING_PAD).build()
	);

	public static final BlockEntityType<FurnaceGeneratorBlockEntity> FURNACE_GENERATOR = register(
		"furnace_generator",
		BlockEntityType.Builder.create(FurnaceGeneratorBlockEntity::new, CyberbopBlocks.FURNACE_GENERATOR).build()
	);

	public static final BlockEntityType<ControllerBlockEntity> CONTROLLER = register(
		"controller",
		BlockEntityType.Builder.create(ControllerBlockEntity::new, CyberbopBlocks.CONTROLLER).build()
	);

	public static final BlockEntityType<AssemblerBlockEntity> ASSEMBLER = register(
		"assembler",
		BlockEntityType.Builder.create(AssemblerBlockEntity::new, CyberbopBlocks.ASSEMBLER).build()
	);

	public static void init() {
		IEnergyStorage.SIDED.registerForBlockEntities((blockEntity, context) ->  ((EnergyBlockEntity) blockEntity).energyStorage, BATTERY_TEST, SOLAR_PANEL, ENERGY_RECEIVER, ASSEMBLER, FURNACE_GENERATOR, ENERGY_RECEIVER, CHARGING_PAD);
	}

	public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, CyberbopMod.id(path), blockEntityType);
	}
}
