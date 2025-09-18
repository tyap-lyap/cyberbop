package tyaplyap.cyberbop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyaplyap.cyberbop.extension.PlayerExtension;

public class ControllerBlock extends Block {

	public ControllerBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if(!world.isClient()) {
			((PlayerExtension)player).setCyborg(!((PlayerExtension)player).isCyborg());
			player.sendMessage(Text.literal("isCyborg: " + ((PlayerExtension)player).isCyborg()));
		}
		return ActionResult.SUCCESS;
	}
}
