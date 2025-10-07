package tyaplyap.cyberbop.screen;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.FurnaceGeneratorBlockEntity;
import tyaplyap.cyberbop.screen.slot.BucketOutputSlot;
import tyaplyap.cyberbop.screen.slot.FurnaceFuelSlot;
import tyaplyap.cyberbop.packet.EnergyGuiUpdatePacket;
import tyaplyap.cyberbop.util.ImplInventory;

public class FurnaceGeneratorScreenHandler extends ScreenHandler {

	private final Inventory inventory;
	private final PropertyDelegate propertyDelegate;
	private final ServerPlayerEntity serverPlayer;
	private FurnaceGeneratorBlockEntity furnaceGeneratorBlock;
	public FurnaceGeneratorScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
		this(CyberbopMod.FURNACE_GENERATOR_SCREEN, syncId, playerInventory, ImplInventory.ofSize(2), new ArrayPropertyDelegate(4), pos, null);
	}

	public FurnaceGeneratorScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ImplInventory inventory, PropertyDelegate propertyDelegate, BlockPos pos, ServerPlayerEntity serverPlayer) {
		super(type, syncId);
		this.serverPlayer = serverPlayer;
		if (serverPlayer != null && !serverPlayer.getWorld().isClient) {
			this.furnaceGeneratorBlock = (FurnaceGeneratorBlockEntity) playerInventory.player.getWorld().getBlockEntity(pos);
		}
		checkSize(inventory,2);
		checkDataCount(propertyDelegate, 2);
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

	@Override
	public void sendContentUpdates() {
		ServerPlayNetworking.send(serverPlayer, new EnergyGuiUpdatePacket(furnaceGeneratorBlock.getEnergyStored(), furnaceGeneratorBlock.capacity()));
		super.sendContentUpdates();
	}

	public float getFuelProgress() {
		int i = this.propertyDelegate.get(1);
		if (i == 0) {
			i = 200;
		}

		return MathHelper.clamp((float)this.propertyDelegate.get(0) / i, 0.0F, 1.0F);
	}

	@Override
	public void onClosed(PlayerEntity player) {
		if (player instanceof ServerPlayerEntity serverPlayer) {
		ServerPlayNetworking.send(serverPlayer, new EnergyGuiUpdatePacket(0, 0));
		}
		super.onClosed(player);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot2 = this.slots.get(slot);
		if (slot2.hasStack()) {
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
