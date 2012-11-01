package darvin939.SpleefArena.Commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import darvin939.SpleefArena.SpleefArena;
import darvin939.SpleefArena.Utils;

public class StrikeCommand {

	public StrikeCommand() {
	}

	public void execute(Player p, String[] args, SpleefArena plg) {
		if (args.length == 1) {
			Utils.msg(p, "&3/spl strike &2[playername/random] {damage}", 'u');
		} else if (args.length >= 2) {
			plg.strike_plys.put(p, true);
			if (args[1].equalsIgnoreCase("random")) {
				execRandom(p, Utils.newArgs(args), plg);
				return;
			} else if (args[1].equalsIgnoreCase("all")) {
				execAll(p, Utils.newArgs(args), plg);
				return;
			}
			try {
				for (Player pia : plg.pInArea(p)) {
					if (args[1].equalsIgnoreCase(pia.getName())) {
						execPlayer(pia, p, Utils.newArgs(args), plg);
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (Player pio : plg.players) {
				if (args[1].equalsIgnoreCase(pio.getName())) {
					Utils.msg(p, "Player " + Bukkit.getPlayer(args[1]).getName() + " is outside of playing area", 'e');
					return;
				}
			}
			plg.strike_plys.remove(p);
			Utils.msg(p, "&3/spl strike &2[playername/random] {damage}", 'u');
		}
	}

	private void execAll(Player p, String[] args, SpleefArena plg) {
		if (plg.pInArea(p).size() != 0) {
			if (args.length == 1) {
				for (Player pia : plg.pInArea(p)) {
					World world = pia.getWorld();
					world.strikeLightningEffect(pia.getLocation());
					pia.damage(2);
					Utils.msg(pia, "You was striked by " + p.getName(), 'p');
				}
			} else if (args.length == 2) {
				for (Player pia : plg.pInArea(p)) {
					if (args[1].matches("[1-9]+[0-9]") || args[1].matches("[1-9]") && Integer.valueOf(args[1]) <= 20) {
						World world = pia.getWorld();
						world.strikeLightningEffect(pia.getLocation());
						pia.damage(Integer.valueOf(args[1]));
						Utils.msg(pia, "You was striked by " + p.getName(), 'p');

					} else
						Utils.msg(p, "&3Damage: &21-20", 'u');
				}
			}
		} else {
			Utils.msg(p, "There are no players on playing area", 'e');
		}
	}

	private void execPlayer(Player pia, Player p, String[] args, SpleefArena plg) {
		if (args.length == 1) {
			World world = pia.getWorld();
			world.strikeLightningEffect(pia.getLocation());
			pia.damage(2);
			Utils.msg(pia, "You was striked by " + p.getName(), 'p');
		} else if (args.length == 2) {
			if (args[1].matches("[1-9]+[0-9]") || args[1].matches("[1-9]") && Integer.valueOf(args[1]) <= 20) {
				World world = pia.getWorld();
				world.strikeLightningEffect(pia.getLocation());
				pia.damage(Integer.valueOf(args[1]));
				Utils.msg(pia, "You was striked by " + p.getName(), 'p');
			} else
				Utils.msg(p, "&3Damage: &21-20", 'u');
		}
	}

	private void execRandom(Player p, String[] args, SpleefArena plg) {
		if (args.length == 1) {
			Random rnd = new Random();
			int index = rnd.nextInt(plg.pInArea(p).size());
			if (index >= -1 && plg.pInArea(p).size() > 0) {
				Player pia = plg.pInArea(p).get(rnd.nextInt(plg.pInArea(p).size()));
				World world = pia.getWorld();
				world.strikeLightningEffect(pia.getLocation());
				pia.damage(2);
				Utils.msg(pia, "You was striked by " + p.getName(), 'p');
			}
		} else if (args.length == 2) {
			if (args[1].matches("[1-9]+[0-9]") || args[1].matches("[1-9]") && Integer.valueOf(args[1]) <= 20) {
				Random rnd = new Random();
				int index = rnd.nextInt(plg.pInArea(p).size());
				if (index >= -1 && plg.pInArea(p).size() > 0) {
					Player pia = plg.pInArea(p).get(rnd.nextInt(plg.pInArea(p).size()));
					World world = pia.getWorld();
					world.strikeLightningEffect(pia.getLocation());
					pia.damage(Integer.valueOf(args[1]));
					Utils.msg(pia, "You was striked by " + p.getName(), 'p');
				}
			} else
				Utils.msg(p, "&3Damage: &21-20", 'u');
		}
	}
}
