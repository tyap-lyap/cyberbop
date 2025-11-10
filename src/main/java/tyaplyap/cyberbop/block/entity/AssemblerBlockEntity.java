package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.item.CyberbopItems;
import tyaplyap.cyberbop.item.CyborgModuleItem;
import tyaplyap.cyberbop.item.CyborgPartItem;
import tyaplyap.cyberbop.screen.AssemblerScreenHandler;
import tyaplyap.cyberbop.util.transfer.BlockEnergyStorage;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;
import tyaplyap.cyberbop.util.CyborgPartType;

import java.util.Map;

public class AssemblerBlockEntity extends EnergyContainer {

	public int tickError;

	public boolean reverse;

	protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);

	@Override
	protected Text getContainerName() {
		return Text.translatable("container.cyberbop.assembler");
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new AssemblerScreenHandler(CyberbopMod.ASSEMBLER_SCREEN, syncId, playerInventory, this, pos, (ServerPlayerEntity) playerInventory.player);
	}

	@Override
	public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
		return pos;
	}

	@Override
	public IEnergyStorage.Type typeMachine() {
		return IEnergyStorage.Type.RECEIVER;
	}

	@Override
	public int getTransferRate() {
		return 64;
	}

	@Override
	public int getCapacity() {
		return 128000;
	}

	@Override
	boolean canInsertEnergy(EnergyStorage source, IEnergyStorage.Type sourceType) {
		return true;
	}

	@Override
	boolean canExtractEnergy(EnergyStorage target, IEnergyStorage.Type sourceType) {
		return target.type().equals(IEnergyStorage.Type.CYBORG);
	}

	@Override
	public void getDirectionsIO(Map<Direction, BlockEnergyStorage.TypeIO> directionMap) {
		for (var direction : Direction.values()) {
			if (!direction.equals(Direction.UP)) {
				directionMap.put(direction, BlockEnergyStorage.TypeIO.INPUT);
			}
		}
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return inventory;
	}

	public AssemblerBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.ASSEMBLER, pos, state);
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		Inventories.writeNbt(nbt, this.inventory, registryLookup);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory, registryLookup);
	}

	public boolean isComplete() {
		return !getHead().isEmpty() && isValid(getHead(), CyborgPartType.HEAD) && !getBody().isEmpty() && isValid(getBody(), CyborgPartType.BODY) && !getRightArm().isEmpty() && isValid(getRightArm(), CyborgPartType.RIGHT_ARM) && !getLeftArm().isEmpty() && isValid(getLeftArm(), CyborgPartType.LEFT_ARM) && !getRightLeg().isEmpty() && isValid(getRightLeg(), CyborgPartType.RIGHT_LEG) && !getLeftLeg().isEmpty()  && isValid(getLeftLeg(), CyborgPartType.LEFT_LEG);
	}

	public boolean containsModule(Item moduleStack) {
		return moduleStack instanceof CyborgModuleItem module && ((!getModule(1).isEmpty() && getModule(1).getItem().equals(module)) || (!getModule(2).isEmpty() && getModule(2).getItem().equals(module)) || (!getModule(3).isEmpty() && getModule(4).getItem().equals(module)));
	}

	public ItemStack getModule(int id) {
		return switch (id) {
			case 1 -> this.getItems().get(6);
			case 2 -> this.getItems().get(7);
			case 3 -> this.getItems().get(8);
			case 4 -> this.getItems().get(9);
			default -> ItemStack.EMPTY;
		};
	}

	public void setModule(int id, ItemStack module) {
			switch (id) {
				case 1 -> this.getItems().set(6, module);
				case 2 -> this.getItems().set(7, module);
				case 3 -> this.getItems().set(8, module);
				case 4 -> this.getItems().set(9, module);
			}
	}

	public void addModule(ItemStack module) {
		if(getModule(1).isEmpty()) setModule(1, module);
		else if(getModule(2).isEmpty()) setModule(2, module);
		else if(getModule(3).isEmpty()) setModule(3, module);
		else if(getModule(4).isEmpty()) setModule(4, module);
	}

	public boolean hasEmptyModuleSlot() {
		return (getModule(1).isEmpty()) || (getModule(2).isEmpty()) || (getModule(3).isEmpty() || (getModule(4).isEmpty()));
	}

	public boolean isValid(ItemStack itemStack, CyborgPartType partType) {
		if(itemStack.getItem() instanceof CyborgPartItem partItem) {
			return partItem.getPartName(partType) != null;
		}
		return false;
	}

	public ItemStack getHead() {return this.getItems().get(0);}

	public ItemStack getBody() {return this.getItems().get(1);}

	public ItemStack getRightArm() {return this.getItems().get(2);}

	public ItemStack getLeftArm() {return this.getItems().get(3);}

	public ItemStack getRightLeg() {return this.getItems().get(4);}

	public ItemStack getLeftLeg() {return this.getItems().get(5);}

	public ItemStack getPartStack(CyborgPartType partType) {
		switch (partType) {
			case HEAD -> {
				return getHead();
			}
			case BODY -> {
				return getBody();
			}
			case RIGHT_ARM -> {
				return getRightArm();
			}
			case LEFT_ARM -> {
				return getLeftArm();
			}
			case RIGHT_LEG -> {
				return getRightLeg();
			}
			case LEFT_LEG -> {
				return getLeftLeg();
			}
		}
		return ItemStack.EMPTY;
	}

	public void setPartStack(CyborgPartType partType, ItemStack stack) {
		int id = 0;
		switch (partType) {
			case HEAD -> id = 0;
			case BODY -> id = 1;
			case RIGHT_ARM -> id = 2;
			case LEFT_ARM -> id = 3;
			case RIGHT_LEG -> id = 4;
			case LEFT_LEG -> id = 5;
		}

		setStack(id, stack);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		NbtCompound nbtCompound = super.toInitialChunkDataNbt(registryLookup);
		this.writeNbt(nbtCompound, registryLookup);
		return nbtCompound;
	}

	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	public void updateListeners() {
		this.markDirty();
		this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
	}

	public static void animateError(World world, BlockPos pos, BlockState state, AssemblerBlockEntity blockEntity) {
		if (blockEntity.tickError <= 255 && !blockEntity.reverse) {
			blockEntity.tickError += 16;
		} else {
			blockEntity.reverse = true;
		}
		if (blockEntity.tickError > 60 && blockEntity.reverse) {
			blockEntity.tickError -= 16;
		} else {
			blockEntity.reverse = false;
		}
	}
}
