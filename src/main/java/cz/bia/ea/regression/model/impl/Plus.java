package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;

public class Plus extends AbstractBinaryExpression {

	public Plus(@NotNull Expression leftChild, @NotNull Expression rightChild) {
		super(leftChild, rightChild);
	}

	@Override
	public double eval(double x) {
		return leftChild.eval(x) + rightChild.eval(x);
	}

	@Override
	public String toStringExpression() {
		return String.format("(%s + %s)", leftChild.toStringExpression(), rightChild.toStringExpression());
	}

}
