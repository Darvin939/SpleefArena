package darvin939.SpleefArena.Commands.Handlers;

import org.bukkit.entity.Player;

import darvin939.SpleefArena.SpleefArena;
import darvin939.SpleefArena.Commands.Handler;
import darvin939.SpleefArena.Commands.InvalidUsage;
import darvin939.SpleefArena.Utils.Util;

public class Help extends Handler {

	public Help(SpleefArena plugin) {
		super(plugin);
	}

	@Override
	public boolean perform(Player p, String[] args) throws InvalidUsage {
		String[] cmds = plugin.Commands.getCommands();
		Util.Print(p, "&b=================== &2SpleefArena &b===================");
		for (String cmd : cmds) {
			Util.Print(p, "&6/spl " + cmd + " &f: " + plugin.Commands.getHelp(cmd));
		}
		Util.Print(p, "&7For more help of command type &6/dd &4<command> &6help");
		return true;
	}

}
