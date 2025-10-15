package tyaplyap.cyberbop.extension;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.CyborgPartType;

import java.util.ArrayList;
import java.util.List;

public interface PlayerExtension {
	boolean isCyborg();
	void setCyborg(boolean isCyborg);

	void setCyborgHead(ItemStack head);
	void setCyborgBody(ItemStack body);
	void setCyborgRightArm(ItemStack rightArm);
	void setCyborgLeftArm(ItemStack leftArm);
	void setCyborgRightLeg(ItemStack rightLeg);
	void setCyborgLeftLeg(ItemStack leftLeg);

	ItemStack getCyborgHead();
	ItemStack getCyborgBody();
	ItemStack getCyborgRightArm();
	ItemStack getCyborgLeftArm();
	ItemStack getCyborgRightLeg();
	ItemStack getCyborgLeftLeg();

	default ItemStack getCyborgPart(CyborgPartType partType) {
		switch (partType) {
			case HEAD -> {
				return getCyborgHead();
			}
			case BODY -> {
				return getCyborgBody();
			}
			case RIGHT_ARM -> {
				return getCyborgRightArm();
			}
			case LEFT_ARM -> {
				return getCyborgLeftArm();
			}
			case RIGHT_LEG -> {
				return getCyborgRightLeg();
			}
			case LEFT_LEG -> {
				return getCyborgLeftLeg();
			}
		}
		return ItemStack.EMPTY;
	}

	default void clearAllParts() {
		this.setCyborgHead(ItemStack.EMPTY);
		this.setCyborgBody(ItemStack.EMPTY);
		this.setCyborgRightArm(ItemStack.EMPTY);
		this.setCyborgLeftArm(ItemStack.EMPTY);
		this.setCyborgRightLeg(ItemStack.EMPTY);
		this.setCyborgLeftLeg(ItemStack.EMPTY);
	}

	default ItemStack getModule(Item module) {
		for(ItemStack stack : getModules()) {
			if(stack.isOf(module)) return stack;
		}
		return null;
	}

	ItemStack getModule1();
	ItemStack getModule2();
	ItemStack getModule3();

	void setModule1(ItemStack module);
	void setModule2(ItemStack module);
	void setModule3(ItemStack module);

	List<ItemStack> getModules();

	boolean containsModule(Item module);

	void setEnergyStored(int cyborgEnergy);

	int getEnergyStored();

	int getCapacity();

	EnergyStorage getEnergyStorage();

//	Vec3d getAssemblePos();
//	void setAssemblePos(Vec3d pos);
}
