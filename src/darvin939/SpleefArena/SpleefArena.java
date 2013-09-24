package darvin939.SpleefArena;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import darvin939.SpleefArena.Config.Nodes;
import darvin939.SpleefArena.Commands.Handler;
import darvin939.SpleefArena.Commands.InvalidUsage;
import darvin939.SpleefArena.Commands.Parser;
import darvin939.SpleefArena.Commands.Handlers.About;
import darvin939.SpleefArena.Commands.Handlers.Help;
import darvin939.SpleefArena.Listeners.BlockListener;
import darvin939.SpleefArena.Listeners.PlayerListener;
import darvin939.SpleefArena.Listeners.SignListener;
import darvin939.SpleefArena.Listeners.Wand;
import darvin939.SpleefArena.Utils.Util;

public class SpleefArena extends JavaPlugin {
	private Logger log = Logger.getLogger("Minecraft");

	public static String prefix = "[SpleefArena] ";
	public static final String cmdPrefix = "/spl ";
	public static final String sysPrefix = "&b[SpleefArena]&f ";
	public static final String premPrefix = "spleefarena.";

	public PluginDescriptionFile des;
	public static File datafolder;
	public Parser Commands = new Parser();

	private PlayerListener plis = new PlayerListener(this);
	private SignListener slis = new SignListener(this);
	private BlockListener blis = new BlockListener(this);
	private Wand wlis = new Wand(this);

	public void onEnable() {
		des = getDescription();
		log.info(prefix + "Plugin " + des.getName() + " v" + des.getVersion() + " enabled");
		datafolder = getDataFolder();
		if (!datafolder.exists())
			datafolder.mkdir();
		PluginManager pm = getServer().getPluginManager();
		Config.extract(new String[] { "config.yml" });
		Config.load(new File(getDataFolder(), "config.yml"));
		new Config(this, Nodes.verCheck.getBoolean(), Nodes.language.getString().toLowerCase(), "spleefarena", prefix);
		Config.getSignCfg().loadSigns();
		new Tasks(this);
		registerEvents(pm);
		registerCommands();
	}

	private void registerCommands() {
		// help
		Commands.add("/spl help", new Help(this));
		Commands.setPermission("help", "darkdays.help");
		Commands.setHelp("help", Config.FGU.MSG("hlp_cmd_help"));

		// about
		Commands.add("/spl about", new About(this));
		Commands.setHelp("about", Config.FGU.MSG("hlp_cmd_about"));
	}

	public static String getDataPath() {
		return datafolder.getAbsolutePath();
	}

	public void registerEvents(PluginManager pm) {
		pm.registerEvents(plis, this);
		pm.registerEvents(slis, this);
		pm.registerEvents(blis, this);
		pm.registerEvents(wlis, this);
	}

	public void onDisable() {
		log.info(prefix + "Plugin v." + des.getVersion() + " disabled");
		Config.getSignCfg().saveSigns();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String split = "/" + command.getName().toLowerCase();
		if (args.length > 0)
			split = split + " " + args[0];
		Handler handler = Commands.getHandler(split);
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (split.equalsIgnoreCase("/spl") || args.length == 0) {
				Util.PrintSysPx(p, Config.FGU.MSG("hlp_topic", SpleefArena.cmdPrefix + "help"));
				return true;
			} else {

				if (handler == null) {
					Util.PrintMSG(p, "cmd_unknown", SpleefArena.cmdPrefix + args[0]);
					Util.Print(p, Config.FGU.MSG("hlp_commands") + " &2" + SpleefArena.cmdPrefix + "&7<" + Commands.getCommandsString() + "&7>");
					return true;
				}
				try {
					if (!handler.perform(p, args))
						Util.PrintSysPx(p, Config.FGU.MSG("hlp_topic", SpleefArena.cmdPrefix + "help"));
					return true;
				} catch (InvalidUsage ex) {
					return false;
				}
			}

		}
		sender.sendMessage("You must be a Player to do this");
		return true;
	}

	public boolean hasPermissions(Player p, String command, Boolean mess) {
		if (Commands.hasPermission(command)) {
			if (p.hasPermission(Commands.getPermission(command)))
				return true;
			else {
				if (mess)
					Config.FGU.PrintMsg(p, Config.FGU.MSG("cmd_noperm", Commands.getPermission(command), 'f', '7'));
				return false;
			}
		}
		return false;
	}

	public boolean hasPermissions(Player p, String perm) {
		return p.hasPermission(SpleefArena.premPrefix + perm);
	}

	public void getHelp(Player p, String command) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b================= &SpleefArena Help &b================="));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Command:&6 " + SpleefArena.cmdPrefix + command.replaceAll("\\.", " ")));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', Commands.getHelp(command)));
	}
}
