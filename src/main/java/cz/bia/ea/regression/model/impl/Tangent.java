package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;

public class Tangent extends AbstractUnaryExpression {

	public Tangent(@NotNull Expression child) {
		super(child);
	}

	@Override
	public double eval(double x) {
		return Math.tan(child.eval(x));
	}

	@Override
	public String toStringExpression() {
		return String.format("tan(%s)", child.toStringExpression());
	}

	@Override
	public String toString() {
		return "tan()";
	}

}
