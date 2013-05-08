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

		final RandomNumbers randomNumbers = new RandomNumbers();
		final ExpressionFactory factory = new ExpressionFactory(randomNumbers);
		factory.setTerminalExpressions(Variable.X, new Number(1.0));
		factory.setBinaryExpressions(PLUS, MINUS, MULTIPLY, DIVIDE);
		factory.setUnaryExpressions(SINE);

		final GPConfiguration config1 = GPConfiguration.createDefaultConfig();
		config1.objective = ObjectiveFunction.MAE;
		config1.fitnessThreshold = 0.01;
		config1.initTreeDepth = 4;

		final Evolution evolution = new Evolution(randomNumbers);
		evolution.setExpressionFactory(factory);

		final Expression expression1 = evolution.evolveTreeFor(data1, config1);
		System.out.println(expression1.toStringExpression());

		final Function f2 = new Function() {
			@Override
			public double eval(double x) {
				return Math.log(x);
			}
		};
		final List<Tuple> data2 = DataGenerator.generateDataTuples(f2, 0.1, 10, 200);
		final GAConfiguration config2 = GAConfiguration.createDefaultConfig();
		config2.objective = ObjectiveFunction.MAE;
		config2.fitnessThreshold = 0.01;
		config2.initPolyOrder = 3;
		config2.paramRangeFrom = -1.0;
		config2.paramRangeTo = +1.0;

		final Expression expression2 = evolution.evolvePolyFor(data2, config2);
		System.out.println(expression2.toStringExpression());
	}

}
