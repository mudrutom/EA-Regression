package cz.bia.ea.regression.model.impl;

import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.Terminal;

@Terminal
public class Number extends AbstractExpression {

	private double number;

	public Number(double number) {
		this.number = number;
	}

	public double getNumber() {
		return number;
	}

	public void setNumber(double number) {
		this.number = number;
	}

	@Override
	public Expression duplicate() {
		return new Number(number);
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
	public int getDepth() {
		return 0;
	}

	@Override
	public String toStringExpression() {
		return String.format("%g", number);
	}

}
