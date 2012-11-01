package darvin939.SpleefArena.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import darvin939.SpleefArena.SpleefArena;
import darvin939.SpleefArena.Utils;

public class DestructionCommand {
	
	public DestructionCommand() {
	}

	public void execute(Player p, String[] args, SpleefArena plg) {
		if (args.length == 1) {
			if (plg.desc_plys.containsKey(p))
				plg.desc_plys.remove(p);
			if (plg.desc_tasks.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(plg.desc_tasks.get(p));
				plg.desc_tasks.remove(p);
			}
			Utils.msg(p, "Destruction mode disabled", 'p');

		} else if (args.length == 2) {
			Utils.msg(p, "&3/spl destruction &2[repeat] [radius]", 'u');
			Utils.msg(p, "&3Repeat: &21-99 &7(99 VERY slow!)&f, &3Radius: &20-9", 'u');
		} else if (args.length == 3 && (args[1].matches("[1-9]+[0-9]") || args[1].matches("[1-9]")) && args[2].matches("[0-9]")) {
			plg.repeat = Integer.valueOf(args[1]);
			plg.radius = Integer.valueOf(args[2]);
			plg.desc_plys.put(p, true);
			Utils.msg(p, "Destruction mode enabled &7(&3Repeat:&2" + Integer.valueOf(args[1]) + " &3Radius:&2" + Integer.valueOf(args[2]) + "&7)", 'p');
		} else {
			Utils.msg(p, "&3/spl destruction &2[repeat] [radius]", 'u');
			Utils.msg(p, "&3Repeat: &21-99 &7(99 VERY slow!)&f, &3Radius: &20-9", 'u');
		}
	}
}
