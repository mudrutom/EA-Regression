package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;

public class Logarithm extends AbstractUnaryExpression {

	public Logarithm(@NotNull Expression child) {
		super(child);
	}

	@Override
	public double eval(double x) {
		return Math.log(child.eval(x));
	}

	@Override
	public String toStringExpression() {
		return String.format("log(%s)", child.toStringExpression());
	}

}
