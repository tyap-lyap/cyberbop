package tyaplyap.cyberbop.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyaplyap.cyberbop.block.entity.EnergyBlockEntity;

public class DebugUtil {
	public static void updateEnergyDebug (World world, BlockPos pos, BlockState state, EnergyBlockEntity energyBlock) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			energyBlock.updateListeners();
		}
	}
}
