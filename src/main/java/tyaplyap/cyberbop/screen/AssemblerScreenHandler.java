package tyaplyap.cyberbop.screen;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.block.entity.FurnaceGeneratorBlockEntity;
import tyaplyap.cyberbop.packet.EnergyGuiUpdatePacket;
import tyaplyap.cyberbop.screen.slot.BucketOutputSlot;
import tyaplyap.cyberbop.screen.slot.CyborgPartSlot;
import tyaplyap.cyberbop.screen.slot.FurnaceFuelSlot;
import tyaplyap.cyberbop.util.ImplInventory;

public class AssemblerScreenHandler extends ScreenHandler {

	private final Inventory inventory;
	private final ServerPlayerEntity serverPlayer;
	private AssemblerBlockEntity assemblerBlockEntity;

	public AssemblerScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
		this(CyberbopMod.FURNACE_GENERATOR_SCREEN, syncId, playerInventory, ImplInventory.ofSize(9), pos, null);
	}

	public AssemblerScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ImplInventory inventory, BlockPos pos, ServerPlayerEntity serverPlayer) {
		super(type, syncId);
		this.serverPlayer = serverPlayer;
		if (serverPlayer != null && !serverPlayer.getWorld().isClient) {
			this.assemblerBlockEntity = (AssemblerBlockEntity) playerInventory.player.getWorld().getBlockEntity(pos);
		}
		checkSize(inventory,9);
		this.inventory = inventory;
		this.addSlot(new CyborgPartSlot(inventory, 0, 80, 13,"head"));
		this.addSlot(new CyborgPartSlot(inventory, 1, 80, 35,"body"));
		this.addSlot(new CyborgPartSlot(inventory, 2, 102, 30,"right_arm"));
		this.addSlot(new CyborgPartSlot(inventory, 3, 58, 30,"left_arm"));
		this.addSlot(new CyborgPartSlot(inventory, 4, 96, 57,"right_leg"));
		this.addSlot(new CyborgPartSlot(inventory, 5, 64, 57,"left_leg"));
		this.addSlot(new Slot(inventory,6,8, 12));
		this.addSlot(new Slot(inventory,7,8, 30));
		this.addSlot(new Slot(inventory,8,8, 48));


		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}

	}

	@Override
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
		if (!player.getWorld().isClient) {
			assemblerBlockEntity.updateListeners();
		}
		super.onSlotClick(slotIndex, button, actionType, player);
	}
	@Override
	public void sendContentUpdates() {
		ServerPlayNetworking.send(serverPlayer, new EnergyGuiUpdatePacket(assemblerBlockEntity.getEnergyStored(), assemblerBlockEntity.capacity()));
		super.sendContentUpdates();
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
