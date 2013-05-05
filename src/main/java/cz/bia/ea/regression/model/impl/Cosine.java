package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;

public class Cosine extends AbstractUnaryExpression {

	public Cosine(@NotNull Expression child) {
		super(child);
	}

	@Override
	public double eval(double x) {
		return Math.cos(child.eval(x));
	}

	@Override
	public String toStringExpression() {
		return String.format("cos(%s)", child.toStringExpression());
	}

	@Override
	public String toString() {
		return "cos()";
	}

}
