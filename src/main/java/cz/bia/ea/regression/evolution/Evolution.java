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
import java.util.concurrent.CountDownLatch;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class Evolution {

	private final RandomNumbers randomNumbers;

	private final ExpressionFactory factory;

//	private static final int threadNum = 2;
//	private final ExecutorService executor;
//	private final Evaluator[] evaluators;

	private List<GPTree> population;
	private List<Tuple> dataTuples;

	private final int popSize, depth, tournamentSize, selectionSize;
	private final double threshold;

	public Evolution(@NotNull ExpressionFactory factory, @NotNull RandomNumbers randomNumbers) {
		this.randomNumbers = checkNotNull(randomNumbers);
		this.factory = checkNotNull(factory);

//		executor = Executors.newFixedThreadPool(threadNum);
//		evaluators = new Evaluator[threadNum];

		popSize = 500;  // popSize % threads == 0
		depth = 5;
		tournamentSize = 5;
		selectionSize = 200; // selSize % 2 == 0
		threshold = 0.01;

		// TODO configuration object
	}

	public synchronized Expression evolve(@NotNull List<Tuple> dataTuples) {
		this.dataTuples = checkNotNull(dataTuples);

		// generate initial population
		population = new ArrayList<GPTree>(popSize);
		for (ExpressionWrapper wrapper : factory.generateExpressions(popSize, depth)) {
			population.add(new GPTree(wrapper));
		}

		// main loop
		int i = 0;
		GPTree best = null;
		while (best == null || best.getFitness() > threshold) {
			evaluatePopulation();
			best = population.get(0);
			breadNewPopulation();
			i++;
		}

		// return the best found expression
		return best.getExpression();
	}

	private void evaluatePopulation() {
		for (GPTree individual : population) {
			individual.computeFitness(dataTuples);
		}
		Collections.sort(population);

		// TODO run Evaluators in parallel
//		final CountDownLatch latch = new CountDownLatch(threadNum);
//		final int n = popSize / threadNum;
//		for (int i = 0; i < threadNum; i++) {
//			evaluators[i].setIndividuals(population.subList(i * n, (i+1) * n));
//			evaluators[i].setCountDownLatch(latch);
//			executor.execute(evaluators[i]);
//		}
//		try {
//			latch.wait();
//		} catch (InterruptedException e) {
//			e.printStackTrace(System.err);
//		}
	}

	private void breadNewPopulation() {
		// parent selection from the population
		final List<GPTree> parents = tournamentSelection();
		// recombination of parents to create new children
		final List<GPTree> children = breadChildren(parents);

		// combine parents and children to new population
		population = new ArrayList<GPTree>(popSize);
		population.addAll(parents);
		population.addAll(children);
		// apply mutation on new population (excluding the best-one)
		mutate(population.subList(1, population.size()));

		// fill the rest of the population
		for (ExpressionWrapper wrapper : factory.generateExpressions(popSize - population.size(), depth)) {
			population.add(new GPTree(wrapper));
		}
	}

	private List<GPTree> tournamentSelection() {
		final List<GPTree> selection = new ArrayList<GPTree>(selectionSize);
		
		// set the first-one as the best to ensure it'll survive
		GPTree winner = population.get(0);
		for (int round = 0; round < selectionSize; round++) {
			// select the winner of one tournament
			for (int i = 0; i < tournamentSize; i++) {
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
			GPTreeUtils.subtreeCrossover(children.get(i), children.get(i+1), randomNumbers);
		}

		return children;

		// TODO run Breeders in parallel
//		final CountDownLatch latch = new CountDownLatch(threadNum);
//		final int n = children.size() / threadNum;
//		for (int i = 0; i < threadNum; i++) {
//			executor.execute(new Breeder(parents.subList(i * n, (i+1) * n), latch, randomNumbers));
//		}
//		try {
//			latch.wait();
//		} catch (InterruptedException e) {
//			e.printStackTrace(System.err);
//		}
	}

	private void mutate(List<GPTree> individuals) {
		for (GPTree individual : individuals) {
			if (randomNumbers.nextInt(10) == 0) { // 10% chance
				GPTreeUtils.subtreeMutation(individual, factory, randomNumbers);
			}
			if (randomNumbers.nextInt(10) == 0) { // 10% chance
				GPTreeUtils.pointMutation(individual, factory, randomNumbers);
			}
		}
	}

	private static class Evaluator implements Runnable {

		private final List<Tuple> dataTuples;
		private List<GPTree> individuals = null;
		private CountDownLatch latch = null;

		public Evaluator(@NotNull List<Tuple> dataTuples) {
			this.dataTuples = checkNotNull(dataTuples);
		}

		public void setIndividuals(@NotNull List<GPTree> individuals) {
			this.individuals = checkNotNull(individuals);
		}

		public void setCountDownLatch(@NotNull CountDownLatch latch) {
			this.latch = checkNotNull(latch);
		}

		@Override
		public void run() {
			checkState(individuals != null && latch != null, "individuals and latch must be set");
			for (GPTree individual : individuals) {
				individual.computeFitness(dataTuples);
			}
			latch.countDown();
		}
	}

	private static class Breeder implements Runnable {

		private final List<GPTree> parents;
		private final CountDownLatch latch;
		private final RandomNumbers randomNumbers;

		public Breeder(List<GPTree> parents, CountDownLatch latch, RandomNumbers randomNumbers) {
			this.parents = parents;
			this.latch = latch;
			this.randomNumbers = randomNumbers;
		}

		@Override
		public void run() {
			// crossover all pairs of parents
			for (int i = 0; i < parents.size(); i += 2) {
				GPTreeUtils.subtreeCrossover(parents.get(i), parents.get(i+1), randomNumbers);
			}
			latch.countDown();
		}
	}

}
