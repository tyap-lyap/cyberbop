package tyaplyap.cyberbop.screen;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.block.entity.AssemblerBlockEntity;
import tyaplyap.cyberbop.packet.EnergyGuiUpdatePacket;
import tyaplyap.cyberbop.screen.slot.CyborgModuleSlot;
import tyaplyap.cyberbop.screen.slot.CyborgPartSlot;
import tyaplyap.cyberbop.util.CyborgPartType;
import tyaplyap.cyberbop.util.ImplInventory;

public class AssemblerScreenHandler extends ScreenHandler {

	private final Inventory inventory;
	private final ServerPlayerEntity serverPlayer;
	private AssemblerBlockEntity assemblerBlockEntity;
	public static final int[] MODULE_SLOTS = {6,7,8};

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
		this.addSlot(new CyborgPartSlot(inventory, 0, 80, 13, CyborgPartType.HEAD));
		this.addSlot(new CyborgPartSlot(inventory, 1, 80, 35,CyborgPartType.BODY));
		this.addSlot(new CyborgPartSlot(inventory, 2, 102, 25,CyborgPartType.RIGHT_ARM));
		this.addSlot(new CyborgPartSlot(inventory, 3, 58, 25,CyborgPartType.LEFT_ARM));
		this.addSlot(new CyborgPartSlot(inventory, 4, 96, 57,CyborgPartType.RIGHT_LEG));
		this.addSlot(new CyborgPartSlot(inventory, 5, 64, 57,CyborgPartType.LEFT_LEG));
		this.addSlot(new CyborgModuleSlot(inventory,6,8, 12, MODULE_SLOTS));
		this.addSlot(new CyborgModuleSlot(inventory,7,8, 30, MODULE_SLOTS));
		this.addSlot(new CyborgModuleSlot(inventory,8,8, 48, MODULE_SLOTS));


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
		ServerPlayNetworking.send(serverPlayer, new EnergyGuiUpdatePacket(assemblerBlockEntity.getEnergyStored(), assemblerBlockEntity.getCapacity()));
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
	public ItemStack quickMove(PlayerEntity player, int invSlot) {
		ItemStack newStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(invSlot);
		if (slot != null && slot.hasStack()) {
			ItemStack originalStack = slot.getStack();
			newStack = originalStack.copy();
			if (invSlot < this.inventory.size()) {
				if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
				return ItemStack.EMPTY;
			}

			if (originalStack.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
		}

		return newStack;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}
}
