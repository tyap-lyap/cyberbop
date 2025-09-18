package tyaplyap.cyberbop;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tyaplyap.cyberbop.block.CyberbopBlocks;
import tyaplyap.cyberbop.client.CyborgModel;
import tyaplyap.cyberbop.entity.FakePlayerEntity;

public class CyberbopMod implements ModInitializer {
	public static final String MOD_ID = "cyberbop";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final EntityModelLayer CYBORG_LAYER = new EntityModelLayer(
		id("cyborg"), "main");
	public static final EntityType<FakePlayerEntity> FAKE_PLAYER_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "fake_player"), FabricEntityTypeBuilder.<FakePlayerEntity>create(SpawnGroup.MISC,FakePlayerEntity::new).dimensions(EntityDimensions.changing(0.6F, 1.99F)).trackedUpdateRate(2).build());

	@Override
	public void onInitialize() {
		CyberbopBlocks.init();

		EntityModelLayerRegistry.registerModelLayer(CYBORG_LAYER, CyborgModel::getTexturedModelData);

		FabricDefaultAttributeRegistry.register(FAKE_PLAYER_ENTITY, LivingEntity.createLivingAttributes());
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
