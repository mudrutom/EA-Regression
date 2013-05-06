package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;

public class Exponential extends AbstractUnaryExpression {

	public Exponential(@NotNull Expression child) {
		super(child);
	}

	@Override
	public double eval(double x) {
		return Math.exp(child.eval(x));
	}

	@Override
	public String toStringExpression() {
		return String.format("exp(%s)", child.toStringExpression());
	}

	@Override
	public String toString() {
		return "exp()";
	}

}
