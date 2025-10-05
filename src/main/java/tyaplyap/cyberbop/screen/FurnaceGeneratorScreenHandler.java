package tyaplyap.cyberbop.screen;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.FurnaceGeneratorBlockEntity;
import tyaplyap.cyberbop.screen.slot.BucketOutputSlot;
import tyaplyap.cyberbop.screen.slot.FurnaceFuelSlot;
import tyaplyap.cyberbop.util.ImplInventory;

public class FurnaceGeneratorScreenHandler extends ScreenHandler {

	private final Inventory inventory;

	private final PropertyDelegate propertyDelegate;

	public FurnaceGeneratorScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(CyberbopMod.FURNACE_GENERATOR_SCREEN, syncId, playerInventory, ImplInventory.ofSize(2), new ArrayPropertyDelegate(4));
	}

	public FurnaceGeneratorScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ImplInventory inventory, PropertyDelegate propertyDelegate) {
		super(type, syncId);
		checkSize(inventory,2);
		checkDataCount(propertyDelegate, 4);
		this.inventory = inventory;
		this.addSlot(new FurnaceFuelSlot( inventory, 0, 80, 17));
		this.addSlot(new BucketOutputSlot(inventory,1,80,53));


		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	this.propertyDelegate = propertyDelegate;
		this.addProperties(this.propertyDelegate);

	}


	public boolean isBurning() {
		return this.propertyDelegate.get(0) > 0;
	}

//	@Override
//	public ItemStack quickMove(PlayerEntity player, int invSlot) {
//		ItemStack newStack = ItemStack.EMPTY;
//		Slot slot = this.slots.get(invSlot);
//		if (slot != null && slot.hasStack()) {
//			ItemStack originalStack = slot.getStack();
//			newStack = originalStack.copy();
//			if (invSlot < this.inventory.size()) {
//					if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
//						return ItemStack.EMPTY;
//					}
//				} else if (!this.insertItem(originalStack, 1, this.inventory.size(), false)) {
//					return ItemStack.EMPTY;
//				}
//
//
//			if (originalStack.isEmpty()) {
//				slot.setStack(ItemStack.EMPTY);
//			} else {
//				slot.markDirty();
//			}
//		}
//		return newStack;
//	}


	public float getFuelProgress() {
		int i = this.propertyDelegate.get(1);
		if (i == 0) {
			i = 200;
		}

		return MathHelper.clamp((float)this.propertyDelegate.get(0) / i, 0.0F, 1.0F);
	}

	public float getEnergyHeight() {
		//System.out.println((float)this.propertyDelegate.get(2));
		//System.out.println((float)this.propertyDelegate.get(3));
		return (68 * (MathHelper.clamp((float)this.propertyDelegate.get(3) / this.propertyDelegate.get(2), 0.0F, 1.0F)));
		//return this.propertyDelegate.get(2); // MathHelper.clamp((float)this.propertyDelegate.get(3) / this.propertyDelegate.get(2), 0.0F, 1.0F);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot2 = this.slots.get(slot);
		if (slot2 != null && slot2.hasStack()) {
			ItemStack itemStack2 = slot2.getStack();
			itemStack = itemStack2.copy();
			if (slot == 1) {
				if (!this.insertItem(itemStack2, 3, 38, true)) {
					return ItemStack.EMPTY;
				}

				slot2.onQuickTransfer(itemStack2, itemStack);
			} else if (slot != 0) {
				if (AbstractFurnaceBlockEntity.canUseAsFuel(itemStack2)) {
					if (!this.insertItem(itemStack2, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slot >= 2 && slot < 29) {
					if (!this.insertItem(itemStack2, 30, 38, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slot >= 29 && slot < 38 && !this.insertItem(itemStack2, 3, 30, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 2, 38, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot2.setStack(ItemStack.EMPTY);
			} else {
				slot2.markDirty();
			}

			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot2.onTakeItem(player, itemStack2);
		}
		return itemStack;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}
}
