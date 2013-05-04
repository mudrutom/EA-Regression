package cz.bia.ea.regression.evolution.measure;

public class SumAbsoluteError implements Objective {

	@Override
	public double getError(double expected, double real) {
		return Math.abs(expected - real);
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
