package tyaplyap.cyberbop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.client.CyborgModel;
import tyaplyap.cyberbop.entity.FakePlayerEntity;
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
			if (player instanceof ServerPlayerEntity playerEntity) {
				FakePlayerEntity fakePlayerEntity = new FakePlayerEntity(CyberbopMod.FAKE_PLAYER_ENTITY,world, playerEntity.getGameProfile(), playerEntity.getClientOptions().playerModelParts());
				fakePlayerEntity.setPosition(playerEntity.getPos());
				world.spawnEntity(fakePlayerEntity);
			}
		}
		return ActionResult.SUCCESS;
	}
}
