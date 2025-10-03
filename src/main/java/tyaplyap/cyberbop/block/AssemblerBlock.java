package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyborgArmPartItem;
import tyaplyap.cyberbop.item.CyborgLegPartItem;
import tyaplyap.cyberbop.item.CyborgPartItem;

import java.util.Set;

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
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}

				else if(part.partName.contains("body") && assembler.body.isBlank()) {
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.body = part.partName;
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}

			}
			if(stack.getItem() instanceof CyborgArmPartItem part) {
				if(assembler.rightArm.isBlank()) {
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.rightArm = part.right;
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}
				else if(assembler.leftArm.isBlank()) {
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.leftArm = part.left;
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}
			}
			if(stack.getItem() instanceof CyborgLegPartItem part) {
				if(assembler.rightLeg.isBlank()) {
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.rightLeg = part.right;
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}
				else if(assembler.leftLeg.isBlank()) {
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.leftLeg = part.left;
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}
			}

			if(stack.isEmpty() && player instanceof PlayerExtension ex) {
				if(!ex.isCyborg()) {
					ex.setCyborg(true);
					ex.setCyborgEnergy(ex.getCyborgMaxEnergy());
					ex.setCyborgHead(assembler.head);
					ex.setCyborgBody(assembler.body);
					ex.setCyborgRightArm(assembler.rightArm);
					ex.setCyborgLeftArm(assembler.leftArm);
					ex.setCyborgRightLeg(assembler.rightLeg);
					ex.setCyborgLeftLeg(assembler.leftLeg);

					assembler.head = "";
					assembler.body = "";
					assembler.rightArm = "";
					assembler.leftArm = "";
					assembler.rightLeg = "";
					assembler.leftLeg = "";
					assembler.updateListeners();

					player.teleport((ServerWorld) world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Set.of(), 180, 0);
				}
				else {
					ex.setCyborg(false);
					assembler.head = ex.geCyborgHead();
					assembler.body = ex.getCyborgBody();
					assembler.rightArm = ex.getCyborgRightArm();
					assembler.leftArm = ex.getCyborgLeftArm();
					assembler.rightLeg = ex.getCyborgRightLeg();
					assembler.leftLeg = ex.getCyborgLeftLeg();
					assembler.updateListeners();

					ex.setCyborgHead("");
					ex.setCyborgBody("");
					ex.setCyborgRightArm("");
					ex.setCyborgLeftArm("");
					ex.setCyborgRightLeg("");
					ex.setCyborgLeftLeg("");
				}
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}
}
