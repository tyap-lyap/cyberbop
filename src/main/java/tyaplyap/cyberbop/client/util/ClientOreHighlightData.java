package tyaplyap.cyberbop.client.util;

import net.minecraft.util.math.BlockPos;

import java.util.*;

public class ClientOreHighlightData {
	private static final ArrayList<BlockPos> HIGHLIGHTED_BLOCKS = new ArrayList<>();

	public static void addHighlight(BlockPos pos) {
		HIGHLIGHTED_BLOCKS.add(pos);
	}

	public static void removeHighlight(BlockPos pos) {
		HIGHLIGHTED_BLOCKS.remove(pos);
	}

	public static boolean isEmpty() {
		return HIGHLIGHTED_BLOCKS.isEmpty();
	}

	public static void clearHighlights() {
		HIGHLIGHTED_BLOCKS.clear();
	}

	public static ArrayList<BlockPos> getHighlightedBlocks() {
		return HIGHLIGHTED_BLOCKS;
	}
}
