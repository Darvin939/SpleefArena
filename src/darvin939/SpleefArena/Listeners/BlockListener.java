package darvin939.SpleefArena.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import darvin939.SpleefArena.Config;
import darvin939.SpleefArena.SignConfig;
import darvin939.SpleefArena.SignData;
import darvin939.SpleefArena.SpleefArena;
import darvin939.SpleefArena.Utils.Util;

public class BlockListener implements Listener {

	// private SpleefArena plg;

	public BlockListener(SpleefArena plugin) {
		// this.plg = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		Player p = event.getPlayer();
		if ((b.getType() == Material.SIGN_POST) || (b.getType() == Material.WALL_SIGN)) {
			BlockState state = b.getState();
			if ((state instanceof Sign)) {
				Sign sign = (Sign) state;
				if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[SpleefArena]")) {
					String line2 = ChatColor.stripColor(sign.getLine(2));
					if (line2.startsWith("ID: ")) {
						Util.PrintMSG(p, "sign_remove");
						String id = line2.replace("ID: ", "");
						Config.getSignCfg().removeArea(id);
					}
				}
			}
		}
	}

	public void replaceArena(SignData sd) {
		if (sd != null) {
			Location l1 = sd.getMinPoint();
			Location l2 = sd.getMaxPoint();
			World w = sd.getWorld();
			Material m = sd.getMaterial();
			double y = sd.getY();
			for (int x = Math.min(l1.getBlockX(), l2.getBlockX()); x <= Math.max(l1.getBlockX(), l2.getBlockX()); x++)
				for (int z = Math.min(l1.getBlockZ(), l2.getBlockZ()); z <= Math.max(l1.getBlockZ(), l2.getBlockZ()); z++) {
					Location repblock = new Location(w, x, y, z);
					repblock.getBlock().setType(m);
				}
		} else {
			System.out.println("error");
		}
	}

	private Block isBlockSign(Block b) {
		for (int z = -1; z <= 1; z++) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					Block rel = b.getRelative(x, y, z);
					if (rel.getType() == Material.SIGN_POST || rel.getType() == Material.WALL_SIGN) {
						BlockState state = rel.getState();
						if ((state instanceof Sign)) {
							Sign sign = (Sign) state;
							if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[SpleefArena]")) {
								String line2 = ChatColor.stripColor(sign.getLine(2));
								if (line2.startsWith("ID: ")) {
									return rel;
								}
							}
						}
					}
				}
			}
		}
		return b;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
		Block b = event.getBlock();
		int n = event.getNewCurrent();
		int o = event.getOldCurrent();
		if ((n != 0) && (o == 0)) {
			if (isBlockSign(b) != b) {
				Block block = isBlockSign(b);
				BlockState state = block.getState();
				if ((state instanceof Sign)) {
					Sign sign = (Sign) state;
					if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[SpleefArena]")) {
						String line2 = ChatColor.stripColor(sign.getLine(2));
						if (line2.startsWith("ID: ")) {
							try {
								String id = line2.replace("ID: ", "");
								replaceArena(SignConfig.signs.get(Integer.parseInt(id)));
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
