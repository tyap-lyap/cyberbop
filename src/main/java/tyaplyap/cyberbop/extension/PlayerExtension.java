package tyaplyap.cyberbop.extension;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tyaplyap.cyberbop.CyberbopMod;
import tyaplyap.cyberbop.item.CyborgPartItem;
import tyaplyap.cyberbop.util.transfer.EnergyStorage;
import tyaplyap.cyberbop.util.CyborgPartType;

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

	default void setCyborgPart(CyborgPartType partType, ItemStack stack) {
		switch (partType) {
			case HEAD -> setCyborgHead(stack);
			case BODY -> setCyborgBody(stack);
			case RIGHT_ARM -> setCyborgRightArm(stack);
			case LEFT_ARM -> setCyborgLeftArm(stack);
			case RIGHT_LEG -> setCyborgRightLeg(stack);
			case LEFT_LEG -> setCyborgLeftLeg(stack);
		}
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

	default void copyFrom(PlayerExtension old) {
		this.setCyborg(old.isCyborg());
		this.setEnergyStored(old.getEnergyStored());

		CyborgPartType.forEach(partType -> {
			this.setCyborgPart(partType, old.getCyborgPart(partType));
		});

		this.setModule1(old.getModule1());
		this.setModule2(old.getModule2());
		this.setModule3(old.getModule3());
	}

	default void setupAttributes(PlayerEntity player) {
		double maxHealth = 0;

		for(CyborgPartType partType : CyborgPartType.values()) {
			if (this.getCyborgPart(partType).getItem() instanceof CyborgPartItem partItem) {
				maxHealth = maxHealth + ((CyborgPartItem)this.getCyborgPart(partType).getItem()).getHealth();
			}
		}
		if(maxHealth > 0) {
			maxHealth = maxHealth - player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getBaseValue();

			player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(new EntityAttributeModifier(CyberbopMod.id("cyborg_health"), maxHealth, EntityAttributeModifier.Operation.ADD_VALUE));
		}
	}

//	Vec3d getAssemblePos();
//	void setAssemblePos(Vec3d pos);
}
