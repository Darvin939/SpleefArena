package darvin939.SpleefArena;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import darvin939.SpleefArena.Utils.FGUtilCore;

public class Config extends FGUtilCore {
	public static FGUtilCore FGU;
	private static SignConfig signcfg;

	public Config(SpleefArena plg, boolean vcheck, String lng, String devbukkitname, String px) {
		super(plg, vcheck, lng, devbukkitname, px);
		setupMessages();
		SaveMSG();
		FGU = this;
		signcfg = new SignConfig(plg);
	}

	public static SignConfig getSignCfg() {
		return signcfg;
	}

	private void setupMessages() {
		// arena_
		addMSG("arena_created", "Spleef Arena created (%1% - %2%, Y:%3%)");
		// region_
		addMSG("region_notsel", "Region not selected!");
		addMSG("region_wenf", "WorldEdit not found on the server!");
		// hlp_
		addMSG("hlp_topic", "Type %1% to show help topic");
		// hlp_cmd_
		addMSG("hlp_cmd_help", "Show this help topic");
		addMSG("hlp_cmd_about", "Show information about the plugin");
	}

	public static void extract(String[] names) {
		for (String name : names) {
			File actual = new File(SpleefArena.getDataPath(), name);
			if (!actual.exists()) {
				InputStream input = SpleefArena.class.getResourceAsStream("/" + name);
				if (input != null) {
					FileOutputStream output = null;
					try {
						output = new FileOutputStream(actual);
						byte[] buf = new byte[8192];
						int length = 0;
						while ((length = input.read(buf)) > 0) {
							output.write(buf, 0, length);
						}
					} catch (Exception e) {
					} finally {
						try {
							if (input != null)
								input.close();
						} catch (Exception e) {
						}
						try {
							if (output != null)
								output.close();
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}

	public static void load(File file) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (Nodes n : Nodes.values())
			if (!n.getNode().isEmpty())
				if (config.get(n.getNode()) != null)
					n.setValue(config.get(n.getNode()));
	}

	public static void save(File file) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (Nodes n : Nodes.values())
			if (!n.getNode().isEmpty())
				config.set(n.getNode(), n.getValue());
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static enum Nodes {
		language("Language", "english"), verCheck("Version-check", false), wand_item("WandItem", 369);
		String node;
		Object value;

		private Nodes(String node, Object value) {
			this.node = node;
			this.value = value;
		}

		public String getNode() {
			return node;
		}

		public Object getValue() {
			return this.value;
		}

		public Boolean getBoolean() {
			return (Boolean) value;
		}

		public Integer getInteger() {
			if (value instanceof Double)
				return ((Double) value).intValue();

			return (Integer) value;
		}

		public Double getDouble() {
			if (value instanceof Integer)
				return (double) ((Integer) value).intValue();

			return (Double) value;
		}

		public String getString() {
			return (String) value;
		}

		public Long getLong() {
			if (value instanceof Integer)
				return ((Integer) value).longValue();

			return (Long) value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public String toString() {
			return String.valueOf(value);
		}
	}

}
