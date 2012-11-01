package darvin939.SpleefArena.Commands;

import org.bukkit.entity.Player;

import darvin939.SpleefArena.SpleefArena;
import darvin939.SpleefArena.Utils;

public class HelpCommand {

	public HelpCommand() {
	}

	public void execute(Player p, String[] args, SpleefArena plg) {
		Utils.msg(p, "&b---------------- &2SpleefArena Help&b ---------------------", '/');
		Utils.msg(p, "&3/spl update &f- Check for updates", '/');
		Utils.msg(p, "&3/spl destruction &2[repeat] [radius] &f- Enable the destruction of the surface", '/');
		Utils.msg(p, "&3/spl strike &2[playername/random] {damage}", '/');
		Utils.msg(p, "&3/spl help &f- Show this help topic", '/');
		Utils.msg(p, "&b-----------------------------------------------------", '/');
	}
}
