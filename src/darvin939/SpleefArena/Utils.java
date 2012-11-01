package darvin939.SpleefArena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class Utils {
	public static WorldEditPlugin worldedit;

	public static boolean WEFound() {
		Plugin worldEdit = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if ((worldEdit != null) && ((worldEdit instanceof WorldEditPlugin))) {
			worldedit = ((WorldEditPlugin) worldEdit);
			return true;
		}
		return false;
	}

	public static void msg(Player p, String message, char type) {
		//worldedit.getSelection(null).getRegionSelector().getRegion()
		String rMessage = "";
		switch (type) {
		case 'e':
			rMessage = "&c[Error]&f " + message;
			break;
		case 'u':
			rMessage = "[Usage] " + message;
			break;
		case 'p':
			rMessage = "&b[SP]&f " + message;
			break;
		case '/':
			rMessage = message;
		}
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', rMessage));
	}

	public static boolean hasPerm(Player p, String perm, Boolean message) {
		if (!p.hasPermission(perm)) {
			if (message)
				msg(p, "You don't have the Permissions to do that!", 'p');
			return false;
		}
		return true;
	}

	public static String FCTU(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	public static String[] newArgs(String[] args) {
		String[] newArgs = new String[args.length - 1];
		System.arraycopy(args, 1, newArgs, 0, args.length - 1);
		return newArgs;
	}

}
