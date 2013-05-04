package cz.bia.ea.regression.evolution.measure;

public class MeanSquareError extends SumSquareError {

	@Override
	public double getOverallError(double... errors) {
		return super.getOverallError(errors) / errors.length;
	}

}
