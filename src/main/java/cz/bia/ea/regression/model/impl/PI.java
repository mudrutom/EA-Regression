package cz.bia.ea.regression.model.impl;

public class PI extends Number {

	public static final PI PI = new PI();

	public PI() {
		super(Math.PI);
	}

	@Override
	public String toStringExpression() {
		return "π";
	}

}
