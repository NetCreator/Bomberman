package io.github.mdsimmo.bomberman.commands.game.force;

import io.github.mdsimmo.bomberman.Bomberman;
import io.github.mdsimmo.bomberman.Game;
import io.github.mdsimmo.bomberman.commands.Command;

import java.util.List;

import org.bukkit.command.CommandSender;

public class Start extends Command {

	public Start(Command parent) {
		super(parent);
	}

	@Override
	public String name() {
		return "start";
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
		if (args.size() != 1) {
            return false;
        }
        Game game = Game.findGame(args.get(0));
        if (game == null)
            Bomberman.sendMessage(sender, "Game not found");
        else if (game.isPlaying)
            Bomberman.sendMessage(sender, "Game already started");
        else {
            if (game.startGame())
                Bomberman.sendMessage(sender, "Game starting");
            else
                Bomberman.sendMessage(sender, "There are not enough players");
        }
        return true;
	}

	@Override
	public String description() {
		return "Forcibly start a game";
	}

	@Override
	public String usage(CommandSender sender) {
		return "/" + path() + "<game>";
	}

	@Override
	public Permission permission() {
		return Permission.GAME_OPERATE;
	}

}
