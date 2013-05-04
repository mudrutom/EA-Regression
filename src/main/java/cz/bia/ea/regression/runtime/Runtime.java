package cz.bia.ea.regression.runtime;

import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.ExpressionWrapper;
import cz.bia.ea.regression.model.factory.ExpressionFactory;

import java.util.List;

public class Runtime {

	public static void main(String[] args) {
		final ExpressionFactory factory = new ExpressionFactory();
		final List<ExpressionWrapper> list = factory.generateExpressions(20, 4);
		for (Expression e : list) {
			System.out.println(e.toStringExpression());
		}
	}

}
