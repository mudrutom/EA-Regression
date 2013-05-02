package cz.bia.ea.regression.model.impl;

import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.Terminal;

@Terminal
public class Variable implements Expression {

	@Override
	public double eval(double x) {
		return x;
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

	@Override
	public String toStringExpression() {
		return "X";
	}

}
