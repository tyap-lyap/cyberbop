package tyaplyap.cyberbop.compat;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.api.thirst.ThirstHelper;
import tyaplyap.cyberbop.extension.PlayerExtension;

public class ToughAsNailsCyberbop {
	public static boolean initialized = false;

	public static void init() {
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			if(!ToughAsNailsCyberbop.initialized) {
				TemperatureHelper.registerPlayerTemperatureModifier((player, temperatureLevel) -> {
					if(player instanceof PlayerExtension ex) {
						if(ex.isCyborg()) return TemperatureLevel.NEUTRAL;
					}
					return temperatureLevel;
				});
				initialized = true;
			}
		});

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			server.getPlayerManager().getPlayerList().forEach(player -> {
				if(player instanceof PlayerExtension ex && ThirstHelper.getThirst(player) instanceof ThirstDataExtension tde) {
					tde.setCyborg(ex.isCyborg());
				}
			});
		});
	}
}
