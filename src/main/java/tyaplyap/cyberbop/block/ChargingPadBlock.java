package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.ChargingPadBlockEntity;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.block.entity.EnergyBlockEntity;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;

import java.util.List;

public class ChargingPadBlock extends BlockWithEntity implements WireConnectable {

	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 5.0, 16.0);

	protected ChargingPadBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		ChargingPadBlockEntity chargingPadBlock = world.getBlockEntity(pos, CyberbopBlockEntities.CHARGING_PAD).orElse(null);
		if (!world.isClient) {
			if (chargingPadBlock == null) {
				CyberbopMod.LOGGER.warn("Ignoring receive energy attempt for Charging Pad without matching block entity at {}", pos);
			} else {
				if (0 < chargingPadBlock.getEnergyStored() && entity instanceof ServerPlayerEntity player && player instanceof PlayerExtension cyborg && cyborg.isCyborg() && cyborg.getEnergyStored() != cyborg.getCapacity()) {
					 if (EnergyStorage.transfer(chargingPadBlock.energyStorage, cyborg.getEnergyStorage(), chargingPadBlock.getTransferRate(), chargingPadBlock.typeMachine()) > 0) chargingPadBlock.markDirty();
				}
			}
		}
		super.onSteppedOn(world, pos, state, entity);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return createCodec(ChargingPadBlock::new);
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ChargingPadBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return !world.isClient ? validateTicker(type, CyberbopBlockEntities.BATTERY_TEST, EnergyBlockEntity::BatteryTick) : null;
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("64000 Energy Capacity").formatted(Formatting.GRAY));

		if (stack.get(CyberbopItems.STORED_ENERGY_COMPONENT) != null) {
			tooltip.addAll(Text.literal(stack.get(CyberbopItems.STORED_ENERGY_COMPONENT) + " Energy Stored").getWithStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY))));
		}
	}

	@Override
	public boolean canConnect(BlockState state, BlockPos pos, BlockState wireState, BlockPos wirePos, Direction direction) {
		return !direction.equals(Direction.DOWN);
	}
}
