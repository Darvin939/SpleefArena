package darvin939.SpleefArena;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class BlockTask {
	public static void execute(Player p, SpleefArena plg) {

		if (plg.strike_plys.containsKey(p)) {
			plg.strike_plys.remove(p);
			return;
		}

		if (plg.desc_plys.containsKey(p)) {
			if (plg.desc_tasks.containsKey(p)) {
				Bukkit.getScheduler().cancelTask(plg.desc_tasks.get(p));
				plg.desc_tasks.remove(p);
			}
			if (plg.desc_plys.get(p))
				plg.destruction(p, plg.repeat, plg.radius);
			plg.area.remove(p);
			return;
		}
		plg.replaceArena(Material.SNOW_BLOCK);
		Utils.msg(p, "The surface restored", 'p');
	}
}
