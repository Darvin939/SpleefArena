package darvin939.SpleefArena;

import darvin939.SpleefArena.Commands.Commands;
import darvin939.SpleefArena.Listeners.SPBlockListener;
import darvin939.SpleefArena.Listeners.SPPlayerListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SpleefArena extends JavaPlugin {
	private Logger log = Logger.getLogger("Minecraft");
	public PluginDescriptionFile des;
	SPBlockListener blocklis = new SPBlockListener(this);
	SPPlayerListener playerlis = new SPPlayerListener(this);
	public Server serv;
	private Commands Commander;
	public ArrayList<Player> inarea = new ArrayList<Player>();
	public HashMap<Player, SignData> area = new HashMap<Player, SignData>();
	public String[] cmds = { "update", "help", "destruction", "strike" };
	public ArrayList<SignData> sd = new ArrayList<SignData>();
	public HashMap<Player, Boolean> strike_plys = new HashMap<Player, Boolean>();
	public HashMap<Player, Boolean> desc_plys = new HashMap<Player, Boolean>();
	public HashMap<Player, Integer> desc_tasks = new HashMap<Player, Integer>();
	public int repeat = 0;
	public int radius = 0;
	Random rnd = new Random();

	public String v_url = "http://dl.dropbox.com/u/27143035/plugins/SpleefArena/version_new.txt";
	public double v_curr = 0.0;
	public double v_new = 0.0;

	// String px = ChatColor.AQUA + "[SP]" + ChatColor.WHITE;

	public double fx = 0.0;
	public double fz = 0.0;
	public double sx = 0.0;
	public double sz = 0.0;
	public double y = 0.0;
	public double ymax = 3;
	public World SignW;
	public FileConfiguration config;
	public boolean RedstoneCurrent = false;
	public Player RedstoneEventPlayer;
	public Player[] players;

	public void onDisable() {
		log.info("[SpleefArena] Plugin v." + des.getVersion() + " disabled");
		LoadCfg();
		SaveCfg();
	}

	public void onEnable() {
		des = getDescription();
		log.info("[SpleefArena] Plugin v." + des.getVersion() + " enabled");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(blocklis, this);
		pm.registerEvents(playerlis, this);
		serv = getServer();
		config = getConfig();
		SaveCfg();
		LoadCfg();
		v_curr = Double.valueOf(des.getVersion());
		Commander = new Commands(this);
		getCommand("spl").setExecutor(Commander);
	}

	public void getNewVersion() {
		try {
			URL url = new URL(v_url);
			InputStream u = url.openConnection().getInputStream();
			byte[] buffer = new byte[32768];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (true) {
				int readBytesCount = u.read(buffer);
				if (readBytesCount == -1) {
					break;
				}
				if (readBytesCount > 0) {
					baos.write(buffer, 0, readBytesCount);
				}
			}
			baos.flush();
			baos.close();
			byte[] data = baos.toByteArray();
			String[] ln = new String(data).split(";");
			v_new = Double.valueOf(ln[0]);
		} catch (IOException e) {
		}
	}

	public void LoadCfg() {
		try {
			sd.clear();
			Set<String> keys = config.getKeys(true);
			if (keys.size() > 0)
				for (String s : keys) {
					String[] ln = s.split("\\.");
					if (ln.length == 1)
						sd.add(new SignData(config.getString(s + ".Fx", "unknown"), config.getString(s + ".Fz", "unknown"), config.getString(s + ".Sx", "unknown"), config.getString(s + ".Sz", "unknown"), config.getString(s + ".Y", "unknown"), ln[0]));
				}
		} catch (Exception e) {
		}
	}

	public void SaveCfg() {
		try {
			saveConfig();
		} catch (Exception e) {
		}
	}

	public void SaveSignArea(String signdata, Location loc1, Location loc2) {
		ConfigurationSection s = config.createSection(signdata);
		s.set("Fx", Integer.valueOf(loc1.getBlockX()));
		s.set("Fz", Integer.valueOf(loc1.getBlockZ()));
		s.set("Sx", Integer.valueOf(loc2.getBlockX()));
		s.set("Sz", Integer.valueOf(loc2.getBlockZ()));
		s.set("Y", Integer.valueOf(loc1.getBlockY()));
		SaveCfg();
	}

	public void replaceArena(Material mat) {
		Location loc1 = new Location(SignW, fx, y, fz);
		Location loc2 = new Location(SignW, sx, y, sz);
		int y = loc1.getBlockY();
		for (int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x <= Math.max(loc1.getBlockX(), loc2.getBlockX()); x++)
			for (int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z <= Math.max(loc1.getBlockZ(), loc2.getBlockZ()); z++) {
				Location repblock = new Location(SignW, x, y, z);
				repblock.getBlock().setType(mat);
			}
	}

	public void setArea(Player p) {
		SaveCfg();
		LoadCfg();
		for (int i = 0; i < sd.size(); i++) {
			SignData sd = (SignData) this.sd.get(i);
			if ((fx == sd.fx) && (sx == sd.sx) && (fz == sd.fz) && (sz == sd.sz) && (y == sd.y)) {
				area.put(p, sd);
			}
		}
	}

	public void destruction(final Player p, int repeat, final int radius) {
		if (area.get(p) != null) {
			final SignData sd = (SignData) area.get(p);
			int task;
			Utils.msg(p, "Destruction mode currently enabled!", 'p');
			task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {

					int xleng = (int) (sd.sx - sd.fx);
					int zleng = (int) (sd.sz - sd.fz);
					int rand_x = (int) (sd.fx + rnd.nextInt(xleng + 1));
					int rand_z = (int) (sd.fz + rnd.nextInt(zleng + 1));
					for (int i = 0; i <= radius; i++) {
						int mj = (int) Math.sqrt(radius * radius - i * i);
						for (int j = 0; j <= mj; j++) {
							if ((rand_x + i >= fx) && ((rand_x + i <= sd.sx) && (rand_z + j >= sd.fz) && (rand_z + j <= sd.sz))) {
								Location delblock = new Location(SignW, rand_x + i, y, rand_z + j);
								delblock.getBlock().setType(Material.AIR);
							}
							if ((rand_x + i >= sd.fx) && ((rand_x + i <= sd.sx) && (rand_z - j >= sd.fz) && (rand_z - j <= sd.sz))) {
								Location delblock = new Location(SignW, rand_x + i, y, rand_z - j);
								delblock.getBlock().setType(Material.AIR);
							}
							if ((rand_x - i >= sd.fx) && ((rand_x - i <= sd.sx) && (rand_z + j >= sd.fz) && (rand_z + j <= sd.sz))) {
								Location delblock = new Location(SignW, rand_x - i, y, rand_z + j);
								delblock.getBlock().setType(Material.AIR);
							}
							if ((rand_x - i >= sd.fx) && ((rand_x - i <= sd.sx) && (rand_z - j >= sd.fz) && (rand_z - j <= sd.sz))) {
								Location delblock = new Location(SignW, rand_x - i, y, rand_z - j);
								delblock.getBlock().setType(Material.AIR);
							}
						}
					}

				}
			}, 0, repeat * 2);
			desc_tasks.put(p, task);
		} else
			Utils.msg(p, "Please choose the arena data", 'e');
	}

	public ArrayList<Player> pInArea(Player p) {
		inarea.clear();
		if (area.get(p) != null) {
			final SignData sd = (SignData) area.get(p);
			for (Player pia : players) {
				Location loc = p.getLocation();
				if (loc.getBlockX() <= sd.sx && loc.getBlockX() >= sd.fx && loc.getBlockZ() <= sd.sz && loc.getBlockZ() >= sd.fz && loc.getBlockY() <= sd.y + ymax && loc.getBlockY() >= sd.y) {
					inarea.add(pia);
				}
			}
			area.remove(p);
		} else
			Utils.msg(p, "Please choose the arena data", 'e');
		return inarea;
	}

}
