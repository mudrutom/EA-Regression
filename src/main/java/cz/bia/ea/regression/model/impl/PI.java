package cz.bia.ea.regression.model.impl;

import cz.bia.ea.regression.model.Expression;

public class PI extends Number {

	public static final PI PI = new PI();

	public PI() {
		super(Math.PI);
	}

	@Override
	public Expression duplicate() {
		return PI;
	}

	@Override
	public String toStringExpression() {
		return "π";
	}

}
