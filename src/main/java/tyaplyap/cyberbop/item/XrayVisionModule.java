package tyaplyap.cyberbop.item;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyaplyap.cyberbop.client.util.ClientOreHighlightData;
import tyaplyap.cyberbop.extension.PlayerExtension;

import java.util.List;

public class XrayVisionModule extends CyborgModuleItem {
	private static final int RADIUS = 10;

	public XrayVisionModule(Settings settings) {
		super(settings);
	}

	@Override
	public void tick(ServerWorld world, PlayerEntity player, PlayerExtension extension) {
		if(player.age % 100L == 0L && extension.getEnergyStored() > 0 && !player.isCreative() && !player.isSpectator()) {
			extension.setEnergyStored(Math.max(extension.getEnergyStored() - 50, 0));
		}
	}

	@Override
	public void clientTick(ClientWorld world, PlayerEntity player, PlayerExtension extension) {
		if(player.age % 100L == 0L) {
			if(extension.getEnergyStored() > 0) {
				highlightNearbyOres(world, player.getBlockPos());
			}
		}
		if(extension.getEnergyStored() <= 0 && !ClientOreHighlightData.isEmpty()) ClientOreHighlightData.clearHighlights();
	}

	@Override
	public void onModuleRemoved(World world, PlayerEntity player) {
		if(world.isClient()) {
			if(!ClientOreHighlightData.isEmpty()) ClientOreHighlightData.clearHighlights();
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Highlights nearby ores thought").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("walls every 5 seconds.").formatted(Formatting.GRAY));
	}

	private void highlightNearbyOres(ClientWorld world, BlockPos center) {
		ClientOreHighlightData.clearHighlights();

		for (int x = -RADIUS; x <= RADIUS; x++) {
			for (int y = -RADIUS; y <= RADIUS; y++) {
				for (int z = -RADIUS; z <= RADIUS; z++) {
					BlockPos pos = center.add(x, y, z);

					if (isOreBlock(world.getBlockState(pos))) {
						ClientOreHighlightData.addHighlight(pos);
					}
				}
			}
		}
	}

	private boolean isOreBlock(BlockState state) {
		return state.isIn(ConventionalBlockTags.ORES);
	}
}
