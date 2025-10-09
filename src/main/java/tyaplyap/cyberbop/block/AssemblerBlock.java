package tyaplyap.cyberbop.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
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
		return world.isClient ? validateTicker(type, CyberbopBlockEntities.ASSEMBLER, AssemblerBlockEntity::clientTick) : null;
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


			if(stack.getItem() instanceof CyborgPartItem part) {//Head

				if(part.partName.contains("head") && assembler.getHead().isEmpty()) {
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.setStack(0, part.getDefaultStack());
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}

				else if(part.partName.contains("body") && assembler.getBody().isEmpty()) {//Body
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.setStack(1, part.getDefaultStack());
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}

			}
			if(stack.getItem() instanceof CyborgArmPartItem part) {//Right Arm
				if(assembler.getRightArm().isEmpty()) {
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.setStack(2, part.getDefaultStack());
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}
				else if(assembler.getLeftArm().isEmpty()) {//Left Arm
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.setStack(3, part.getDefaultStack());
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}
			}
			if(stack.getItem() instanceof CyborgLegPartItem part) {//Right Leg
				if(assembler.getRightLeg().isEmpty()) {
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.setStack(4, part.getDefaultStack());
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}
				else if(assembler.getLeftLeg().isEmpty()) {//Left Leg
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					assembler.setStack(5, part.getDefaultStack());
					assembler.updateListeners();
					return ActionResult.SUCCESS;
				}
			}


			if (player.isSneaking()) {

			if(stack.isEmpty() && player instanceof PlayerExtension cyborg) {
				if (!cyborg.isCyborg()) {
					if (assembler.isComplete()) {
						assembler.transferEnergy(cyborg.capacity(), cyborg);
						cyborg.setCyborg(true);
						//ex.setCyborgEnergy(10000);
						cyborg.setCyborgHead(((CyborgPartItem) assembler.getHead().getItem()).partName);
						cyborg.setCyborgBody(((CyborgPartItem) assembler.getBody().getItem()).partName);
						cyborg.setCyborgRightArm(((CyborgArmPartItem) assembler.getRightArm().getItem()).right);
						cyborg.setCyborgLeftArm(((CyborgArmPartItem) assembler.getLeftArm().getItem()).left);
						cyborg.setCyborgRightLeg(((CyborgLegPartItem) assembler.getRightLeg().getItem()).right);
						cyborg.setCyborgLeftLeg(((CyborgLegPartItem) assembler.getLeftLeg().getItem()).left);

						assembler.clearParts();

						if (assembler.module1 != null) cyborg.setModule1(assembler.module1);
						if (assembler.module2 != null) cyborg.setModule2(assembler.module2);
						if (assembler.module3 != null) cyborg.setModule3(assembler.module3);

//						assembler.head = "";
//						assembler.body = "";
//						assembler.rightArm = "";
//						assembler.leftArm = "";
//						assembler.rightLeg = "";
//						assembler.leftLeg = "";
//
//						assembler.module1 = "";
//						assembler.module2 = "";
//						assembler.module3 = "";
						assembler.updateListeners();

						player.teleport((ServerWorld) world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Set.of(), 180, 0);
						return ActionResult.SUCCESS;
					} else {
						if (assembler.getHead().isEmpty()) player.sendMessage(Text.literal("Head part is missing!"));
						if (assembler.getBody().isEmpty()) player.sendMessage(Text.literal("Body part is missing!"));
						if (assembler.getRightArm().isEmpty()) player.sendMessage(Text.literal("Right arm part is missing!"));
						if (assembler.getLeftArm().isEmpty()) player.sendMessage(Text.literal("Left arm part is missing!"));
						if (assembler.getRightLeg().isEmpty()) player.sendMessage(Text.literal("Right leg part is missing!"));
						if (assembler.getLeftLeg().isEmpty()) player.sendMessage(Text.literal("Left leg part is missing!"));
						return ActionResult.SUCCESS;
					}

				} else {
					if (!assembler.isComplete()) {
						if (!assembler.isFull()) {
							cyborg.transferEnergy(assembler.capacity(), assembler);
						}
						cyborg.setEnergyStored(0);
						cyborg.setCyborg(false);
						assembler.setStack(0, new ItemStack(CyberbopItems.partToItem(cyborg.getCyborgHead())));
						assembler.setStack(1, new ItemStack(CyberbopItems.partToItem(cyborg.getCyborgBody())));
						assembler.setStack(2, new ItemStack(CyberbopItems.partToItem(cyborg.getCyborgRightArm())));
						assembler.setStack(3, new ItemStack(CyberbopItems.partToItem(cyborg.getCyborgLeftArm())));
						assembler.setStack(4, new ItemStack(CyberbopItems.partToItem(cyborg.getCyborgRightLeg())));
						assembler.setStack(5, new ItemStack(CyberbopItems.partToItem(cyborg.getCyborgLeftLeg())));
//						assembler.head = ex.getCyborgHead();
//						assembler.body = ex.getCyborgBody();
//						assembler.rightArm = ex.getCyborgRightArm();
//						assembler.leftArm = ex.getCyborgLeftArm();
//						assembler.rightLeg = ex.getCyborgRightLeg();
//						assembler.leftLeg = ex.getCyborgLeftLeg();

						var moduleItem1 = CyberbopItems.getModule(cyborg.getModule1());
						if (moduleItem1 != null) moduleItem1.onModuleRemoved((ServerWorld) world, player);

						var moduleItem2 = CyberbopItems.getModule(cyborg.getModule2());
						if (moduleItem2 != null) moduleItem2.onModuleRemoved((ServerWorld) world, player);

						var moduleItem3 = CyberbopItems.getModule(cyborg.getModule3());
						if (moduleItem3 != null) moduleItem3.onModuleRemoved((ServerWorld) world, player);

						assembler.module1 = cyborg.getModule1();
						assembler.module2 = cyborg.getModule2();
						assembler.module3 = cyborg.getModule3();
						assembler.updateListeners();

						cyborg.setCyborgHead("");
						cyborg.setCyborgBody("");
						cyborg.setCyborgRightArm("");
						cyborg.setCyborgLeftArm("");
						cyborg.setCyborgRightLeg("");
						cyborg.setCyborgLeftLeg("");

						cyborg.setModule1("");
						cyborg.setModule2("");
						cyborg.setModule3("");

						return ActionResult.SUCCESS;
					}
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
