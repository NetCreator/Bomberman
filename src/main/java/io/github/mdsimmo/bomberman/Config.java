package io.github.mdsimmo.bomberman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

@SuppressWarnings("unchecked")
public enum Config {

	FARE ("stake.fare", null),
	PRIZE ("stake.prize", new ItemStack(Material.DIAMOND, 3)),
	POT ("stake.pot", false),
	LIVES ("lives", 3),
	BOMBS ( "bombs", 3),
	POWER ( "power", 3),
	MIN_PLAYERS ( "minplayers", 2),
	AUTOSTART ( "autostart.enabled", false ),
	AUTOSTART_DELAY ( "autostart.delay", 30 ),
	DEFAULT_ARENA ("defaultarena", "default" ),
	DROPS_ITEMS ("drops.items", Arrays.asList(
			new ItemStack(Material.TNT, 3),
			new ItemStack(Material.BLAZE_POWDER, 2),
			new Potion(PotionType.INSTANT_HEAL, 1).toItemStack(1),
			new Potion(PotionType.SPEED, 2).toItemStack(1))),
	DROPS_CHANCE ("drops.chance", 0.1d),
	BLOCKS_DROPPING ("blocks.drop", Arrays.asList(
			Material.DIRT), Material.class),
	BLOCKS_DESTRUCTABLE ("blocks.destructable", Arrays.asList(
			Material.DIRT), Material.class),
	SUDDEN_DEATH ("timeout.suddendeath", 60*5),
	TIME_OUT ("timeout.gameover", 60*8),
	PROTECT_EXPLOSIONS ("griefprotection.explosion", false),
	PROTECT_PLACING ("griefprotection.blockplacing", false),
	PROTECT_DESTROYING ("griefprotection.blockdestroy", false),
	PROTECT_FIRE ("griefprotection.fire", false),
	INITIAL_ITEMS("initialitems", Arrays.asList(
			new ItemStack(Material.TNT, 3),
			new ItemStack(Material.BLAZE_POWDER, 3)));
	
	private static Plugin plugin = Bomberman.instance;
	private static FileConfiguration c = plugin.getConfig();
	
	private final String path;
	private final Object value;
	
	Config(String path, Object standard) {
		this(path, standard, Object.class);
	}
	
	Config (String path, Object standard, Object clazzList) {
		this.path = path;
		FileConfiguration config = Bomberman.instance.getConfig();
		if (clazzList == Material.class) {
			config.addDefault(path, MaterialToString((List<Material>) standard));
			value = StringtoMaterial((List<String>) config.get(path));
		} else {
			config.addDefault(path, standard);
			value = config.get(path);
		}
	}
	
	static {
		c.options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	public <T> T getValue() {
		return (T) value;
	}
	
	public <T> T getValue(FileConfiguration config) {
		if (config != null && config.contains(path))
			return (T) config.get(path);
		else
			return (T) value;
	}
	
	public String getPath() {
		return path;
	}
	
	private static List<String> MaterialToString(List<Material> materials) {
		List<String> converted = new ArrayList<>();
		for (Material m : materials)
			converted.add(m.toString());
		return converted;
	}
	
	private static List<Material> StringtoMaterial(List<String> strings) {
		List<Material> materials = new ArrayList<>();
		for (String s : strings) {
			materials.add(Material.valueOf(s));
		}
		return materials;
	}
}