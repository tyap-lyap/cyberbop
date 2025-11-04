package tyaplyap.cyberbop.item;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyaplyap.cyberbop.block.ControllerBlock;
import tyaplyap.cyberbop.extension.PlayerExtension;

public abstract class CyborgModuleItem extends Item {

	public CyborgModuleItem(Settings settings) {
		super(settings);
	}

	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension extension, ItemStack stack) {
	}

	public void clientTick(ClientWorld world, PlayerEntity player, PlayerExtension extension) {
	}

	public void onModuleRemoved(World world, PlayerEntity player) {

	}

	public  void controllerLogic(ControllerBlock controllerBlock, BlockPos pos, World world, PlayerEntity player, ItemStack itemStack) {

	}
}
