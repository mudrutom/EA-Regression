package cz.bia.ea.regression.model.impl;

import cz.bia.ea.regression.model.Expression;

public class E extends Number {

	public static final E E = new E();

	public E() {
		super(Math.E);
	}

	@Override
	public Expression duplicate() {
		return E;
	}

	@Override
	public String toStringExpression() {
		return "e";
	}

}
