package tyaplyap.cyberbop.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.client.util.ClientOreHighlightData;
import tyaplyap.cyberbop.extension.PlayerExtension;

import java.util.List;

public class XrayVisionModule extends CyborgModuleItem {
	// TODO апгрэйды для разных руд
	public static final TagKey<Block> XRAY_VISIBLE_ORE = TagKey.of(RegistryKeys.BLOCK, CyberbopMod.id("xray_visible_ore"));
	private static final int RADIUS = 8;
	static int alpha = 55;

	public XrayVisionModule(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension extension, ItemStack stack) {
		if(player.age % 100L == 0L && extension.getEnergyStored() > 0 && !player.isCreative() && !player.isSpectator()) {
			extension.setEnergyStored(Math.max(extension.getEnergyStored() - 500, 0));
		}
	}

	@Override
	public void clientTick(ClientWorld world, PlayerEntity player, PlayerExtension extension) {
		if(alpha > 0) alpha = alpha - 1;

		if(player.age % 100L == 0L) {
			if(extension.getEnergyStored() > 0) {
				highlightNearbyOres(world, player.getBlockPos());
			}
		}
		if(extension.getEnergyStored() <= 0 && !ClientOreHighlightData.isEmpty()) ClientOreHighlightData.clearHighlights();
	}

	public static float getAlpha() {
		return ((float)XrayVisionModule.alpha / (float)100);
	}

	@Override
	public void onModuleRemoved(World world, PlayerEntity player) {
		if(world.isClient()) {
			if(!ClientOreHighlightData.isEmpty()) ClientOreHighlightData.clearHighlights();
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Highlights nearby ores through").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("walls every 5 seconds.").formatted(Formatting.GRAY));
	}

	private void highlightNearbyOres(ClientWorld world, BlockPos center) {
		ClientOreHighlightData.clearHighlights();
		alpha = 55;

		for (int x = -RADIUS; x <= RADIUS; x++) {
			for (int y = -RADIUS; y <= RADIUS; y++) {
				for (int z = -RADIUS; z <= RADIUS; z++) {
					BlockPos pos = center.add(x, y, z);

					if (isVisibleOre(world.getBlockState(pos))) {
						ClientOreHighlightData.addHighlight(pos);
					}
				}
			}
		}
	}

	private boolean isVisibleOre(BlockState state) {
		return state.isIn(XRAY_VISIBLE_ORE);
	}
}
