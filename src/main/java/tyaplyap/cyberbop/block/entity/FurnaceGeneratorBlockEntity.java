package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.screen.FurnaceGeneratorScreenHandler;

import static net.minecraft.block.entity.AbstractFurnaceBlockEntity.createFuelTimeMap;

public class FurnaceGeneratorBlockEntity extends EnergyContainer {

	private int burnTime;

	private int fuelTime;

	private int capacity;

	private int storedEnergy;

	protected final PropertyDelegate propertyDelegate = new PropertyDelegate() {
		@Override
		public int get(int index) {
			switch (index) {
				case 0:
					return burnTime;
				case 1:
					return fuelTime;
				case 2:
					return capacity;
				case 3:
					return storedEnergy;
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
				case 2:
					capacity = value;
					break;
				case 3:
					storedEnergy = value;
					break;

			}
		}

		@Override
		public int size() {
			return 4;
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

	@Override
	public int capacity() {
		return 2400000;
	}

	@Override
	public int transferRate() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Type type() {
		return Type.GENERATOR;
	}

	public static void tick (World world, BlockPos pos, BlockState state, FurnaceGeneratorBlockEntity blockEntity) {
		EnergyBlockEntity.tick(world, pos, state, blockEntity);
		//blockEntity.capacity = (int)(68 * (MathHelper.clamp((float)blockEntity.getFreakEnergyStored() / blockEntity.capacity(), 0.0F, 1.0F)));
		blockEntity.capacity = blockEntity.capacity();
		blockEntity.storedEnergy = blockEntity.getFreakEnergyStored();
		if (blockEntity.isBurning()) {
			blockEntity.burnTime--;
			if (!blockEntity.isFull()) {
				blockEntity.setFreakEnergyStored(Math.min(blockEntity.capacity(), blockEntity.getFreakEnergyStored() + 256));
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
						}
					}
				}
			}
		}
		state = state.with(AbstractFurnaceBlock.LIT, blockEntity.isBurning());
		world.setBlockState(pos, state, Block.NOTIFY_ALL);
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
		return new FurnaceGeneratorScreenHandler(CyberbopMod.FURNACE_GENERATOR_SCREEN, syncId, playerInventory, this, propertyDelegate);
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
}
