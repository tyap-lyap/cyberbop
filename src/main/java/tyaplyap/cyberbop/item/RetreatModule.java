package tyaplyap.cyberbop.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import tyaplyap.cyberbop.block.ControllerBlock;
import tyaplyap.cyberbop.extension.PlayerExtension;

//TODO данный модуль телепортирует игрока в точку ассемблера когда заряд падает до нуля
public class RetreatModule extends CyborgModuleItem {

	public RetreatModule(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension extension, ItemStack stack) {
		if (stack.get(CyberbopItems.RETREAT_MODULE_COMPONENT) != null && stack.get(CyberbopItems.RETREAT_MODULE_STATUS_COMPONENT) != null && player instanceof ServerPlayerEntity serverPlayer) {
			if (extension.getEnergyStored() <= 0) {
				GlobalPos globalPos = stack.get(CyberbopItems.RETREAT_MODULE_COMPONENT);
				ServerWorld serverWorld = serverPlayer.server.getWorld(globalPos.dimension());
				if (serverWorld != null) {
					BlockPos pos = globalPos.pos();
					BlockState blockState = serverWorld.getBlockState(pos);
					if (!stack.get(CyberbopItems.RETREAT_MODULE_STATUS_COMPONENT) && blockState.getBlock() instanceof ControllerBlock block) {
						serverPlayer.teleport(serverWorld, pos.getX() + 0.5, pos.getY()+1, pos.getZ() + 0.5,blockState.get(ControllerBlock.FACING).asRotation() ,0);

					}
				}
				stack.set(CyberbopItems.RETREAT_MODULE_STATUS_COMPONENT, true);
			} else if (stack.get(CyberbopItems.RETREAT_MODULE_STATUS_COMPONENT) != null && stack.get(CyberbopItems.RETREAT_MODULE_STATUS_COMPONENT)) {
				stack.set(CyberbopItems.RETREAT_MODULE_STATUS_COMPONENT, false);
			}
		}
	}

	@Override
	public void controllerLogic(ControllerBlock controllerBlock, BlockPos pos, World world, PlayerEntity player, ItemStack stack) {
		GlobalPos positionComponent = new GlobalPos(world.getRegistryKey(), pos);
		stack.set(CyberbopItems.RETREAT_MODULE_COMPONENT, positionComponent);
		stack.set(CyberbopItems.RETREAT_MODULE_STATUS_COMPONENT, false);
	}
}
