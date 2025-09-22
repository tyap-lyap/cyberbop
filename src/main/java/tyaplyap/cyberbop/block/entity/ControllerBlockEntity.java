package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ControllerBlockEntity extends BlockEntity {

	public ControllerBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.CONTROLLER, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState state, ControllerBlockEntity blockEntity) {

	}
}
