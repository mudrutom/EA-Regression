package cz.bia.ea.regression.evolution;

import cz.bia.ea.regression.evolution.measure.ObjectiveFunction;

/**
 * Configuration for the evolution.
 */
public class Configuration {

	/** An objective function. */
	public ObjectiveFunction objective;

	/** Maximal depth of initially generated trees. */
	public int initTreeDepth;

	/** Maximal size of the population. */
	public int populationSize;
	/** Size of selection to next generation. */
	public int selectionSize;
	/** Size of tournament selection. */
	public int tournamentSize;

	/** Type of crossover operator. */
	public GPTreeUtils.TreeCrossoverType crossoverType;
	/** Type of mutation operator. */
	public GPTreeUtils.TreeMutationType mutationType;
	/** Probability of mutation. */
	public double mutationProbability;

	/** Maximal number of epochs the evolution will last. */
	public int maxEpochs;
	/** Goal fitness threshold that evolution should reach. */
	public double fitnessThreshold;

	/**
	 * @return <tt>true</tt> IFF the configuration is valid
	 */
	public boolean validate() {
		return  objective != null && initTreeDepth >= 0 &&
				populationSize > 0 && selectionSize > 0 && tournamentSize > 0 &&
				populationSize >= 2 * selectionSize && selectionSize % 2 == 0 && populationSize >= tournamentSize &&
				crossoverType != null && mutationType != null &&
				mutationProbability >= 0.0 && mutationProbability <= 1.0 &&
				maxEpochs > 0 && !Double.isNaN(fitnessThreshold);
	}

	/**
	 * @return configuration with default values set
	 */
	public static Configuration createDefaultConfig() {
		final Configuration config = new Configuration();

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
