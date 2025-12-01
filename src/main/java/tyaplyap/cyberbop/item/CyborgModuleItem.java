package tyaplyap.cyberbop.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import tyaplyap.cyberbop.client.render.module.ModuleRenderer;

public abstract class CyborgModuleItem extends BaseCyborgModuleItem {
	@Environment(EnvType.CLIENT)
	private ModuleRenderer renderer;

	public CyborgModuleItem(Item.Settings settings) {
		super(settings);
	}

	@Environment(EnvType.CLIENT)
	public void setModuleRenderer(ModuleRenderer renderer) {
		this.renderer = renderer;
	}

	@Environment(EnvType.CLIENT)
	public ModuleRenderer getModuleRenderer() {
		return this.renderer;
	}
}
