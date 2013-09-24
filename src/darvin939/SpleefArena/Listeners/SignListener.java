package darvin939.SpleefArena.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

import darvin939.SpleefArena.Config;
import darvin939.SpleefArena.SignConfig;
import darvin939.SpleefArena.SignData;
import darvin939.SpleefArena.SpleefArena;
import darvin939.SpleefArena.Utils.Util;

public class SignListener implements Listener {
	SpleefArena plg;
	Material mat = Material.SNOW_BLOCK;

	public SignListener(SpleefArena plg) {
		this.plg = plg;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignChange(SignChangeEvent event) {
		Player p = event.getPlayer();
		if (ChatColor.stripColor(event.getLine(0)).equalsIgnoreCase("[sp]")) {
			if (!plg.hasPermissions(p, "sign.create")) {
				event.setLine(0, "{sp}");
			} else {
				if (Util.getWorldEdit() != null) {
					WorldEditPlugin we = Util.getWorldEdit();
					setLine(event, 0, "&9[SpleefArena]");
					setLine(event, 1, Util.FCTU(p.getName()));
					if (we.getSelection(p) != null) {

						int id = Config.getSignCfg().getID();
						Selection sel = we.getSelection(p);
						Location l1 = sel.getMaximumPoint();
						Location l2 = sel.getMinimumPoint();

						setLine(event, 2, "ID: &6" + id);
						setLine(event, 3, "Mat: &6" + mat.name());

						SignData sd = new SignData(l1, l2);
						sd.setOwner(p);
						sd.setMaterial(mat);
						Util.PrintMSG(p, "arena_created", (int) l1.getBlockX() + "." + l1.getBlockZ() + ";" + l2.getBlockX() + "." + l2.getBlockZ() + ";" + sd.getY());

						SignConfig.signs.put(id, sd);
					} else {
						Util.PrintMSG(p, "region_notsel");
						setLine(event, 2, "Region not");
						setLine(event, 3, "selected!");
					}
				} else {
					Util.PrintMSG(p, "region_wenf");
					event.getBlock().breakNaturally();
				}

			}
		} else if ((ChatColor.stripColor(event.getLine(0)).equalsIgnoreCase("[SpleefArena]"))) {
			event.setLine(0, "{SpleefArena}");
			clearParamLines(event);
		}
	}

	public void clearParamLines(SignChangeEvent event) {
		event.setLine(1, "");
		event.setLine(2, "");
		event.setLine(3, "");
	}

	public void setLine(SignChangeEvent event, int n, String text) {
		event.setLine(n, ChatColor.translateAlternateColorCodes('&', text));
	}
}