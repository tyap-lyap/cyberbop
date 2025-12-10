package tyaplyap.cyberbop.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import software.bernie.geckolib.GeckoLibConstants;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.AnimatableIdCache;
import tyaplyap.cyberbop.client.render.module.AnimatableModuleRenderer;

public abstract class AnimatableCyborgModule extends BaseCyborgModuleItem implements GeoItem {
	@Environment(EnvType.CLIENT)
	private AnimatableModuleRenderer renderer;

	public AnimatableCyborgModule(Settings settings) {
		super(settings);
	}


	public static long getOrAssignIdUpdate(ItemStack stack, ServerWorld level) {
		if (!(stack.getComponents() instanceof ComponentMapImpl components))
			return Long.MAX_VALUE;

		Long id = components.get(GeckoLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get());

		if (id == null)
			components.set(GeckoLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get(), id = AnimatableIdCache.getFreeId(level));


		return id;
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
