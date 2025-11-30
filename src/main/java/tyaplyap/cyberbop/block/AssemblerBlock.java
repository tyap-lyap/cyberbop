package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.item.*;
import tyaplyap.cyberbop.util.CyborgPartType;
import tyaplyap.cyberbop.util.DebugUtil;

import java.util.List;

public class AssemblerBlock extends BlockWithEntity {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

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
		return world.isClient ? validateTicker(type, CyberbopBlockEntities.ASSEMBLER, AssemblerBlockEntity::animateError) : null;
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

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("§b128000 §7Energy Capacity").formatted(Formatting.GRAY));

		if (stack.get(CyberbopItems.STORED_ENERGY_COMPONENT) != null) {
			tooltip.addAll(Text.literal("§b" + stack.get(CyberbopItems.STORED_ENERGY_COMPONENT) + " §7Energy Stored").getWithStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY))));
		}
	}
}
