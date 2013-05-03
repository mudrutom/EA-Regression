package cz.bia.ea.regression.model.impl;

public class E extends Number {

	public static final E E = new E();

	public E() {
		super(Math.E);
	}

	@Override
	public String toStringExpression() {
		return "e";
	}

}
