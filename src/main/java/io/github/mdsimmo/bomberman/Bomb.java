package io.github.mdsimmo.bomberman;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Bomb implements Runnable {

		private Plugin plugin = Bomberman.instance;
		private PlayerRep rep;
		private Block tnt;
		private Location spawn;
		private Game game;
		private int strength;
		private int eTaskId;
		
		public Bomb(Game game, PlayerRep rep, Block tnt) {
			this.game = game;
			this.rep = rep;
			this.tnt = tnt;
			strength = rep.bombStrength();
			spawn = tnt.getLocation();
			eTaskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 60);
			game.explosions.put(tnt, this);
		}
		
		@Override
		public void run() {
			new Explosion();
		}
				
		public class Explosion implements Listener, Runnable {

			public Explosion() {
				tnt.setType(Material.AIR);
				game.explosions.remove(tnt);
				plugin.getServer().getPluginManager().registerEvents(this, plugin);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 20);
				spawn.getWorld().playSound(spawn, Sound.EXPLODE, 1, (float) Math.random()+0.5f);
				createFire();
			}

			/**
			 * creates fire in the '+' pattern
			 */
			private void createFire() {
				createFire(0, 1);
				createFire(0, -1);
				createFire(1, 0);
				createFire(-1, 0);
			}
			
			/**
			 * creates a line of fire in the given x, z direction;
			 * @param x the unit to step in the x direction
			 * @param z the unit to step in the z direction
			 */
			private void createFire(int x, int z) {
				boolean stoppedAbove = false, stoppedBelow = false;
				for (int i = 0; i <= strength; i++) {
					if (!stoppedAbove)
						stoppedAbove = createFire(i*x, 1, i*z);
					if (!stoppedBelow)
						stoppedBelow = createFire(i*x, -1, i*z);
					if (createFire(i*x, 0, i*z)) {
						return;
					}
				}
			}

			/**
			 * creates fire at the given location if it can.
			 * Returns true if the fireball should stop
			 */
			private boolean createFire(int x, int y, int z) {
				Location l = spawn.clone().add(z, y, x);
				Block b = l.getBlock();
				
				// destroy dirt (or other blocks that can be blown up)
				if (game.getDestructables().contains(b.getType())) {
					new DeathBlock(b, rep);
					return true;
				}
				
				// create fire on non solid blocks
				if (!b.getType().isSolid()) {
					new DeathBlock(b, rep);
					return false;
				}
				
				// explode other tnts
				for (Block otherTnt : new HashSet<>(game.explosions.keySet())) {
					if (otherTnt.equals(b)) {
						Bomb other = game.explosions.get(otherTnt); 
						plugin.getServer().getScheduler().cancelTask(other.eTaskId);
						other.run();
						return true;
					}
				}
				// not solid so stop
				return true;
			}

			@Override
			public void run() {
				if (rep.isPlaying)
					rep.player.getInventory().addItem(new ItemStack(Material.TNT));
			}
			
		}

		public class DeathBlock implements Runnable{

			public PlayerRep cause;
			private Block block;
			private int duration = 20;
			private int dbTaskId;
			private Material original;
			
			public DeathBlock(Block block, PlayerRep cause) {
				this.block = block;
				this.cause = cause;
				
				original = block.getType();
				block.setType(Material.FIRE);
				dbTaskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 1);
				
				for (DeathBlock db : new ArrayList<>(game.deathBlocks))
					if (db.block.equals(block)) {
						// remove the old block
						plugin.getServer().getScheduler().cancelTask(db.dbTaskId);
						game.deathBlocks.remove(db);
					}
				game.deathBlocks.add(this);
			}

			@Override
			public void run() {
				for (PlayerRep rep : new ArrayList<PlayerRep>(game.players)) {
					if (touching(rep.player)) {
						rep.damage(this);
					}
				}
				if (--duration <= 0) {
					if (block.getType() == Material.FIRE)
						block.setType(Material.AIR);
						game.drop(block.getLocation(), original);
					plugin.getServer().getScheduler().cancelTask(dbTaskId);
					game.deathBlocks.remove(this);
				}
			}
			
			public boolean touching(Player player) {
				double margin = 0.295; // magical value that seems to be how far fire burns 
				Location l = player.getLocation();
				Location min = block.getLocation().add(0, -1, 0);
				Location max = block.getLocation().add(1, 2, 1);
				if (l.getX() >= min.getX()-margin
						&& l.getX() <= max.getX()+margin
						&& l.getY() >= min.getY()-margin
						&& l.getY() <= max.getY()+margin
						&& l.getZ() >= min.getZ()-margin
						&& l.getZ() <= max.getZ()+margin)
					return true;
				else 
					return false;
				
			}
		}
		
	}