package cz.bia.ea.regression.generate;

public class Tuple {

	public final double x;
	public final double y;

	public Tuple(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return String.format("[%g,%g]", x, y);
	}

}
