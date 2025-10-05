package tyaplyap.cyberbop;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tyaplyap.cyberbop.block.CyberbopBlocks;
import tyaplyap.cyberbop.block.entity.CyberbopBlockEntities;
import tyaplyap.cyberbop.block.entity.EnergyBlockEntity;
import tyaplyap.cyberbop.entity.FakePlayerEntity;
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.screen.FurnaceGeneratorScreenHandler;

import static net.minecraft.server.command.CommandManager.*;

public class CyberbopMod implements ModInitializer {
	public static final String MOD_ID = "cyberbop";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ScreenHandlerType<FurnaceGeneratorScreenHandler> FURNACE_GENERATOR_SCREEN = Registry.register(Registries.SCREEN_HANDLER, id("furnace_generator"), new ScreenHandlerType<>(FurnaceGeneratorScreenHandler::new, FeatureSet.empty()));

	public static final EntityType<FakePlayerEntity> FAKE_PLAYER_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "fake_player"), FabricEntityTypeBuilder.<FakePlayerEntity>create(SpawnGroup.MISC,FakePlayerEntity::new).dimensions(EntityDimensions.changing(0.6F, 1.99F)).trackedUpdateRate(2).build());

	public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
		.displayName(Text.translatable("itemGroup.cyberbop.items"))
		.entries((ctx, entries) -> {
			CyberbopItems.ITEMS.forEach((id, item) -> entries.add(item.getDefaultStack()));
			CyberbopBlocks.ITEMS.forEach((id, item) -> entries.add(item.getDefaultStack()));
		})
		.icon(CyberbopItems.ADVANCED_HEAD::getDefaultStack).build();

	@Override
	public void onInitialize() {

		Registry.register(Registries.ITEM_GROUP, CyberbopMod.id("items"), CyberbopMod.ITEM_GROUP);

		CyberbopBlocks.init();
		CyberbopItems.init();
		CyberbopBlockEntities.init();

		FabricDefaultAttributeRegistry.register(FAKE_PLAYER_ENTITY, LivingEntity.createLivingAttributes());

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("cyberbop").then(literal("spawncyborg").executes(commandContext -> {
			var player = commandContext.getSource().getPlayer();
			var cyborg = new FakePlayerEntity(FAKE_PLAYER_ENTITY, commandContext.getSource().getWorld(), player);
			cyborg.setPosition(commandContext.getSource().getPlayer().getPos());
			commandContext.getSource().getWorld().spawnEntity(cyborg);

			return 1;
			}))
			.then(literal("setstore").then(argument("energy", IntegerArgumentType.integer(0, Integer.MAX_VALUE)).executes(commandContext -> {
				if (commandContext.getSource().isExecutedByPlayer()) {
					HitResult hitResult = commandContext.getSource().getPlayer().raycast(commandContext.getSource().getPlayer().getBlockInteractionRange(), 0.0F, false);
					if (hitResult.getType() == HitResult.Type.BLOCK) {
						BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
						BlockEntity blockEntity = commandContext.getSource().getWorld().getBlockEntity(blockPos);
						if (blockEntity instanceof EnergyBlockEntity energyBlock) {
							energyBlock.setFreakEnergyStored(IntegerArgumentType.getInteger(commandContext, "energy"));
						}
					} else {
						commandContext.getSource().sendFeedback(() -> Text.literal("No Block"), false);

					}
					return 1;
				} else {
					commandContext.getSource().sendFeedback(() -> Text.literal("Source not Player"), false);
					return 0;
				}

			}))).then(literal("setstoreCyborg").then(argument("energy", IntegerArgumentType.integer(0, Integer.MAX_VALUE)).executes(commandContext -> {
				if (commandContext.getSource().isExecutedByPlayer()) {
					if (commandContext.getSource().getPlayer() instanceof PlayerExtension cyborg && cyborg.isCyborg()) {
						cyborg.setCyborgEnergy(IntegerArgumentType.getInteger(commandContext, "energy"));
					}
				} else {
					commandContext.getSource().sendFeedback(() -> Text.literal("Source not Player"), false);
					return 0;
				}
				return 0;
			}))).executes(context -> {
				context.getSource().sendFeedback(() -> Text.literal("Called /cyberbop with no arguments"), false);

				return 1;
			})));
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
