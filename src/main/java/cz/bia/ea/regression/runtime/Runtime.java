package cz.bia.ea.regression.runtime;

import cz.bia.ea.regression.evolution.Evolution;
import cz.bia.ea.regression.evolution.GAConfiguration;
import cz.bia.ea.regression.evolution.GPConfiguration;
import cz.bia.ea.regression.evolution.measure.ObjectiveFunction;
import cz.bia.ea.regression.generate.DataGenerator;
import cz.bia.ea.regression.generate.Function;
import cz.bia.ea.regression.generate.Tuple;
import cz.bia.ea.regression.model.factory.ExpressionFactory;
import cz.bia.ea.regression.model.impl.Number;
import cz.bia.ea.regression.model.impl.Variable;
import cz.bia.ea.regression.util.RandomNumbers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static cz.bia.ea.regression.model.factory.CompositeExpression.*;

public class Runtime {

	public static void main(String[] args) {
		final Function f1 = new Function() {
			@Override
			public double eval(double x) {
				return Math.cos(2.0 * x);
			}
		};
		final List<Tuple> data1 = DataGenerator.generateDataTuples(f1, 0.0, 2.0 * Math.PI, 200);

		final Function f2 = new Function() {
			@Override
			public double eval(double x) {
				return x*x*x - 2.0*x*x + x;
			}
		};
		final List<Tuple> data2 = DataGenerator.generateDataTuples(f2, 0.0, 10.0, 100);

		final RandomNumbers randomNumbers = new RandomNumbers();
		final ExpressionFactory factory = new ExpressionFactory(randomNumbers);
		factory.setTerminalExpressions(Variable.X, new Number(1.0));
		factory.setBinaryExpressions(PLUS, MINUS, MULTIPLY, DIVIDE);
		factory.setUnaryExpressions(SINE);

		final Evolution evolution = new Evolution(randomNumbers);
		evolution.setExpressionFactory(factory);

		runGP(data1, evolution, 10);
		runGA(data2, evolution, 10);
	}

	private static void runGP(List<Tuple> data, Evolution evolution, int n) {
		final GPConfiguration config = GPConfiguration.createDefaultConfig();
		config.objective = ObjectiveFunction.MAE;
		config.fitnessThreshold = 0.01;
		config.initTreeDepth = 4;

		final List<Evolution.Result> results = new ArrayList<Evolution.Result>(n);
		for (int i = 0; i < n; i++) {
			results.add(evolution.evolveTreeFor(data, config));
		}
		exportResults("GP", results);
	}

	private static void runGA(List<Tuple> data, Evolution evolution, int n) {
		final GAConfiguration config = GAConfiguration.createDefaultConfig();
		config.objective = ObjectiveFunction.MAE;
		config.fitnessThreshold = 0.1;
		config.initPolyOrder = 3;
		config.paramRangeFrom = -5.0;
		config.paramRangeTo = +5.0;

		final List<Evolution.Result> results = new ArrayList<Evolution.Result>(n);
		for (int i = 0; i < n; i++) {
			results.add(evolution.evolvePolyFor(data, config));
		}
		exportResults("GA", results);
	}

	private static void exportResults(String filePrefix, List<Evolution.Result> results) {
		try {
			final BufferedWriter out = new BufferedWriter(new FileWriter(filePrefix + ".out"));
			final BufferedWriter fit = new BufferedWriter(new FileWriter(filePrefix + ".fit"));
			final BufferedWriter time = new BufferedWriter(new FileWriter(filePrefix + ".time"));

			for (Evolution.Result result : results) {
				out.write(Double.toString(result.fitness));
				out.write(' ');
				out.write(Long.toString(result.time));
				out.write(' ');
				out.write(Integer.toString(result.epochs));
				out.write('\n');
				out.flush();

				Iterator<Double> fitIter = result.fitnessProgress.iterator();
				Iterator<Long> timeIter = result.timeProgress.iterator();
				while (fitIter.hasNext()) {
					fit.write(Double.toString(fitIter.next()));
					fit.write(' ');
					time.write(Long.toString(timeIter.next()));
					time.write(' ');
				}
				fit.write('\n');
				fit.flush();
				time.write('\n');
				time.flush();
			}

			out.close();
			fit.close();
			time.close();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

}
