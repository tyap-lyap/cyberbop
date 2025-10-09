package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.item.CyborgArmPartItem;
import tyaplyap.cyberbop.item.CyborgLegPartItem;
import tyaplyap.cyberbop.item.CyborgPartItem;
import tyaplyap.cyberbop.screen.AssemblerScreenHandler;

import java.util.Map;

public class AssemblerBlockEntity extends EnergyContainer {

	public int tickError;

	public boolean reverse;

	protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);

	@Override
	protected Text getContainerName() {
		return Text.translatable("block.cyberbop.assembler");
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
	public int capacity() {
		return 6900000;
	}


	@Override
	public int energyConsumption() {
		return 100;
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return inventory;
	}

	public Map<String,ItemStack> getParts () {
		return null;
	}

	@Override
	public Type type() {
		return Type.RECEIVER;
	}

	public String head = "";
	public String rightArm = "";
	public String leftArm = "";
	public String body = "";
	public String rightLeg = "";
	public String leftLeg = "";

	public String module1 = "";
	public String module2 = "";
	public String module3 = "";

	public AssemblerBlockEntity(BlockPos pos, BlockState state) {
		super(CyberbopBlockEntities.ASSEMBLER, pos, state);
	}

	public static void clientTick(World world, BlockPos pos, BlockState state, AssemblerBlockEntity blockEntity) {
		if (blockEntity.tickError <= 255 && !blockEntity.reverse) {
			blockEntity.tickError += 32;
		} else {
			blockEntity.reverse = true;
		}
		if (blockEntity.tickError > 60 && blockEntity.reverse) {
			blockEntity.tickError -= 32;
		} else {
			blockEntity.reverse = false;
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		Inventories.writeNbt(nbt, this.inventory, registryLookup);
//
//		nbt.putString("head", this.head);
//		nbt.putString("rightArm", this.rightArm);
//		nbt.putString("leftArm", this.leftArm);
//		nbt.putString("body", this.body);
//		nbt.putString("rightLeg", this.rightLeg);
//		nbt.putString("leftLeg", this.leftLeg);

		nbt.putString("module1", this.module1);
		nbt.putString("module2", this.module2);
		nbt.putString("module3", this.module3);
	}
	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory, registryLookup);

//		this.head = nbt.getString("head");
//		this.rightArm = nbt.getString("rightArm");
//		this.leftArm = nbt.getString("leftArm");
//		this.body = nbt.getString("body");
//		this.rightLeg = nbt.getString("rightLeg");
//		this.leftLeg = nbt.getString("leftLeg");

		this.module1 = nbt.getString("module1");
		this.module2 = nbt.getString("module2");
		this.module3 = nbt.getString("module3");
	}

	public boolean isEmpty() {
		return head.isBlank() && body.isBlank() && rightArm.isBlank() && leftArm.isBlank() && rightLeg.isBlank() && leftLeg.isBlank();
	}

	public boolean isComplete() {
		return !getHead().isEmpty() && isValid(getHead(), "head") && !getBody().isEmpty() && isValid(getBody(), "body") && !getRightArm().isEmpty() && isValid(getRightArm(), "right_arm") && !getLeftArm().isEmpty() && isValid(getLeftArm(), "left_arm") && !getRightLeg().isEmpty() && isValid(getRightLeg(), "right_leg") && !getLeftLeg().isEmpty()  && isValid(getLeftLeg(), "left_leg");
	}

	public boolean containsModule(String module) {
		return ((module1 != null && module1.equals(module)) || (module2 != null && module2.equals(module)) || (module3 != null && module3.equals(module)));
	}

	public void addModule(String module) {
		if(module1 == null || module1.isBlank()) module1 = module;
		else if(module2 == null || module2.isBlank()) module2 = module;
		else if(module3 == null || module3.isBlank()) module3 = module;
	}

	public boolean hasEmptyModuleSlot() {
		return (module1 == null || module1.isBlank()) || (module2 == null || module2.isBlank()) || (module3 == null || module3.isBlank());
	}

	public boolean isValid(ItemStack itemStack, String part) {
		return switch (itemStack.getItem()) {
			case CyborgPartItem item when part.equals("head") && item.partName.contains(part) -> true;
			case CyborgPartItem item when part.equals("body") && item.partName.contains(part) -> true;
			case CyborgArmPartItem item when part.equals("right_arm") && item.right.contains(part) -> true;
			case CyborgArmPartItem item when part.equals("left_arm") && item.left.contains(part) -> true;
			case CyborgLegPartItem item when part.equals("right_leg") && item.right.contains(part) -> true;
			case CyborgLegPartItem item when part.equals("left_leg") && item.left.contains(part) -> true;
			case null, default -> false;
		};
	}

	public ItemStack getHead() {return this.getItems().get(0);}

	public ItemStack getBody() {return this.getItems().get(1);}

	public ItemStack getRightArm() {return this.getItems().get(2);}

	public ItemStack getLeftArm() {return this.getItems().get(3);}

	public ItemStack getRightLeg() {return this.getItems().get(4);}

	public ItemStack getLeftLeg() {return this.getItems().get(5);}

	public void clearParts() {
		for (int id = 0; id <= 5; ++id) {
			System.out.println(id);
			this.setStack(id, ItemStack.EMPTY);
		}
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
}
