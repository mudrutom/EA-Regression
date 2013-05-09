package cz.bia.ea.regression.runtime;

import cz.bia.ea.regression.evolution.Evolution;
import cz.bia.ea.regression.evolution.GAConfiguration;
import cz.bia.ea.regression.evolution.GPConfiguration;
import cz.bia.ea.regression.evolution.measure.ObjectiveFunction;
import cz.bia.ea.regression.generate.DataGenerator;
import cz.bia.ea.regression.generate.Function;
import cz.bia.ea.regression.generate.Tuple;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.factory.ExpressionFactory;
import cz.bia.ea.regression.model.impl.Number;
import cz.bia.ea.regression.model.impl.Variable;
import cz.bia.ea.regression.util.RandomNumbers;

import java.util.List;

import static cz.bia.ea.regression.model.factory.CompositeExpression.*;

public class Runtime {

	public static void main(String[] args) {
		final Function f1 = new Function() {
			@Override
			public double eval(double x) {
				return Math.cos(2 * x);
			}
		};
		final List<Tuple> data1 = DataGenerator.generateDataTuples(f1, 0.0, 2 * Math.PI, 200);

		final Function f2 = new Function() {
			@Override
			public double eval(double x) {
				return Math.log(x);
			}
		};
		final List<Tuple> data2 = DataGenerator.generateDataTuples(f2, 0.1, 10, 200);

		final RandomNumbers randomNumbers = new RandomNumbers();
		final ExpressionFactory factory = new ExpressionFactory(randomNumbers);
		factory.setTerminalExpressions(Variable.X, new Number(1.0));
		factory.setBinaryExpressions(PLUS, MINUS, MULTIPLY, DIVIDE);
		factory.setUnaryExpressions(SINE);

		final Evolution evolution = new Evolution(randomNumbers);
		evolution.setExpressionFactory(factory);

		runGP(data1, evolution);
		runGA(data2, evolution);
	}

	private static void runGP(List<Tuple> data, Evolution evolution) {
		final GPConfiguration config = GPConfiguration.createDefaultConfig();
		config.objective = ObjectiveFunction.MAE;
		config.fitnessThreshold = 0.01;
		config.initTreeDepth = 4;

		final Expression expression1 = evolution.evolveTreeFor(data, config).result;
		System.out.println(expression1.toStringExpression());
	}

	private static void runGA(List<Tuple> data, Evolution evolution) {
		final GAConfiguration config = GAConfiguration.createDefaultConfig();
		config.objective = ObjectiveFunction.MAE;
		config.fitnessThreshold = 0.01;
		config.initPolyOrder = 4;
		config.paramRangeFrom = -5.0;
		config.paramRangeTo = +5.0;

		final Expression expression2 = evolution.evolvePolyFor(data, config).result;
		System.out.println(expression2.toStringExpression());
	}

}
