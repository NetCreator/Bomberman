package io.github.mdsimmo.bomberman.commands.game;

import io.github.mdsimmo.bomberman.Bomberman;
import io.github.mdsimmo.bomberman.Game;
import io.github.mdsimmo.bomberman.commands.Command;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

public class Info extends Command {

	public Info(Command parent) {
		super(parent);
	}

	@Override
	public String description() {
		return "Show information about a game";
	}

	@Override
	public String name() {
		return "info";
	}

	@Override
	public Permission permission() {
		return Permission.OBSERVER;
	}

	@Override
	public List<String> options(CommandSender sender, List<String> args) {
		if (args.size() == 1)
			return Game.allGames();
		else
			return null;
	}

	@Override
	public boolean run(CommandSender sender, List<String> args) {
		if (args.size() != 1)
			return false;

		Game game = Game.findGame(args.get(0));
		if (game == null) {
			Bomberman.sendMessage(sender, "Game not found");
			return true;
		}
		Bomberman.sendHeading(sender, "Info: " + game.name);
		Map<String, String> list = new LinkedHashMap<>();
		if (game.isPlaying)
			list.put("Status", "In progress");
		else
			list.put("Status", "Waiting");
		list.put("Players ", "" + game.players.size());
		list.put("Min players", "" + game.getMinPlayers());
		list.put("Max players", "" + game.board.spawnPoints.size());
		list.put("Init bombs", "" + game.getBombs());
		list.put("Init lives", "" + game.getLives());
		list.put("Init power", "" + game.getPower());
		list.put("Autostart", "" + game.getAutostart());
		if (game.getFare() == null)
			list.put("Entry fare", "no fee");
		else
			list.put("Entry fare", game.getFare().getType() + " x"
					+ game.getFare().getAmount());
		if (game.getPot() == true && game.getFare() != null)
			list.put("Prize", "Pot currently at " + game.getFare().getAmount()
					* game.players.size() + " " + game.getFare().getType());
		else {
			if (game.getPrize() == null)
				list.put("Prize", "No prize");
			else
				list.put("Prize", game.getPrize().getAmount() + " "
						+ game.getPrize().getType());
		}
		list.put("Arena", game.board.name);
		Bomberman.sendMessage(sender, list);
		return true;
	}

	@Override
	public String usage(CommandSender sender) {
		return "/" + path() + "<game>";
	}

}
