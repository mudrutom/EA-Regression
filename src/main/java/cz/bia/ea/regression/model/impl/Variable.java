package cz.bia.ea.regression.model.impl;

import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.Terminal;

@Terminal
public class Variable extends AbstractExpression {

	public static final Variable X = new Variable();

	@Override
	public Expression duplicate() {
		return X;
	}

	@Override
	public double eval(double x) {
		return x;
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

	@Override
	public int getDepth() {
		return 0;
	}

	@Override
	public String toStringExpression() {
		return "X";
	}

}
