package tyaplyap.cyberbop.util.transfer;

import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.Map;

public abstract class BlockEnergyStorage extends EnergyStorage {
	public Map<Direction, TypeIO> directionIO = new HashMap<>();

	public BlockEnergyStorage() {
		for (var direction : Direction.values()) {
			directionIO.put(direction, TypeIO.NONE);
		}
		UpdateDirections();
	}

	public void UpdateDirections() {
		getDirectionIO(directionIO);
	}

	public abstract void getDirectionIO(Map<Direction, TypeIO> direction);

	public enum TypeIO {
		 INPUT,
		 OUTPUT,
		 IO,
		 NONE,
	 }
}
