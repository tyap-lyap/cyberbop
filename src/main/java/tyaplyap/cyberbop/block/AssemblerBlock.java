package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.item.CyborgPartItem;

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
		return validateTicker(type, CyberbopBlockEntities.ASSEMBLER, AssemblerBlockEntity::tick);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if(!world.isClient() && world.getBlockEntity(pos) instanceof AssemblerBlockEntity assembler) {

			ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

			if(stack.getItem() instanceof CyborgPartItem part) {

				if(part.partName.contains("head") && assembler.head.isBlank()) {
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.head = part.partName;
					return ActionResult.SUCCESS;
				}

			}
		}
		return ActionResult.PASS;
	}
}
