package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.item.*;
import tyaplyap.cyberbop.util.CyborgPartType;

public class AssemblerBlock extends BlockWithEntity {

	protected AssemblerBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return createCodec(AssemblerBlock::new);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AssemblerBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		// Make sure to check world.isClient if you only want to tick only on serverside.
		return world.isClient ? validateTicker(type, CyberbopBlockEntities.ASSEMBLER, AssemblerBlockEntity::clientTick) : null;
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if(!world.isClient() && world.getBlockEntity(pos) instanceof AssemblerBlockEntity assembler) {
			ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

			if(stack.getItem() instanceof CyborgModuleItem moduleItem) {
				if(assembler.hasEmptyModuleSlot() && !assembler.containsModule(moduleItem)) {
					assembler.addModule(stack);
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}
			}

			if(stack.getItem() instanceof CyborgPartItem partItem) {
				for(CyborgPartType partType : CyborgPartType.values()) {
					if(partItem.getPartName(partType) != null && assembler.getPartStack(partType).isEmpty()) {
						assembler.setPartStack(partType, stack);
						player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
						assembler.updateListeners();
						return ActionResult.SUCCESS;
					}
				}
			}

			NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
			if (screenHandlerFactory != null) {
				player.openHandledScreen(screenHandlerFactory);
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof AssemblerBlockEntity assembler) {
				ItemScatterer.spawn(world, pos, assembler);
				super.onStateReplaced(state, world, pos, newState, moved);
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
}
