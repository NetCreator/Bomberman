package io.github.mdsimmo.bomberman;

import net.minecraft.server.v1_7_R3.Entity;
import net.minecraft.server.v1_7_R3.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

public class QuickHack implements Listener {

	private Plugin plugin = Bomberman.instance;
	
	public QuickHack() {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onCreeperSpawn(CreatureSpawnEvent e) {
		if (e.getEntityType() == EntityType.CREEPER) {
			plugin.getLogger().info("Spawning skeleton");
			e.setCancelled(true);
			Location l = e.getLocation();
			CraftWorld cw = (CraftWorld) l.getWorld();
			WorldServer ws = cw.getHandle();
			Entity skeleton = new CustomEntitySkeleton(ws);
			skeleton.setLocation(l.getX(), l.getY(), l.getZ(), l.getPitch(), l.getYaw());
			ws.addEntity(skeleton);
			plugin.getLogger().info("Successful add");
		}
	}
	
}
