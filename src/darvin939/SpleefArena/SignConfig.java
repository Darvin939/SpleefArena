package darvin939.SpleefArena;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import darvin939.SpleefArena.Utils.Util;

public class SignConfig {
	public static LinkedHashMap<Integer, SignData> signs = new LinkedHashMap<Integer, SignData>();
	private FileConfiguration cfgSigns;
	private File cfgSignsFile;
	private SpleefArena plg;

	// public static LinkedHashMap<Integer, SignData> getSigns() {
	// return signs;
	// }

	// public static void setSigns(LinkedHashMap<Integer, SignData> s) {
	// signs = s;
	// }

	public SignConfig(SpleefArena plugin) {
		plg = plugin;
		cfgSignsFile = new File(plg.getDataFolder() + "/signs.yml");
		cfgSigns = YamlConfiguration.loadConfiguration(cfgSignsFile);
		saveConfig();
	}

	public void saveConfig() {
		try {
			cfgSigns.save(cfgSignsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeArea(String id) {
		if (cfgSigns.isConfigurationSection(id))
			cfgSigns.set(id, null);
		signs.remove(id);
		saveConfig();
	}

	public void loadSigns() {
		int errors = 0;
		for (String s : cfgSigns.getKeys(false)) {
			String world = s.split(":")[0];
			if (plg.getServer().getWorld(world) == null)
				continue;
			World w = plg.getServer().getWorld(world);
			try {
				ConfigurationSection sec = cfgSigns.getConfigurationSection(s);
				SignData sd = getArea2DFromSection(sec, w);
				sd.setOwner(plg.getServer().getPlayer(sec.getString("Owner")));
				sd.setMaterial(Material.getMaterial(sec.getString("Material")));
				signs.put(sec.getInt("ID"), sd);
			} catch (Exception e) {
				errors++;
			}
		}
		if (errors > 0)
			plg.getLogger().info(errors + " sign(s) not loaded!");
	}

	public SignData getArea2DFromSection(ConfigurationSection sec, World w) {
		double y = sec.getDouble("Y");
		Location l1, l2;
		String[] pos1 = sec.getString("Pos1").split(";");
		double x1 = Double.parseDouble(pos1[0]);
		double z1 = Double.parseDouble(pos1[1]);
		l1 = new Location(w, x1, y, z1);

		String[] pos2 = sec.getString("Pos2").split(";");
		double x2 = Double.parseDouble(pos2[0]);
		double z2 = Double.parseDouble(pos2[1]);
		l2 = new Location(w, x2, y, z2);

		return new SignData(l1, l2);
	}

	public int getID() {
		int id = 0;
		for (String s : cfgSigns.getKeys(false)) {
			try {
				if (cfgSigns.getConfigurationSection(s).getInt("ID") > id)
					id = cfgSigns.getConfigurationSection(s).getInt("ID");
			} catch (Exception e) {
			}
		}
		return id + 1;
	}

	public void getAreaInfo(Player p, SignData sd) {
		Location l1 = sd.getMinPoint();
		Location l2 = sd.getMaxPoint();
		Util.Print(p, "&b===== &2Area Info&b =====");
		Util.Print(p, "&7Owner:&6 " + Util.FCTU(sd.getOwner().getName()));
		Util.Print(p, "&7World:&6 " + sd.getWorld().getName());
		Util.Print(p, "&7Material:&6 " + sd.getMaterial().name());
		Util.Print(p, "&7Pos1:&6 x:" + l1.getBlockX() + " y:" + l1.getBlockY() + " z:" + l1.getBlockZ());
		Util.Print(p, "&7Pos2:&6 x:" + l2.getBlockX() + " y:" + l2.getBlockY() + " z:" + l2.getBlockZ());
	}

	public void saveSigns() {
		for (Entry<Integer, SignData> set : signs.entrySet()) {
			String id = String.valueOf(set.getKey());
			if (!cfgSigns.isConfigurationSection(id))
				cfgSigns.createSection(id);
			ConfigurationSection section = cfgSigns.getConfigurationSection(id);
			section.set("Owner", set.getValue().getOwner().getName());
			section.set("Material", set.getValue().getMaterial().name());
			section.set("Pos1", set.getValue().getMinPoint().getBlockX() + ";" + set.getValue().getMinPoint().getBlockZ());
			section.set("Pos2", set.getValue().getMaxPoint().getBlockX() + ";" + set.getValue().getMaxPoint().getBlockZ());
			section.set("Y", set.getValue().getY());
			// section.set("ID", getID());
			saveConfig();
		}
	}
}
