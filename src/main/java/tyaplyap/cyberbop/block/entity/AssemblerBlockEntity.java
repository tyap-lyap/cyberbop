package tyaplyap.cyberbop.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AssemblerBlockEntity extends BlockEntity {

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

	public static void tick(World world, BlockPos pos, BlockState state, AssemblerBlockEntity blockEntity) {

	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);

		nbt.putString("head", this.head);
		nbt.putString("rightArm", this.rightArm);
		nbt.putString("leftArm", this.leftArm);
		nbt.putString("body", this.body);
		nbt.putString("rightLeg", this.rightLeg);
		nbt.putString("leftLeg", this.leftLeg);

		nbt.putString("module1", this.module1);
		nbt.putString("module2", this.module2);
		nbt.putString("module3", this.module3);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);

		this.head = nbt.getString("head");
		this.rightArm = nbt.getString("rightArm");
		this.leftArm = nbt.getString("leftArm");
		this.body = nbt.getString("body");
		this.rightLeg = nbt.getString("rightLeg");
		this.leftLeg = nbt.getString("leftLeg");

		this.module1 = nbt.getString("module1");
		this.module2 = nbt.getString("module2");
		this.module3 = nbt.getString("module3");
	}

	public boolean isEmpty() {
		return head.isBlank() && body.isBlank() && rightArm.isBlank() && leftArm.isBlank() && rightLeg.isBlank() && leftLeg.isBlank();
	}

	public boolean isComplete() {
		return !head.isBlank() && !body.isBlank() && !rightArm.isBlank() && !leftArm.isBlank() && !rightLeg.isBlank() && !leftLeg.isBlank();
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
