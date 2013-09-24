package darvin939.SpleefArena.Commands;

import org.bukkit.entity.Player;

import darvin939.SpleefArena.SpleefArena;

public abstract class Handler {

	protected final SpleefArena plugin;

	public Handler(SpleefArena plugin) {
		this.plugin = plugin;
	}

	public abstract boolean perform(Player p, String[] args) throws InvalidUsage;

	protected boolean hasPermissions(Player p, String command, Boolean mess) {
		return plugin.hasPermissions(p, command, mess);
	}
	
	protected void getHelp(Player p, String command){
		plugin.getHelp(p, command);
	}
}
