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
import net.minecraft.server.network.ServerPlayerEntity;
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
import tyaplyap.cyberbop.util.CyborgPartType;

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

			if (player.isSneaking()) {
				if (stack.isEmpty() && player instanceof PlayerExtension cyborg) {
					if (!cyborg.isCyborg()) {
						if (assembler.isComplete()) {
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
								else ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), assembler.getModule(1));

							if (!assembler.getModule(2).isEmpty())
								if (assembler.getModule(2).getItem() instanceof CyborgModuleItem)
									cyborg.setModule2(assembler.getModule(2));
								else ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), assembler.getModule(2));

							if (!assembler.getModule(3).isEmpty())
								if (assembler.getModule(3).getItem() instanceof CyborgModuleItem)
									cyborg.setModule3(assembler.getModule(3));
								else ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), assembler.getModule(3));

							assembler.getItems().clear();

							assembler.transferEnergy(cyborg.capacity(), cyborg);

							assembler.updateListeners();

							player.teleport((ServerWorld) world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Set.of(), 180, 0);
							return ActionResult.SUCCESS;
						} else {
							if (assembler.getHead().isEmpty())
								player.sendMessage(Text.literal("Head part is missing!"));
							if (assembler.getBody().isEmpty())
								player.sendMessage(Text.literal("Body part is missing!"));
							if (assembler.getRightArm().isEmpty())
								player.sendMessage(Text.literal("Right arm part is missing!"));
							if (assembler.getLeftArm().isEmpty())
								player.sendMessage(Text.literal("Left arm part is missing!"));
							if (assembler.getRightLeg().isEmpty())
								player.sendMessage(Text.literal("Right leg part is missing!"));
							if (assembler.getLeftLeg().isEmpty())
								player.sendMessage(Text.literal("Left leg part is missing!"));
							return ActionResult.SUCCESS;
						}
					} else {
						if (assembler.isEmpty()) {
							if (!assembler.isFull()) {
								cyborg.transferEnergy(assembler.capacity(), assembler);
							}
							cyborg.setEnergyStored(0);
							cyborg.setCyborg(false);
							assembler.setStack(0, cyborg.getCyborgHead());
							assembler.setStack(1, cyborg.getCyborgBody());
							assembler.setStack(2, cyborg.getCyborgRightArm());
							assembler.setStack(3, cyborg.getCyborgLeftArm());
							assembler.setStack(4, cyborg.getCyborgRightLeg());
							assembler.setStack(5, cyborg.getCyborgLeftLeg());

							if (cyborg.getModule1().getItem() instanceof CyborgModuleItem moduleItem) moduleItem.onModuleRemoved((ServerWorld) world, player);
							if (cyborg.getModule2().getItem() instanceof CyborgModuleItem moduleItem) moduleItem.onModuleRemoved((ServerWorld) world, player);
							if (cyborg.getModule3().getItem() instanceof CyborgModuleItem moduleItem) moduleItem.onModuleRemoved((ServerWorld) world, player);

							assembler.setModule(1, cyborg.getModule1());
							assembler.setModule(2, cyborg.getModule2());
							assembler.setModule(3, cyborg.getModule3());

							cyborg.clearAllParts();

							cyborg.setModule1(ItemStack.EMPTY);
							cyborg.setModule2(ItemStack.EMPTY);
							cyborg.setModule3(ItemStack.EMPTY);
							assembler.updateListeners();

						} else {
							((ServerPlayerEntity) player).sendMessageToClient(Text.of("Assembler have items!"), false);
						}
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
