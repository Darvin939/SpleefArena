package darvin939.SpleefArena.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import darvin939.SpleefArena.BlockTask;
import darvin939.SpleefArena.SignData;
import darvin939.SpleefArena.SpleefArena;
import darvin939.SpleefArena.Utils;

public class SPBlockListener implements Listener {
	SpleefArena plg;

	public SPBlockListener(SpleefArena plg) {
		this.plg = plg;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignChange(SignChangeEvent event) {
		Player p = event.getPlayer();
		if ((ChatColor.stripColor(event.getLine(1)).equalsIgnoreCase("[sp]")) && (event.getLine(0).isEmpty()) && (event.getLine(2).isEmpty()) && (event.getLine(3).isEmpty()))
			if (!p.hasPermission("spleefarena.sign")) {
				event.setLine(1, "{sp}");
			} else {
				event.setLine(0, ChatColor.GREEN + p.getName());
				event.setLine(1, ChatColor.BLUE + "[SpleefArena]");
				event.setLine(2, "Type:" + ChatColor.RED + "Snow");
				event.setLine(3, "WE Found:" + ChatColor.RED + String.valueOf(Utils.WEFound()));
				Utils.msg(p, "SpleefArena sign created", 'p');
			}
		if ((ChatColor.stripColor(event.getLine(1)).equalsIgnoreCase("[SpleefArena]")) && (event.getLine(0).isEmpty()) && (event.getLine(2).isEmpty()) && (event.getLine(3).isEmpty()))
			event.setLine(1, "{SpleefArena}");
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {

		Block b = event.getBlock();
		if ((b.getType() == Material.SIGN_POST) || (b.getType() == Material.WALL_SIGN)) {
			BlockState state = b.getState();
			if ((state instanceof Sign)) {
				plg.SignW = b.getWorld();
				Sign sign = (Sign) state;

				if (plg.RedstoneCurrent)
					if ((ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("[SpleefArena]")) && (!sign.getLine(0).isEmpty()) && (!sign.getLine(2).isEmpty()) && (!sign.getLine(3).isEmpty()) && event.getNewCurrent() == event.getOldCurrent() && event.getNewCurrent() > 0) {
						plg.RedstoneCurrent = true;
						// check there <--->
					} else {
						plg.RedstoneCurrent = false;
					}
				if (plg.RedstoneCurrent) {
					Player p = plg.RedstoneEventPlayer;
					if (p.hasPermission("spleefarena.toggle"))
						if (sign.getLine(2).contains("Type:")) {
							if (Utils.WEFound()) {
								
								if (Utils.worldedit.getSelection(plg.serv.getPlayer(ChatColor.stripColor(sign.getLine(0)))) != null) {
									Location loc1 = Utils.worldedit.getSelection(plg.serv.getPlayer(ChatColor.stripColor(sign.getLine(0)))).getMinimumPoint();
									Location loc2 = Utils.worldedit.getSelection(plg.serv.getPlayer(ChatColor.stripColor(sign.getLine(0)))).getMaximumPoint();
									sign.setLine(2, loc1.getBlockX() + "." + loc1.getBlockY() + "." + loc1.getBlockZ());
									sign.setLine(3, loc2.getBlockX() + "." + loc2.getBlockY() + "." + loc2.getBlockZ());
									plg.SaveSignArea("World=" + sign.getWorld().getName() + ",x=" + sign.getLocation().getBlockX() + ",y=" + sign.getLocation().getBlockY() + ",z=" + sign.getLocation().getBlockZ(), loc1, loc2);
									Utils.msg(p, "Surface data saved", 'p');
									sign.update(true);
								} else 
									Utils.msg(p, "Region not selected!", 'e');
							} else
								Utils.msg(p, "WorldEdit was not found!", 'e');
						} else
							try {
								if (plg.config.isConfigurationSection("World=" + sign.getWorld().getName() + ",x=" + sign.getLocation().getBlockX() + ",y=" + sign.getLocation().getBlockY() + ",z=" + sign.getLocation().getBlockZ())) {
									ConfigurationSection coord = plg.config.getConfigurationSection("World=" + sign.getWorld().getName() + ",x=" + sign.getLocation().getBlockX() + ",y=" + sign.getLocation().getBlockY() + ",z=" + sign.getLocation().getBlockZ());
									plg.fx = coord.getInt("Fx");
									plg.fz = coord.getInt("Fz");
									plg.sx = coord.getInt("Sx");
									plg.sz = coord.getInt("Sz");
									plg.y = coord.getInt("Y");
									// <--->
									plg.setArea(p);
									BlockTask.execute(p, plg);
									// <--->
								} else
									Utils.msg(p, "Surface data not found! Probably data were removed", 'e');
							} catch (Exception e) {
							}
					else {
						Utils.msg(p, "You don't have the Permissions to do that!", 'p');
					}
					plg.RedstoneCurrent = false;
				} else
					plg.RedstoneCurrent = true;
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		Block b = event.getBlock();
		int bx = (int) b.getLocation().getX();
		int by = (int) b.getLocation().getY();
		int bz = (int) b.getLocation().getZ();
		if (!p.hasPermission("spleefarena.blockplace")) {
			plg.LoadCfg();
			for (int i = 0; i < plg.sd.size(); i++) {
				SignData sd = (SignData) plg.sd.get(i);
				if ((bx >= sd.fx) && (bx <= sd.sx) && (bz >= sd.fz) && (bz <= sd.sz) && (by >= sd.y) && (by <= sd.y + plg.ymax))
					b.setType(Material.AIR);
			}
		}

		if (b.getType() == Material.REDSTONE_TORCH_ON || b.getType() == Material.REDSTONE)
			plg.RedstoneEventPlayer = event.getPlayer();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		int bx = (int) b.getLocation().getX();
		int by = (int) b.getLocation().getY();
		int bz = (int) b.getLocation().getZ();

		if ((b.getType() == Material.SIGN_POST) || (b.getType() == Material.WALL_SIGN)) {
			BlockState state = b.getState();
			if ((state instanceof Sign)) {
				plg.SignW = b.getWorld();
				Sign sign = (Sign) state;
				try {
					if ((ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("[SpleefArena]")) && (!sign.getLine(0).isEmpty()) && (!sign.getLine(2).isEmpty()) && (!sign.getLine(3).isEmpty())) {
						if (plg.config.isConfigurationSection("World=" + sign.getWorld().getName() + ",x=" + sign.getLocation().getBlockX() + ",y=" + sign.getLocation().getBlockY() + ",z=" + sign.getLocation().getBlockZ())) {
							ConfigurationSection coord = plg.config.getConfigurationSection("World=" + sign.getWorld().getName() + ",x=" + sign.getLocation().getBlockX() + ",y=" + sign.getLocation().getBlockY() + ",z=" + sign.getLocation().getBlockZ());
							plg.fx = coord.getInt("Fx");
							plg.fz = coord.getInt("Fz");
							plg.sx = coord.getInt("Sx");
							plg.sz = coord.getInt("Sz");
							plg.y = coord.getInt("Y");
							plg.replaceArena(Material.AIR);
							plg.sd.remove(new SignData(coord.getString("Fx"), coord.getString("Fz"), coord.getString("Sx"), coord.getString("Sz"), coord.getString("Y"), "World=" + sign.getWorld().getName() + ",x=" + sign.getLocation().getBlockX() + ",y=" + sign.getLocation().getBlockY() + ",z="
									+ sign.getLocation().getBlockZ()));
						}
						plg.config.set("World=" + sign.getWorld().getName() + ",x=" + sign.getLocation().getBlockX() + ",y=" + sign.getLocation().getBlockY() + ",z=" + sign.getLocation().getBlockZ(), null);
						plg.SaveCfg();
					}
				} catch (Exception e) {
				}
			}
		}
		plg.LoadCfg();
		for (int i = 0; i < plg.sd.size(); i++) {
			SignData sd = (SignData) plg.sd.get(i);
			if ((bx >= sd.fx) && (bx <= sd.sx) && (bz >= sd.fz) && (bz <= sd.sz) && (by == sd.y)) {
				b.setType(Material.AIR);
			}

		}
	}

}
