package darvin939.SpleefArena.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import darvin939.SpleefArena.SpleefArena;
import darvin939.SpleefArena.Utils;

public class SPPlayerListener implements Listener {
	SpleefArena plg;

	public SPPlayerListener(SpleefArena plg) {
		this.plg = plg;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		plg.players = plg.getServer().getOnlinePlayers();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		plg.players = plg.getServer().getOnlinePlayers();
		if (p.hasPermission("spleefarena.update")) {
			try {
				plg.getNewVersion();
				if (plg.v_new > plg.v_curr) {
					Utils.msg(p, "SpleefArena &2v" + plg.des.getVersion() + " &fis outdated!", '/');
					Utils.msg(p, "Please download new version &7(&2v" + plg.v_new + "&7)&f from:", '/');
					Utils.msg(p, " &3" + plg.v_url.replace("version_new.txt", "SpleefArena.jar"), '/');
				}
			} catch (Exception e) {
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.hasBlock())
			if (event.getClickedBlock().getType() == Material.LEVER || event.getClickedBlock().getType() == Material.STONE_BUTTON || event.getClickedBlock().getType() == Material.STONE_PLATE || event.getClickedBlock().getType() == Material.WOOD_PLATE) {
				plg.RedstoneEventPlayer = event.getPlayer();
			}
	}
}
