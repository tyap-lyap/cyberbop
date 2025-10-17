package tyaplyap.cyberbop;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
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
import tyaplyap.cyberbop.extension.PlayerExtension;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.packet.UseJetpackPacket;
import tyaplyap.cyberbop.packet.DebugCablePacket;
import tyaplyap.cyberbop.screen.AssemblerScreenHandler;
import tyaplyap.cyberbop.screen.FurnaceGeneratorScreenHandler;
import tyaplyap.cyberbop.packet.EnergyGuiUpdatePacket;

import static net.minecraft.server.command.CommandManager.*;

public class CyberbopMod implements ModInitializer {
	public static final String MOD_ID = "cyberbop";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ScreenHandlerType<FurnaceGeneratorScreenHandler> FURNACE_GENERATOR_SCREEN = Registry.register(Registries.SCREEN_HANDLER, id("furnace_generator"), new ExtendedScreenHandlerType<>(FurnaceGeneratorScreenHandler::new, BlockPos.PACKET_CODEC));
	public static final ScreenHandlerType<AssemblerScreenHandler> ASSEMBLER_SCREEN = Registry.register(Registries.SCREEN_HANDLER, id("assembler"), new ExtendedScreenHandlerType<>(AssemblerScreenHandler::new, BlockPos.PACKET_CODEC));

//	public static final EntityType<FakePlayerEntity> FAKE_PLAYER_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "fake_player"), FabricEntityTypeBuilder.<FakePlayerEntity>create(SpawnGroup.MISC,FakePlayerEntity::new).dimensions(EntityDimensions.changing(0.6F, 1.99F)).trackedUpdateRate(2).build());

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.playS2C().register(EnergyGuiUpdatePacket.ID, EnergyGuiUpdatePacket.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(DebugCablePacket.ID, DebugCablePacket.PACKET_CODEC);

		CyberbopBlocks.init();
		CyberbopItems.init();
		CyberbopBlockEntities.init();

		UseJetpackPacket.registerC2SPackets();
		UseJetpackPacket.registerS2CPackets();
		UseJetpackPacket.registerServerReceivers();

//		FabricDefaultAttributeRegistry.register(FAKE_PLAYER_ENTITY, LivingEntity.createLivingAttributes());

		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			if(alive) {
				((PlayerExtension)newPlayer).copyFrom((PlayerExtension) oldPlayer);
				((PlayerExtension)newPlayer).setupAttributes(newPlayer);
			}
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
			dispatcher.register(literal("cyberbop").requires(source -> source.hasPermissionLevel(4)).then(literal("setstore").then(argument("energy", IntegerArgumentType.integer(0, Integer.MAX_VALUE)).executes(commandContext -> {
				if (commandContext.getSource().isExecutedByPlayer()) {
					HitResult hitResult = commandContext.getSource().getPlayer().raycast(commandContext.getSource().getPlayer().getBlockInteractionRange(), 0.0F, false);
					if (hitResult.getType() == HitResult.Type.BLOCK) {
						BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
						BlockEntity blockEntity = commandContext.getSource().getWorld().getBlockEntity(blockPos);
						if (blockEntity instanceof EnergyBlockEntity energyBlock) {
							energyBlock.setEnergyStored(IntegerArgumentType.getInteger(commandContext, "energy"));
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
						cyborg.setEnergyStored(IntegerArgumentType.getInteger(commandContext, "energy"));
					}
				} else {
					commandContext.getSource().sendFeedback(() -> Text.literal("Source not Player"), false);
					return 0;
				}
				return 0;
			})))));
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
