package tyaplyap.cyberbop.item;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import tyaplyap.cyberbop.extension.PlayerExtension;

public class CyborgModuleItem extends Item {

	public CyborgModuleItem(Settings settings) {
		super(settings);
	}

	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension extension) {
	}

	public void clientTick(ClientWorld world, PlayerEntity player, PlayerExtension extension) {
	}

	public void onModuleRemoved(World world, PlayerEntity player) {

	}
}
