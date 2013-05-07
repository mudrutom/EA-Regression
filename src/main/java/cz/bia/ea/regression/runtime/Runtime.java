package cz.bia.ea.regression.runtime;

import cz.bia.ea.regression.evolution.Evolution;
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
		final Function f = new Function() {
			@Override
			public double eval(double x) {
				return Math.cos(2 * x);
			}
		};
		final List<Tuple> data = DataGenerator.generateDataTuples(f, 0.0, 2 * Math.PI, 200);

		final RandomNumbers randomNumbers = new RandomNumbers();
		final ExpressionFactory factory = new ExpressionFactory(randomNumbers);
		factory.setTerminalExpressions(Variable.X, new Number(1.0));
		factory.setBinaryExpressions(PLUS, MINUS, MULTIPLY, DIVIDE);
		factory.setUnaryExpressions(SINE);

		final GPConfiguration config = GPConfiguration.createDefaultConfig();
		config.objective = ObjectiveFunction.MAE;
		config.fitnessThreshold = 0.01;
		config.initTreeDepth = 4;

		final Evolution evolution = new Evolution(randomNumbers);
		evolution.setExpressionFactory(factory);

		final Expression expression = evolution.evolveTreeFor(data, config);
		System.out.println(expression.toStringExpression());
	}

}
