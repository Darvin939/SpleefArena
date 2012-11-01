package darvin939.SpleefArena.Commands;

import java.lang.reflect.Method;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import darvin939.SpleefArena.SpleefArena;
import darvin939.SpleefArena.Utils;

public class Commands implements CommandExecutor {
	SpleefArena plg;
	String px;

	public Commands(SpleefArena plg) {
		this.plg = plg;
	}

	public Object execute(String[] args, Player p) {
		String cmdpath = this.getClass().getName().replaceFirst(".Commands", "");
		try {
			if (Utils.hasPerm(p, "spleefarena." + args[0], true)) {
				if (args.length > 0) {
					Class<?> clazz = Class.forName(cmdpath + "." + Utils.FCTU(args[0] + "Command"));
					Object instance = clazz.newInstance();
					Method method = null;
					method = clazz.getDeclaredMethod("execute", Player.class, String[].class, SpleefArena.class);
					return method.invoke(instance, p, args, plg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Utils.msg(p, "Error occurred when trying the command /" + args[0], 'e');
		}
		return null;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Player p = (Player) sender;
		if (args.length > 0) {
			for (String s : plg.cmds) {
				if (args[0].equalsIgnoreCase(s)) {
					execute(args, p);
					return true;
				}
			}
			Utils.msg(p, "Unkown command", 'e');
			return true;
		} else
			Utils.msg(p, "Type &3/spl help &f to show help topic", 'e');
		return true;
	}
}