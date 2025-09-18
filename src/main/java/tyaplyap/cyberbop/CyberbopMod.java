package tyaplyap.cyberbop;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tyaplyap.cyberbop.entity.FakePlayerEntity;

public class CyberbopMod implements ModInitializer {
	public static final String MOD_ID = "cyberbop";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final EntityType<FakePlayerEntity> FAKE_PLAYER_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "fake_player"), FabricEntityTypeBuilder.<FakePlayerEntity>create(SpawnGroup.MISC,FakePlayerEntity::new).dimensions(EntityDimensions.changing(0.6F, 1.99F)).trackedUpdateRate(2).build());

	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(FAKE_PLAYER_ENTITY, LivingEntity.createLivingAttributes());
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
