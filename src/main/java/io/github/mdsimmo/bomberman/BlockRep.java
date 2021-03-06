package io.github.mdsimmo.bomberman;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BlockRep {
	
	protected Material material = Material.AIR;
	protected byte data = 0;
	protected ItemStack[] chestContents;
	
	public BlockRep() {
	}
	
	@SuppressWarnings("deprecation")
	public BlockRep(Block b) {
		material = b.getType();
		data = b.getData();
	}

	@SuppressWarnings("deprecation")
	public void setBlock(Block b) {
		/*if (material == Material.DIRT && Math.random() < 0.25) {
			for (int i = -1; i <= 1; i++) {
				Block relative = b.getRelative(0, i, 0);
				if (relative.getType() == Material.DIRT) {
					relative.setType(Material.AIR);
					b.getRelative(0, i, 0).setData((byte)0);
				}
			}
		} else {*/
			b.setType(material);
			b.setData(data);
		//}
	}
	
	public void save(SaveWriter sw) throws IOException {
		sw.writePart(material);
		sw.writePart(data);
	}

	public static BlockRep loadBlock(SaveReader sr) throws IOException {
		BlockRep rep = new BlockRep();
		rep.material = Material.valueOf(sr.readPart());
		rep.data = Byte.parseByte(sr.readPart(), 10);
		return rep;
	}
}
