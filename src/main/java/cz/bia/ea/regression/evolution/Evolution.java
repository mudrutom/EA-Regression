package cz.bia.ea.regression.evolution;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.generate.Tuple;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.ExpressionWrapper;
import cz.bia.ea.regression.model.factory.ExpressionFactory;
import cz.bia.ea.regression.util.RandomNumbers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Evolution {

	private final RandomNumbers randomNumbers;

	private ExpressionFactory factory;

	private Configuration config;

	private List<GPTree> population;

	private List<Tuple> dataTuples;

	public Evolution(@NotNull RandomNumbers randomNumbers) {
		this.randomNumbers = checkNotNull(randomNumbers);
	}

	public synchronized void setExpressionFactory(@NotNull ExpressionFactory factory) {
		this.factory = checkNotNull(factory);
	}

	public synchronized Expression evolveFor(@NotNull List<Tuple> dataTuples, @NotNull Configuration configuration) {
		this.dataTuples = checkNotNull(dataTuples);
		this.config = checkNotNull(configuration);
		checkArgument(config.validate(), "configuration is not valid");

		GPTree.setObjective(config.objective.objective);

		// generate initial population
		population = new ArrayList<GPTree>(config.populationSize);
		for (ExpressionWrapper wrapper : factory.generateExpressions(config.populationSize, config.initTreeDepth)) {
			population.add(new GPTree(wrapper));
		}

		final double max = config.maxEpochs;
		final double threshold = config.fitnessThreshold;

		// main evolution loop
		GPTree best = null;
		for (int epoch = 0; epoch < max; epoch++) {
			long t = System.currentTimeMillis();
			evaluatePopulation();
			best = population.get(0);
			double bestFit = best.getFitness();
			if (bestFit < threshold) {
				break;
			}
			breadNewPopulation();
			System.out.printf("epoch=%d t=%d best=%g\n", epoch, System.currentTimeMillis() - t, bestFit);
		}

		// return the best found expression
		return (best == null) ? null : best.getExpression();
	}

	private void evaluatePopulation() {
		for (GPTree individual : population) {
			individual.computeFitness(dataTuples);
		}
		Collections.sort(population);
	}

	private void breadNewPopulation() {
		// parent selection from the population
		final List<GPTree> parents = tournamentSelection();
		// recombination of parents to create new children
		final List<GPTree> children = breadChildren(parents);

		// combine parents and children to new population
		population.clear();
		population.addAll(parents);
		population.addAll(children);
		// apply mutation on new population
		mutate(population);

		// fill the rest of the population
		final int rest = config.populationSize - population.size();
		if (rest > 0) {
			for (ExpressionWrapper wrapper : factory.generateExpressions(rest, config.initTreeDepth)) {
				population.add(new GPTree(wrapper));
			}
		}
	}

	private List<GPTree> tournamentSelection() {
		final int size = config.selectionSize;
		final int tournament = config.tournamentSize;
		final List<GPTree> selection = new ArrayList<GPTree>(size);

		// set the first-one as the best to ensure it'll survive
		GPTree winner = population.get(0);
		for (int round = 0; round < size; round++) {
			// select the winner of one tournament
			for (int i = 0; i < tournament; i++) {
				GPTree one = randomNumbers.nextElement(population);
				if (one.compareTo(winner) < 0) {
					winner = one;
				}
			}
			selection.add(winner);
		}

		return selection;
	}

	private List<GPTree> breadChildren(List<GPTree> parents) {
		final List<GPTree> children = new ArrayList<GPTree>(parents.size());

		// copy all parents
		for (GPTree parent : parents) {
			children.add(new GPTree(parent.getRoot().duplicate()));
		}

		// crossover pairs of parents
		for (int i = 0; i < children.size(); i += 2) {
			GPTreeUtils.crossover(config.crossoverType, children.get(i), children.get(i+1), randomNumbers);
		}

		return children;
	}

	private void mutate(List<GPTree> individuals) {
		for (GPTree individual : individuals) {
			if (randomNumbers.nextDouble() < config.mutationProbability) {
				GPTreeUtils.mutation(config.mutationType, individual, factory, randomNumbers);
			}
		}
	}

}
