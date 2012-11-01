package darvin939.SpleefArena.Commands;

import org.bukkit.entity.Player;

import darvin939.SpleefArena.SpleefArena;
import darvin939.SpleefArena.Utils;

public class UpdateCommand {

	public UpdateCommand() {
	}

	public void execute(Player p, String[] args, SpleefArena plg) {
		try {
			plg.getNewVersion();
			if (plg.v_new > plg.v_curr) {

				Utils.msg(p, "SpleefArena &2v" + plg.des.getVersion() + " &fis outdated!", 'p');
				Utils.msg(p, "Please download new version &7(&2v" + plg.v_new + "&7)&f from:", '/');
				Utils.msg(p, " &3" + plg.v_url.replace("version_new.txt", "SpleefArena.jar"), '/');
			} else {
				Utils.msg(p, "Server have the latest version &7(&2v" + plg.des.getVersion() + "&7)&f of the plugin", '/');
			}
		} catch (Exception e) {
		}
	}

}
