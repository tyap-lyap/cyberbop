package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.*;

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

			if(stack.getItem() instanceof CyborgModuleItem moduleItem) {
				if(assembler.hasEmptyModuleSlot() && !assembler.containsModule(moduleItem.getModuleName())) {
					assembler.addModule(moduleItem.getModuleName());
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}
			}

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
					if(assembler.isComplete()) {
						ex.setCyborg(true);
						ex.setCyborgEnergy(10000);
						ex.setCyborgHead(assembler.head);
						ex.setCyborgBody(assembler.body);
						ex.setCyborgRightArm(assembler.rightArm);
						ex.setCyborgLeftArm(assembler.leftArm);
						ex.setCyborgRightLeg(assembler.rightLeg);
						ex.setCyborgLeftLeg(assembler.leftLeg);

						if(assembler.module1 != null) ex.setModule1(assembler.module1);
						if(assembler.module2 != null) ex.setModule2(assembler.module2);
						if(assembler.module3 != null) ex.setModule3(assembler.module3);

						assembler.head = "";
						assembler.body = "";
						assembler.rightArm = "";
						assembler.leftArm = "";
						assembler.rightLeg = "";
						assembler.leftLeg = "";

						assembler.module1 = "";
						assembler.module2 = "";
						assembler.module3 = "";
						assembler.updateListeners();

						player.teleport((ServerWorld) world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Set.of(), 180, 0);
						return ActionResult.SUCCESS;
					}
					else {
						if(assembler.head.isBlank()) player.sendMessage(Text.literal("Head part is missing!"));
						if(assembler.body.isBlank()) player.sendMessage(Text.literal("Body part is missing!"));
						if(assembler.rightArm.isBlank()) player.sendMessage(Text.literal("Right arm part is missing!"));
						if(assembler.leftArm.isBlank()) player.sendMessage(Text.literal("Left arm part is missing!"));
						if(assembler.rightLeg.isBlank()) player.sendMessage(Text.literal("Right leg part is missing!"));
						if(assembler.leftLeg.isBlank()) player.sendMessage(Text.literal("Left leg part is missing!"));
						return ActionResult.SUCCESS;
					}

				}
				else {
					if(assembler.isEmpty()) {
						ex.setCyborg(false);
						assembler.head = ex.getCyborgHead();
						assembler.body = ex.getCyborgBody();
						assembler.rightArm = ex.getCyborgRightArm();
						assembler.leftArm = ex.getCyborgLeftArm();
						assembler.rightLeg = ex.getCyborgRightLeg();
						assembler.leftLeg = ex.getCyborgLeftLeg();

						var moduleItem1 = CyberbopItems.getModule(ex.getModule1());
						if(moduleItem1 != null) moduleItem1.onModuleRemoved((ServerWorld)world, player);

						var moduleItem2 = CyberbopItems.getModule(ex.getModule2());
						if(moduleItem2 != null) moduleItem2.onModuleRemoved((ServerWorld)world, player);

						var moduleItem3 = CyberbopItems.getModule(ex.getModule3());
						if(moduleItem3 != null) moduleItem3.onModuleRemoved((ServerWorld)world, player);

						assembler.module1 = ex.getModule1();
						assembler.module2 = ex.getModule2();
						assembler.module3 = ex.getModule3();
						assembler.updateListeners();

						ex.setCyborgHead("");
						ex.setCyborgBody("");
						ex.setCyborgRightArm("");
						ex.setCyborgLeftArm("");
						ex.setCyborgRightLeg("");
						ex.setCyborgLeftLeg("");

						ex.setModule1("");
						ex.setModule2("");
						ex.setModule3("");

						return ActionResult.SUCCESS;
					}

				}
			}
		}
		return ActionResult.PASS;
	}


	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if(!world.isClient() && world.getBlockEntity(pos) instanceof AssemblerBlockEntity assembler) {
			if(!assembler.head.isBlank()) Block.dropStack(world, pos, CyberbopItems.partToItem(assembler.head).getDefaultStack());
			if(!assembler.body.isBlank()) Block.dropStack(world, pos, CyberbopItems.partToItem(assembler.body).getDefaultStack());
			if(!assembler.rightArm.isBlank()) Block.dropStack(world, pos, CyberbopItems.partToItem(assembler.rightArm).getDefaultStack());
			if(!assembler.leftArm.isBlank()) Block.dropStack(world, pos, CyberbopItems.partToItem(assembler.leftArm).getDefaultStack());
			if(!assembler.rightLeg.isBlank()) Block.dropStack(world, pos, CyberbopItems.partToItem(assembler.rightLeg).getDefaultStack());
			if(!assembler.leftLeg.isBlank()) Block.dropStack(world, pos, CyberbopItems.partToItem(assembler.leftLeg).getDefaultStack());
		}

		return super.onBreak(world, pos, state, player);
	}
}
