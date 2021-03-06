package io.github.mdsimmo.bomberman.commands.arena;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.mdsimmo.bomberman.Board;
import io.github.mdsimmo.bomberman.BoardGenerator;
import io.github.mdsimmo.bomberman.Bomberman;
import io.github.mdsimmo.bomberman.commands.Command;

public class Create extends Command {

	public Create(Command parent) {
		super(parent);
	}

	@Override
	public String name() {
		return "create";
	}

	@Override
	public List<String> options(CommandSender sender, List<String> args) {
		if (args.size() == 1)
			return BoardGenerator.allBoards();
		else
			return null;
	}

	@Override
	public boolean run(CommandSender sender, List<String> args) {
		if (args.size() != 1)
            return false;
        if (sender instanceof Player) {
            Location[] locations = BoardGenerator.getBoundingStructure((Player)sender, args.get(0));
            Board board2 = BoardGenerator.createArena(args.get(0), locations[0], locations[1]);
            BoardGenerator.saveBoard(board2);
            Bomberman.sendMessage(sender, "Arena created");
        }
        return true;
	}

	@Override
	public String description() {
		return "Create a new arena type for games to use";
	}

	@Override
	public String usage(CommandSender sender) {
		return "/" + path() + "<arena> (look at the arena when using)" ;
	}

	@Override
	public Permission permission() {
		return Permission.ARENA_EDITING;
	}

}
