package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.CyberbopBlocks;

public class CyberbopBlockEntities {

	public static final BlockEntityType<ControllerBlockEntity> CONTROLLER = register(
		"controller",
		BlockEntityType.Builder.create(ControllerBlockEntity::new, CyberbopBlocks.CONTROLLER).build()
	);

	public static final BlockEntityType<AssemblerBlockEntity> ASSEMBLER = register(
		"assembler",
		BlockEntityType.Builder.create(AssemblerBlockEntity::new, CyberbopBlocks.ASSEMBLER).build()
	);

	public static void init() {

	}

	public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, CyberbopMod.id(path), blockEntityType);
	}
}
