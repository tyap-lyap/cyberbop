package tyaplyap.cyberbop.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public class FurnaceGeneratorScreenHandler extends ScreenHandler {

	private final Inventory inventory;

	protected FurnaceGeneratorScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory) {
		this(type, syncId, playerInventory, new SimpleInventory(2), new ArrayPropertyDelegate(4));
	}

	public FurnaceGeneratorScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, SimpleInventory inventory, ArrayPropertyDelegate arrayPropertyDelegate) {
		super(type, syncId);
		checkSize(inventory,2);
		this.inventory = inventory;
		inventory.onOpen(playerInventory.player);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		return null;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}
}
