package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;

public class Sine extends AbstractUnaryExpression {

	public Sine(@NotNull Expression child) {
		super(child);
	}

	@Override
	public double eval(double x) {
		return Math.sin(child.eval(x));
	}

	@Override
	public String toStringExpression() {
		return String.format("sin(%s)", child.toStringExpression());
	}

	@Override
	public String toString() {
		return "sin()";
	}

}
