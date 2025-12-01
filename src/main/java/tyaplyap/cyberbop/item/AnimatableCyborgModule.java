package tyaplyap.cyberbop.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoItem;
import tyaplyap.cyberbop.client.render.module.AnimatableModuleRenderer;

public abstract class AnimatableCyborgModule extends BaseCyborgModuleItem implements GeoItem {
	@Environment(EnvType.CLIENT)
	private AnimatableModuleRenderer renderer;

	public AnimatableCyborgModule(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return super.use(world, user, hand);
	}

	@Environment(EnvType.CLIENT)
	public void setModuleRenderer(AnimatableModuleRenderer renderer) {
		this.renderer = renderer;
	}

	@Environment(EnvType.CLIENT)
	public AnimatableModuleRenderer getModuleRenderer() {
		return this.renderer;
	}
}
