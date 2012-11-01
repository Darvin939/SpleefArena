package darvin939.SpleefArena;

public class SignData {
	public int fx;
	public int fz;
	public int sx;
	public int sz;
	public int y;
	String section;

	public SignData(String fx, String fz, String sx, String sz, String y, String sec) {
		this.fx = Integer.valueOf(fx).intValue();
		this.fz = Integer.valueOf(fz).intValue();
		this.sx = Integer.valueOf(sx).intValue();
		this.sz = Integer.valueOf(sz).intValue();
		this.y = Integer.valueOf(y).intValue();
		section = sec;
	}
}
