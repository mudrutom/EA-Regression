package cz.bia.ea.regression.evolution;

import cz.bia.ea.regression.evolution.measure.ObjectiveFunction;

/**
 * Configuration for the Genetic Algorithm evolution.
 */
public class GAConfiguration extends Configuration {

	/** Initial order of generate polynomial. */
	public int initPolyOrder;

	/** Beginning of the range for parameters of polynomial. */
	public double paramRangeFrom;
	/** End of the range for parameters of polynomial. */
	public double paramRangeTo;

	/** Type of crossover operator. */
	public GAPolynomialUtils.PolyCrossoverType crossoverType;
	/** Type of mutation operator. */
	public GAPolynomialUtils.PolyMutationType mutationType;

	@Override
	public boolean validate() {
		return  super.validate() &&
				initPolyOrder >= 0 &&
				paramRangeFrom < paramRangeTo &&
				crossoverType != null && mutationType != null;
	}

	/**
	 * @return configuration with default values set
	 */
	public static GAConfiguration createDefaultConfig() {
		final GAConfiguration config = new GAConfiguration();

		config.objective = ObjectiveFunction.MSE;

		config.initPolyOrder = 5;

		config.paramRangeFrom = -1.0;
		config.paramRangeTo = +1.0;

		config.populationSize = 500;
		config.selectionSize = 200;
		config.tournamentSize = 5;

		config.crossoverType = GAPolynomialUtils.PolyCrossoverType.SIMULATED_BINARY_CROSSOVER;
		config.mutationType = GAPolynomialUtils.PolyMutationType.GAUSSIAN_POINT_MUTATION;
		config.mutationProbability = 0.2;

		config.maxEpochs = 100;
		config.fitnessThreshold = 0.01;

		return config;
	}

}
