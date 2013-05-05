package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;

public class Power extends AbstractBinaryExpression {

	public Power(@NotNull Expression leftChild, @NotNull Expression rightChild) {
		super(leftChild, rightChild);
	}

	@Override
	public double eval(double x) {
		return Math.pow(leftChild.eval(x), rightChild.eval(x));
	}

	@Override
	public String toStringExpression() {
		return String.format("(%s ^ %s)", leftChild.toStringExpression(), rightChild.toStringExpression());
	}

	@Override
	public String toString() {
		return "L^R";
	}

}
