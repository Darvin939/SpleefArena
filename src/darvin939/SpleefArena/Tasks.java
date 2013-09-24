package darvin939.SpleefArena;

public class Tasks {
	private SpleefArena plg;

	public Tasks(SpleefArena plugin) {
		plg = plugin;
		run();
	}

	private void run() {
		plg.getServer().getScheduler().runTaskTimer(plg, new Runnable() {
			public void run() {
				Config.getSignCfg().saveSigns();
			}
		}, 1000, 1000);
	}
}
