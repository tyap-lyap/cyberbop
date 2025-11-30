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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.block.entity.FurnaceGeneratorBlockEntity;
import tyaplyap.cyberbop.item.CyberbopItems;

import java.util.List;

public class FurnaceGeneratorBlock extends BlockWithEntity {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty LIT = Properties.LIT;

	public FurnaceGeneratorBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(LIT, false));
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return createCodec(FurnaceGeneratorBlock::new);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	protected boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
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
		builder.add(FACING, LIT);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient) {
			if (world.getBlockEntity(pos) instanceof FurnaceGeneratorBlockEntity furnaceGeneratorBlock) {
				//furnaceGeneratorBlock.updateListeners();
			}
			NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

			if (screenHandlerFactory != null) {
				player.openHandledScreen(screenHandlerFactory);
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof FurnaceGeneratorBlockEntity furnaceGeneratorBlock) {
				ItemScatterer.spawn(world, pos, furnaceGeneratorBlock);
				super.onStateReplaced(state, world, pos, newState, moved);
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : validateTicker(type, CyberbopBlockEntities.SOLID_FUEL_GENERATOR, FurnaceGeneratorBlockEntity::tick);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FurnaceGeneratorBlockEntity(pos, state);
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.addAll(Text.literal("§b64000 §7Energy Capacity").getWithStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY))));
		tooltip.addAll(Text.literal("§b16/t §7Generation Rate").getWithStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY))));

		if (stack.get(CyberbopItems.STORED_ENERGY_COMPONENT) != null) {
			tooltip.addAll(Text.literal("§b" + stack.get(CyberbopItems.STORED_ENERGY_COMPONENT) + " §7Energy Stored").getWithStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY))));
		}
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(LIT)) {
			double x = (double)pos.getX() + 0.5D;
			double y = pos.getY();
			double z = (double)pos.getZ() + 0.5D;
			if (random.nextDouble() < 0.1) {
				world.playSound(x, y, z, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = state.get(FACING);
			Direction.Axis axis = direction.getAxis();
			double range = random.nextDouble() * 0.6 - 0.3;
			double xi = axis == Direction.Axis.X ? (double)direction.getOffsetX() * 0.52 : range;
			double yi = random.nextDouble() * (double)6.0F / (double)16.0F + 0.2D;
			double zi = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * 0.52 : range;
			world.addParticle(ParticleTypes.SMOKE, x + xi, y + yi, z + zi, 0.0F, 0.0F, 0.0F);
			world.addParticle(ParticleTypes.FLAME, x + xi, y + yi, z + zi, 0.0F, 0.0F, 0.0F);

			if (random.nextFloat() < 0.11F) {
				for(int i = 0; i < random.nextInt(2) + 2; ++i) {
					CampfireBlock.spawnSmokeParticle(world, pos, false, false);
				}
			}
		}
	}
}
