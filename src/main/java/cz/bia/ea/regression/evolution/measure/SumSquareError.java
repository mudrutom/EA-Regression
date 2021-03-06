package cz.bia.ea.regression.evolution.measure;

public class SumSquareError implements Objective {

	@Override
	public double getError(double expected, double real) {
		final double e = expected - real;
		return e * e;
	}

	@Override
	public double getOverallError(double... errors) {
		double sum = 0.0;
		for (double error : errors) {
			sum += error;
		}
		return sum;
	}

}
