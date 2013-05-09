package cz.bia.ea.regression.evolution;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.generate.Tuple;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.ExpressionWrapper;
import cz.bia.ea.regression.model.factory.ExpressionFactory;
import cz.bia.ea.regression.util.RandomNumbers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.*;

public class Evolution {

	private final static boolean VERBOSE = false;

	private final RandomNumbers randomNumbers;

	private ExpressionFactory factory;

	private Configuration config;

	private List<Individual> population;

	private List<Tuple> dataTuples;

	public Evolution(@NotNull RandomNumbers randomNumbers) {
		this.randomNumbers = checkNotNull(randomNumbers);
	}

	public synchronized void setExpressionFactory(@NotNull ExpressionFactory factory) {
		this.factory = checkNotNull(factory);
	}

	public synchronized Result evolvePolyFor(@NotNull List<Tuple> dataTuples, @NotNull GAConfiguration configuration) {
		this.dataTuples = checkNotNull(dataTuples);
		this.config = checkNotNull(configuration);
		checkArgument(config.validate(), "GA configuration is not valid");
		checkState(factory != null, "factory must be set");

		return evolve(EvolutionType.GA);
	}

	public synchronized Result evolveTreeFor(@NotNull List<Tuple> dataTuples, @NotNull GPConfiguration configuration) {
		this.dataTuples = checkNotNull(dataTuples);
		this.config = checkNotNull(configuration);
		checkArgument(config.validate(), "GP configuration is not valid");
		checkState(factory != null, "factory must be set");

		return evolve(EvolutionType.GP);
	}

	private Result evolve(EvolutionType type) {
		final long start = System.currentTimeMillis();

		// Genetic Algorithm and Genetic Programming
		GAPolynomial.setObjective(config.objective.objective);
		GPTree.setObjective(config.objective.objective);

		// generate initial population
		population = new ArrayList<Individual>(config.populationSize);
		initPopulation(type, config.populationSize);
		evaluatePopulation();

		final double max = config.maxEpochs;
		final double threshold = config.fitnessThreshold;

		final List<Double> fitnessProgress = new LinkedList<Double>();
		final List<Long> timeProgress = new LinkedList<Long>();

		// main evolution loop
		Individual best = population.get(0);
		int epoch;
		for (epoch = 0; epoch < max && best.getFitness() > threshold; epoch++) {
			long t = System.currentTimeMillis();

			breadNewPopulation(type);
			evaluatePopulation();
			best = population.get(0);

			fitnessProgress.add(best.getFitness());
			timeProgress.add(System.currentTimeMillis() - t);

			if (VERBOSE) {
				System.out.printf("epoch=%d t=%d best=%g\n", epoch, System.currentTimeMillis() - t, best.getFitness());
			}
		}

		// clean-up
		population.clear();

		// return the best found expression
		return new Result(best.getExpression(), best.getFitness(), System.currentTimeMillis() - start, epoch, fitnessProgress, timeProgress);
	}

	private void initPopulation(EvolutionType type, int size) {
		switch (type) {
			case GA:
				final int order = ((GAConfiguration) config).initPolyOrder;
				final double from = ((GAConfiguration) config).paramRangeFrom;
				final double to = ((GAConfiguration) config).paramRangeTo;
				for (ExpressionWrapper wrapper : factory.generatePolyExpressions(size, order, from, to)) {
					population.add(new GAPolynomial(wrapper));
				}
				break;
			case GP:
				final int depth = ((GPConfiguration) config).initTreeDepth;
				for (ExpressionWrapper wrapper : factory.generateExpressions(size, depth)) {
					population.add(new GPTree(wrapper));
				}
				break;
		}
	}

	private void evaluatePopulation() {
		for (Individual individual : population) {
			individual.computeFitness(dataTuples);
		}
		Collections.sort(population);
	}

