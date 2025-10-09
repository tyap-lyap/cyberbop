package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.ChargingPadBlockEntity;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.extension.PlayerExtension;

public class ChargingPadBlock extends BlockWithEntity {

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
				if (0 < chargingPadBlock.getEnergyStored() && entity instanceof ServerPlayerEntity player && player instanceof PlayerExtension cyborg && cyborg.isCyborg() && cyborg.getEnergyStored() != cyborg.capacity()) {
					if (chargingPadBlock.transferEnergy(chargingPadBlock.transferRate(), cyborg)) chargingPadBlock.markDirty();
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
}
