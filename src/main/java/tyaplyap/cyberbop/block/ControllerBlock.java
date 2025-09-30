package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.ControllerBlockEntity;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.client.CyborgModel;
import tyaplyap.cyberbop.entity.FakePlayerEntity;
import tyaplyap.cyberbop.extension.PlayerExtension;

public class ControllerBlock extends BlockWithEntity {

	public ControllerBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return createCodec(ControllerBlock::new);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ControllerBlockEntity(pos, state);
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		// Make sure to check world.isClient if you only want to tick only on serverside.
		return validateTicker(type, CyberbopBlockEntities.CONTROLLER, ControllerBlockEntity::tick);
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return false;
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if(!world.isClient()) {
			((PlayerExtension)player).setCyborg(!((PlayerExtension)player).isCyborg());
			player.sendMessage(Text.literal("isCyborg: " + ((PlayerExtension)player).isCyborg()));
			if (player instanceof ServerPlayerEntity playerEntity) {
				FakePlayerEntity fakePlayerEntity = new FakePlayerEntity(CyberbopMod.FAKE_PLAYER_ENTITY,world, playerEntity);
				fakePlayerEntity.setPosition(playerEntity.getPos());
				world.spawnEntity(fakePlayerEntity);
			}
		}
		return ActionResult.SUCCESS;
	}


}
