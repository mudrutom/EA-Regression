package cz.bia.ea.regression.evolution.measure;

public enum ObjectiveFunction {

	MAE(new MeanAbsoluteError()), MSE(new MeanSquareError()),
	SAE(new SumAbsoluteError()), SSE(new SumSquareError());

	public final Objective objective;

	private ObjectiveFunction(Objective objective) {
		this.objective = objective;
	}

}
