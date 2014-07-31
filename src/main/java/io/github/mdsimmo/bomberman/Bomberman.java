package io.github.mdsimmo.bomberman;

import org.bukkit.plugin.java.JavaPlugin;

public class Bomberman extends JavaPlugin {
	
	public static Bomberman instance;
	
	/* TODO FEATURES
	 * make chests (and other like things) spawn with contents
	 * more styles and underground styles
	*/
	
	/* TODO BUGS
	 */
	
	@Override
	public void onEnable() {
		instance = this;
		new Config();
		new GameCommander();
		Game.loadGames();
		CustomEntityType.registerEntities();
	}
	
	@Override
	public void onDisable() {
		for (String game : Game.allGames()) {
			Game.findGame(game).terminate();
			Game.findGame(game).saveGame();
		}
		CustomEntityType.unregisterEntities();
	}
}
