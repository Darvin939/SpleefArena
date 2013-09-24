package darvin939.SpleefArena;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SignData {
	private Location lmin;
	private Location lmax;
	private Player owner;
	private Material material;
	private double y;
	private World world;

	public SignData(Location l1, Location l2) {
		this.lmin = l2;
		this.lmax = l1;
		this.y = Math.min(this.lmin.getY(), this.lmax.getY());
		this.world = l1.getWorld();
	}

	public Location getMinPoint() {
		return lmin;
	}

	public Location getMaxPoint() {
		return lmax;
	}

	public double getY() {
		return y;
	}

	public void setMaxPoint(Location max) {
		this.lmax = max;
	}

	public void setMinPoint(Location min) {
		this.lmin = min;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Player getOwner() {
		return owner;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}

	public World getWorld() {
		return world;
	}
}
