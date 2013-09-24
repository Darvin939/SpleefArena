package darvin939.SpleefArena.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import darvin939.SpleefArena.Config;
import darvin939.SpleefArena.SpleefArena;

public class Util {

	public static void Print(Player p, String message) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	public static void PrintMSG(Player p, String message) {
		Print(p, Config.FGU.MSG(message));
	}

	public static void PrintPxMSG(Player p, String message) {
		Config.FGU.PrintPxMsg(p, Config.FGU.MSG(message));
	}

	public static void PrintPx(Player p, String message) {
		Config.FGU.PrintPxMsg(p, message);
	}

	public static void PrintMSG(Player p, String message, String keys) {
		Print(p, Config.FGU.MSG(message, keys));
	}

	public static void PrintSysPx(Player p, String message) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', SpleefArena.sysPrefix + message));
	}

	public static String FCTU(String s) {
		if (s != null && !s.isEmpty() && s.length() > 1)
			return s.substring(0, 1).toUpperCase() + s.substring(1);
		return s.toUpperCase();
	}

	public static String[] newArgs(String[] args) {
		String[] newArgs = new String[args.length - 1];
		System.arraycopy(args, 1, newArgs, 0, args.length - 1);
		return newArgs;
	}

	public static void unknownCmd(Player p, String s, String[] commands) {
		String handlerName = s.toLowerCase() + " ";
		String list = "";
		for (int i = 1; i < commands.length; i++) {
			list = list.isEmpty() ? "..." + handlerName + "&7<" + commands[i] : list + ", " + commands[i];
		}
		Config.FGU.PrintMsg(p, Config.FGU.MSG("cmd_unknown", SpleefArena.cmdPrefix + handlerName + commands[0]));
		Print(p, Config.FGU.MSG("hlp_commands") + " &2" + list + "&7>");
	}

	public static String signNameFormatter(Location l) {
		return new StringBuilder().append(l.getWorld().getName()).append(":").append(l.getBlockX()).append("&").append(l.getBlockY()).append("&").append(l.getBlockZ()).toString();
	}

	public static void unknownCmd(Player p, Class<?> c, String[] commands) {
		unknownCmd(p, c.getSimpleName(), commands);
	}

	public static boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static WorldEditPlugin getWorldEdit() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
			// log.warning(SpleefArena.prefix +
			// "WorldEdit not found on the server");
			return null;
		}
		return (WorldEditPlugin) plugin;
	}
}
