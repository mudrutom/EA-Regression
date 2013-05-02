package cz.bia.ea.regression.model.impl;

import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.Terminal;

@Terminal
public class Number implements Expression {

	private final double number;

	public Number(double number) {
		this.number = number;
	}

	@Override
	public double eval(double x) {
		return number;
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

	@Override
	public String toStringExpression() {
		return String.format("%g", number);
	}

}