	private void breadNewPopulation(EvolutionType type) {
		// parent selection from the population
		final List<Individual> parents = tournamentSelection();
		// recombination of parents to create new children
		final List<? extends Individual> children = breadChildren(type, parents);

		// combine parents and children to new population
		population.clear();
		population.addAll(parents);
		population.addAll(children);
		// apply mutation on new population (excluding the best individual)
		mutate(type, population.subList(1, population.size()));

		// fill the rest of the population
		final int rest = config.populationSize - population.size();
		if (rest > 0) {
			initPopulation(type, rest);
		}
	}

	private List<Individual> tournamentSelection() {
		final int size = config.selectionSize;
		final int tournament = config.tournamentSize;
		final List<Individual> selection = new ArrayList<Individual>(size);

		// set the first-one as the best to ensure it'll survive
		Individual winner = population.get(0);
		for (int round = 0; round < size; round++) {
			// select the winner of one tournament
			for (int i = 0; i < tournament; i++) {
				Individual one = randomNumbers.nextElement(population);
				if (one.compareTo(winner) < 0) {
					winner = one;
				}
			}
			selection.add(winner);
			winner = null;
		}

		return selection;
	}

	@SuppressWarnings("unchecked")
	private List<? extends Individual> breadChildren(EvolutionType type, List<? extends Individual> parents) {
		switch (type) {
			case GA:
				final List<GAPolynomial> gaChildren = new ArrayList<GAPolynomial>(parents.size());

				// copy all parents
				for (GAPolynomial parent : (List<GAPolynomial>) parents) {
					gaChildren.add(new GAPolynomial(parent.getRoot().duplicate()));
				}

				// crossover pairs of parents
				final GAPolynomialUtils.PolyCrossoverType gaCrossover = ((GAConfiguration) config).crossoverType;
				for (int i = 0; i < gaChildren.size(); i += 2) {
					GAPolynomialUtils.crossover(gaCrossover, gaChildren.get(i), gaChildren.get(i + 1), randomNumbers);
				}

				return gaChildren;
			case GP:
				final List<GPTree> gpChildren = new ArrayList<GPTree>(parents.size());

				// copy all parents
				for (GPTree parent : (List<GPTree>) parents) {
					gpChildren.add(new GPTree(parent.getRoot().duplicate()));
				}

				// crossover pairs of parents
				final GPTreeUtils.TreeCrossoverType gpCrossover = ((GPConfiguration) config).crossoverType;
				for (int i = 0; i < gpChildren.size(); i += 2) {
					GPTreeUtils.crossover(gpCrossover, gpChildren.get(i), gpChildren.get(i + 1), randomNumbers);
				}

				return gpChildren;
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	private void mutate(EvolutionType type, List<? extends Individual> individuals) {
		final double p = config.mutationProbability;
		switch (type) {
			case GA:
				final GAPolynomialUtils.PolyMutationType gaMutation = ((GAConfiguration) config).mutationType;
				final double from = ((GAConfiguration) config).paramRangeFrom;
				final double to = ((GAConfiguration) config).paramRangeTo;
				for (GAPolynomial individual : (List<GAPolynomial>) individuals) {
					if (randomNumbers.nextDouble() < p) {
						GAPolynomialUtils.mutation(gaMutation, individual, from, to, randomNumbers);
					}
				}
				break;
			case GP:
				final GPTreeUtils.TreeMutationType gpMutation = ((GPConfiguration) config).mutationType;
				for (GPTree individual : (List<GPTree>) individuals) {
					if (randomNumbers.nextDouble() < p) {
						GPTreeUtils.mutation(gpMutation, individual, factory, randomNumbers);
					}
				}
				break;
		}
	}

	private static enum EvolutionType {
		GA, GP
	}

	public static class Result {
		public final Expression result;
		public final double fitness;
		public final long time;
		public final int epochs;
		public final List<Double> fitnessProgress;
		public final List<Long> timeProgress;

		public Result(Expression result, double fitness, long time, int epochs, List<Double> fitnessProgress, List<Long> timeProgress) {
			this.result = result;
			this.fitness = fitness;
			this.time = time;
			this.epochs = epochs;
			this.fitnessProgress = fitnessProgress;
			this.timeProgress = timeProgress;
		}
	}

}
