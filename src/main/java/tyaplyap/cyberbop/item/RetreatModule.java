package tyaplyap.cyberbop.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import tyaplyap.cyberbop.extension.PlayerExtension;

//TODO данный модуль телепортирует игрока в точку ассемблера когда заряд падает до нуля
public class RetreatModule extends CyborgModuleItem {

	public RetreatModule(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension extension) {

		if(extension.getEnergyStored() <= 0) {

		}
	}
}
