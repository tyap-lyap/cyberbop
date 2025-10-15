package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.screen.FurnaceGeneratorScreenHandler;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.transfer.IEnergyStorage;

import static net.minecraft.block.entity.AbstractFurnaceBlockEntity.createFuelTimeMap;

public class FurnaceGeneratorBlockEntity extends EnergyContainer {

	private int burnTime;

	private int fuelTime;

	protected final PropertyDelegate propertyDelegate = new PropertyDelegate() {
		@Override
		public int get(int index) {
			switch (index) {
				case 0:
					return burnTime;
				case 1:
					return fuelTime;
				default:
					return 0;
			}
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
				case 0:
					burnTime = value;
					break;
				case 1:
					fuelTime = value;
					break;
			}
		}

		@Override
		public int size() {
			return 2;
		}
	};

	public FurnaceGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(CyberbopBlockEntities.FURNACE_GENERATOR, blockPos, blockState);
	}

	protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

	@Override
	protected Text getContainerName() {
		return Text.translatable("container.cyberbop.furnace_generator");
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return inventory;
	}


	public static void tick (World world, BlockPos pos, BlockState state, FurnaceGeneratorBlockEntity blockEntity) {
		if (blockEntity.isBurning()) {
			blockEntity.burnTime--;
			if (!blockEntity.isFull()) {
				blockEntity.setEnergyStored(Math.min(blockEntity.getCapacity(), blockEntity.getEnergyStored() + 256));
			}
		}

		ItemStack inputFuel = blockEntity.inventory.get(0);
		ItemStack outputFuel = blockEntity.inventory.get(1);
		if (!blockEntity.isBurning() && !blockEntity.isFull()) {
			blockEntity.burnTime = blockEntity.getFuelTime(inputFuel);
			blockEntity.fuelTime = blockEntity.burnTime;
			if (blockEntity.isBurning()) {
				blockEntity.markDirty();

				if (!inputFuel.isEmpty()) {
					Item outputFuelItem = inputFuel.getItem().getRecipeRemainder();

					inputFuel.decrement(1);

					if (outputFuelItem != null) {

						if (outputFuel.isEmpty()) {
							blockEntity.inventory.set(1, new ItemStack(outputFuelItem));
						} else if (outputFuel.getItem() == outputFuelItem && outputFuel.getCount() < outputFuel.getMaxCount()) {
							outputFuel.increment(1);
						} else if (inputFuel.isEmpty()) {
							blockEntity.inventory.set(0, new ItemStack(outputFuelItem));
						} else {
							ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(outputFuelItem));
						}
					}
				}
			}
		}

		state = state.with(AbstractFurnaceBlock.LIT, blockEntity.isBurning());
		world.setBlockState(pos, state, Block.NOTIFY_ALL);
		EnergyBlockEntity.BatteryTick(world, pos, state, blockEntity);
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return new int[]{0,1};
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return slot == 1;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return slot == 0;
	}

	protected int getFuelTime(ItemStack fuel) {
		if (fuel.isEmpty()) {
			return 0;
		} else {
			Item item = fuel.getItem();
			return createFuelTimeMap().getOrDefault(item, 0)/10;
		}
	}

	public boolean isBurning() {
		return burnTime > 0;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new FurnaceGeneratorScreenHandler(CyberbopMod.FURNACE_GENERATOR_SCREEN, syncId, playerInventory, this, propertyDelegate, pos, (ServerPlayerEntity) playerInventory.player);
	}

	@Override
	public IEnergyStorage.Type typeMachine() {
		return IEnergyStorage.Type.GENERATOR;
	}

	@Override
	public int getTransferRate() {
		return 90;
	}

	@Override
	public int getCapacity() {
		return 2400000;
	}

	@Override
	boolean canInsertEnergy(EnergyStorage source) {
		return true;
	}

	@Override
	boolean canExtractEnergy(EnergyStorage target) {
		return false;
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		Inventories.writeNbt(nbt, this.inventory, registryLookup);
		nbt.putShort("BurnTime", (short) this.burnTime);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory, registryLookup);
		this.burnTime = nbt.getShort("BurnTime");
	}

//	@Override
//	public Packet<ClientPlayPacketListener> toUpdatePacket() {
//		return BlockEntityUpdateS2CPacket.create(this);
//	}

//	@Override
//	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
//		NbtCompound nbtCompound = super.toInitialChunkDataNbt(registryLookup);
//		this.writeNbt(nbtCompound, registryLookup);
//		nbtCompound.putShort("BurnTime", (short) this.burnTime);
//		nbtCompound.putInt("EnergyStored", this.getFreakEnergyStored());
//		return nbtCompound;
//	}

	@Override
	public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
		return pos;
	}
}
