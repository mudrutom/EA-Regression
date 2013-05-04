package cz.bia.ea.regression.evolution.measure;

public interface Objective {

	public double getError(double expected, double real);

	public double getOverallError(double... errors);

}
