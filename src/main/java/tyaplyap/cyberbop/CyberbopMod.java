package tyaplyap.cyberbop;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CyberbopMod implements ModInitializer {
	public static final String MOD_ID = "cyberbop";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
