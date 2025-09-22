package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AssemblerBlockEntity extends BlockEntity {

	public String head;
	public String rightArm;
	public String leftArm;
	public String body;
	public String rightLeg;
	public String leftLeg;

	public AssemblerBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.ASSEMBLER, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState state, AssemblerBlockEntity blockEntity) {

	}
}
