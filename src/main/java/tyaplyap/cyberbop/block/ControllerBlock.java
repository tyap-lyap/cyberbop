package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.*;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.block.entity.ControllerBlockEntity;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyborgModuleItem;
import tyaplyap.cyberbop.item.CyborgPartItem;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.CyborgPartType;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

import java.util.List;
import java.util.Set;

public class ControllerBlock extends BlockWithEntity {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	protected static final VoxelShape NORTH_SHAPE = createNorthShape();
	protected static final VoxelShape SOUTH_SHAPE = createSouthShape();
	protected static final VoxelShape EAST_SHAPE = createEastShape();
	protected static final VoxelShape WEST_SHAPE = createWestShape();

	public ControllerBlock(Settings settings) {
		super(settings);
	}

	static VoxelShape createNorthShape() {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.1875, 0, 0.25, 0.8125, 0.875, 0.75), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.625, 0.0625, 1, 0.875, 0.3125), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.875, 0.5625, 1, 1.125, 0.8125), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.75, 0.3125, 1, 1, 0.5625), BooleanBiFunction.OR);
		return shape;
	}

	static VoxelShape createSouthShape() {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.1875, 0, 0.25, 0.8125, 0.875, 0.75), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.625, 0.6875, 1, 0.875, 0.9375), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.875, 0.1875, 1, 1.125, 0.4375), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.75, 0.4375, 1, 1, 0.6875), BooleanBiFunction.OR);
		return shape;
	}

	static VoxelShape createEastShape() {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.25, 0, 0.1875, 0.75, 0.875, 0.8125), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.6875, 0.625, 0, 0.9375, 0.875, 1), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.1875, 0.875, 0, 0.4375, 1.125, 1), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.4375, 0.75, 0, 0.6875, 1, 1), BooleanBiFunction.OR);
		return shape;
	}

	static VoxelShape createWestShape() {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.25, 0, 0.1875, 0.75, 0.875, 0.8125), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.0625, 0.625, 0, 0.3125, 0.875, 1), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.5625, 0.875, 0, 0.8125, 1.125, 1), BooleanBiFunction.OR);
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.3125, 0.75, 0, 0.5625, 1, 1), BooleanBiFunction.OR);
		return shape;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if(state.get(FACING).equals(Direction.NORTH)) return NORTH_SHAPE;
		if(state.get(FACING).equals(Direction.EAST)) return EAST_SHAPE;
		if(state.get(FACING).equals(Direction.SOUTH)) return SOUTH_SHAPE;
		if(state.get(FACING).equals(Direction.WEST)) return WEST_SHAPE;
		return NORTH_SHAPE;
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

//	@Override
//	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
//		// Make sure to check world.isClient if you only want to tick only on serverside.
//		return validateTicker(type, CyberbopBlockEntities.CONTROLLER, ControllerBlockEntity::tick);
//	}

	@Override
	protected boolean hasSidedTransparency(BlockState state) {
		return true;
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
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		Direction[] directions = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
		for(Direction direction : directions) {
			var assemblerPos = pos.add(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
			var blockEntity = world.getBlockEntity(assemblerPos);

			if(blockEntity instanceof AssemblerBlockEntity assembler && player instanceof PlayerExtension cyborg) {
				if (!cyborg.isCyborg() && assembler.isComplete()) {
					if(!world.isClient) {
						becomeCyborg(world, player, assemblerPos, assembler);
						player.teleport((ServerWorld) world, assemblerPos.getX() + 0.5, assemblerPos.getY() + 1, assemblerPos.getZ() + 0.5, Set.of(), assembler.getCachedState().get(AssemblerBlock.FACING).asRotation(), 0);
					}
					return ActionResult.SUCCESS;
				}
				else if (cyborg.isCyborg() && assembler.isEmpty()) {
					cyborg.getModules().forEach(stack -> {
						if(stack.getItem() instanceof CyborgModuleItem moduleItem) moduleItem.onModuleRemoved(world, player);
					});
					if(!world.isClient) {
						becomeFlesh(player, assembler);
					}
					return ActionResult.SUCCESS;
				}
			}
		}
		return ActionResult.PASS;
	}

	private void becomeFlesh(PlayerEntity player, AssemblerBlockEntity assembler) {
		PlayerExtension cyborg = (PlayerExtension)player;

		if (!assembler.isFull()) {
			EnergyStorage.transfer(cyborg.getEnergyStorage(), assembler.energyStorage, assembler.getCapacity(), IEnergyStorage.Type.CYBORG);
		}
		cyborg.setEnergyStored(0);
		cyborg.setCyborg(false);
		assembler.setStack(0, cyborg.getCyborgHead());
		assembler.setStack(1, cyborg.getCyborgBody());
		assembler.setStack(2, cyborg.getCyborgRightArm());
		assembler.setStack(3, cyborg.getCyborgLeftArm());
		assembler.setStack(4, cyborg.getCyborgRightLeg());
		assembler.setStack(5, cyborg.getCyborgLeftLeg());

		assembler.setModule(1, cyborg.getModule1());
		assembler.setModule(2, cyborg.getModule2());
		assembler.setModule(3, cyborg.getModule3());

		cyborg.clearAllParts();
		player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).removeModifier(CyberbopMod.id("cyborg_health"));

		cyborg.setModule1(ItemStack.EMPTY);
		cyborg.setModule2(ItemStack.EMPTY);
		cyborg.setModule3(ItemStack.EMPTY);
		assembler.updateListeners();
	}

	private void becomeCyborg(World world, PlayerEntity player, BlockPos assemblerPos, AssemblerBlockEntity assembler) {
		PlayerExtension cyborg = (PlayerExtension)player;
		cyborg.setCyborg(true);

		cyborg.setCyborgHead(assembler.getHead());
		cyborg.setCyborgBody(assembler.getBody());
		cyborg.setCyborgRightArm(assembler.getRightArm());
		cyborg.setCyborgLeftArm(assembler.getLeftArm());
		cyborg.setCyborgRightLeg(assembler.getRightLeg());
		cyborg.setCyborgLeftLeg(assembler.getLeftLeg());

		if (!assembler.getModule(1).isEmpty())
			if (assembler.getModule(1).getItem() instanceof CyborgModuleItem)
				cyborg.setModule1(assembler.getModule(1));
			else ItemScatterer.spawn(world, assemblerPos.getX(), assemblerPos.getY(), assemblerPos.getZ(), assembler.getModule(1));

		if (!assembler.getModule(2).isEmpty())
			if (assembler.getModule(2).getItem() instanceof CyborgModuleItem)
				cyborg.setModule2(assembler.getModule(2));
			else ItemScatterer.spawn(world, assemblerPos.getX(), assemblerPos.getY(), assemblerPos.getZ(), assembler.getModule(2));

		if (!assembler.getModule(3).isEmpty())
			if (assembler.getModule(3).getItem() instanceof CyborgModuleItem)
				cyborg.setModule3(assembler.getModule(3));
			else ItemScatterer.spawn(world, assemblerPos.getX(), assemblerPos.getY(), assemblerPos.getZ(), assembler.getModule(3));

		assembler.getItems().clear();
		EnergyStorage.transfer(assembler.energyStorage, cyborg.getEnergyStorage(), cyborg.getCapacity(), IEnergyStorage.Type.RECEIVER);
		assembler.updateListeners();
		cyborg.setupAttributes(player);
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.addAll(Text.literal("Use controller to take control of the cyborg.").getWithStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY))));
		tooltip.addAll(Text.literal("Place nearby assembler to connect.").getWithStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY))));
	}


}
