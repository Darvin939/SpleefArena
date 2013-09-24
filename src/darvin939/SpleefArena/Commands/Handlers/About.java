package darvin939.SpleefArena.Commands.Handlers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import darvin939.SpleefArena.SpleefArena;
import darvin939.SpleefArena.Commands.Handler;
import darvin939.SpleefArena.Commands.InvalidUsage;
import darvin939.SpleefArena.Utils.Util;

public class About extends Handler {
	public About(SpleefArena plugin) {
		super(plugin);
	}

	@Override
	public boolean perform(Player p, String[] args) throws InvalidUsage {
		PluginDescriptionFile des = plugin.getDescription();
		Util.Print(p, "&2&l&oPlugin " + des.getName() + " v" + des.getVersion());
		Util.Print(p, "&6Author: &7Darvin939 (Sergey Mashoshin. Russia, Moscow)");
		Util.Print(p, "&6Contact Email:&7 darvin212@gmail.com");
		return true;
	}
}