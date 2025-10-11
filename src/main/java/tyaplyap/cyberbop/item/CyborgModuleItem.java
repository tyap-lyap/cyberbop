package tyaplyap.cyberbop.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;

public class CyborgModuleItem extends Item {

	public CyborgModuleItem(Settings settings) {
		super(settings);
	}

	public void tick(ServerWorld world, PlayerEntity player) {

	}

	public void onModuleRemoved(ServerWorld world, PlayerEntity player) {

	}
}
