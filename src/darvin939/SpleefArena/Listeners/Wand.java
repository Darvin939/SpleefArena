package darvin939.SpleefArena.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import darvin939.SpleefArena.Config;
import darvin939.SpleefArena.Config.Nodes;
import darvin939.SpleefArena.SignConfig;
import darvin939.SpleefArena.SpleefArena;

public class Wand implements Listener {

	private SpleefArena plg;

	public Wand(SpleefArena plugin) {
		plg = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		int wand = Nodes.wand_item.getInteger();
		Player p = event.getPlayer();
		System.out.println("test");
		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && p.getItemInHand().getTypeId() == wand && plg.hasPermissions(p, "wand")) {
			Block b = event.getClickedBlock();
			if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN) {
				BlockState state = b.getState();
				if ((state instanceof Sign)) {
					Sign sign = (Sign) state;
					if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[SpleefArena]")) {
						String line2 = ChatColor.stripColor(sign.getLine(2));
						if (line2.startsWith("ID: ")) {
							try {
								String id = line2.replace("ID: ", "");
								Config.getSignCfg().getAreaInfo(p, SignConfig.signs.get(Integer.parseInt(id)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}
