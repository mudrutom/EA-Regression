package cz.bia.ea.regression.runtime;

import cz.bia.ea.regression.evolution.Configuration;
import cz.bia.ea.regression.evolution.Evolution;
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
				return Math.cos(x) * Math.cos(x);
			}
		};
		final List<Tuple> data = DataGenerator.generateDataTuples(f, -10.0, 10.0, 1000);

		final RandomNumbers randomNumbers = new RandomNumbers();
		final ExpressionFactory factory = new ExpressionFactory(randomNumbers);
		factory.setTerminalExpressions(Variable.X, new Number(1.0));
		factory.setBinaryExpressions(PLUS, MINUS, MULTIPLY, DIVIDE);
		factory.setUnaryExpressions(SINE);

		final Configuration config = Configuration.createDefaultConfig();
		config.objective = ObjectiveFunction.MSE;
		config.fitnessThreshold = 0.001;
		config.initTreeDepth = 3;

		final Evolution evolution = new Evolution(randomNumbers);
		evolution.setExpressionFactory(factory);

		final Expression expression = evolution.evolveFor(data, config);
		System.out.println(expression.toStringExpression());
	}

}
