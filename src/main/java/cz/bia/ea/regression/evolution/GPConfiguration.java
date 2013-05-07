package cz.bia.ea.regression.evolution;

import cz.bia.ea.regression.evolution.measure.ObjectiveFunction;

/**
 * Configuration for the Genetic Programming evolution.
 */
public class GPConfiguration extends Configuration {

	/** Maximal depth of initially generated trees. */
	public int initTreeDepth;

	/** Type of crossover operator. */
	public GPTreeUtils.TreeCrossoverType crossoverType;
	/** Type of mutation operator. */
	public GPTreeUtils.TreeMutationType mutationType;

	@Override
	public boolean validate() {
		return  super.validate() &&
				initTreeDepth >= 0 &&
				crossoverType != null && mutationType != null;
	}

	/**
	 * @return configuration with default values set
	 */
	public static GPConfiguration createDefaultConfig() {
		final GPConfiguration config = new GPConfiguration();

		config.objective = ObjectiveFunction.MSE;

		config.initTreeDepth = 5;

		config.populationSize = 500;
		config.selectionSize = 200;
		config.tournamentSize = 5;

		config.crossoverType = GPTreeUtils.TreeCrossoverType.SUBTREE_CROSSOVER;
		config.mutationType = GPTreeUtils.TreeMutationType.POINT_MUTATION;
		config.mutationProbability = 0.2;

		config.maxEpochs = 100;
		config.fitnessThreshold = 0.01;

		return config;
	}

}
