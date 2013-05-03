package cz.bia.ea.regression.model.factory;

import cz.bia.ea.regression.model.BinaryExpression;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.UnaryExpression;

public class ExpressionFactory {

	private ExpressionFactory() { }

	public static Expression createExpression(ExpressionType type) {
		// TODO
		return null;
	}

	public static UnaryExpression createUnaryExpression(ExpressionType type, Expression child) {
		// TODO
		return null;
	}

	public static BinaryExpression createBinaryExpression(ExpressionType type, Expression leftChild, Expression rightChild) {
		// TODO
		return null;
	}

}
