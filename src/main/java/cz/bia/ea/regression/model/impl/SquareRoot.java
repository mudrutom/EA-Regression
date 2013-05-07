package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;

public class SquareRoot extends AbstractUnaryExpression {

	public SquareRoot(@NotNull Expression child) {
		super(child);
	}

	@Override
	public double eval(double x) {
		return Math.sqrt(child.eval(x));
	}

	@Override
	public String toStringExpression() {
		return String.format("sqrt(%s)", child.toStringExpression());
	}

	@Override
	public String toString() {
		return "sqrt()";
	}

}
