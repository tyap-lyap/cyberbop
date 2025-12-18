package tyaplyap.cyberbop.item;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.ControllerBlock;

import java.util.List;

public class ExtendedHealthModule extends CyborgModuleItem {

	public ExtendedHealthModule(Settings settings) {
		super(settings);
	}

	@Override
	public void controllerLogic(ControllerBlock controllerBlock, BlockPos pos, World world, PlayerEntity player, ItemStack itemStack) {
		player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(new EntityAttributeModifier(CyberbopMod.id("cyborg_health_module"), 20, EntityAttributeModifier.Operation.ADD_VALUE));
	}

	@Override
	public void onModuleRemoved(World world, PlayerEntity player) {
		player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).removeModifier(CyberbopMod.id("cyborg_health_module"));
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Grants §a20.0 §7health, this extra health").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("takes extra energy to regenerate.").formatted(Formatting.GRAY));
	}
}
