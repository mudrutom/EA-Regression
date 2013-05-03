package cz.bia.ea.regression.model.factory;

import cz.bia.ea.regression.model.impl.*;

public enum CompositeExpression {

	PLUS(Plus.class), MINUS(Minus.class), MULTIPLY(Multiply.class), DIVIDE(Divide.class),
	POWER(Power.class), LOGARITHM(Logarithm.class), EXPONENTIAL(Exponential.class),
	SINE(Sine.class), COSINE(Cosine.class);

	public final Class type;

	private CompositeExpression(Class type) {
		this.type = type;
	}

	public static CompositeExpression[] getUnaryTypes() {
		return new CompositeExpression[]{LOGARITHM, EXPONENTIAL, SINE, COSINE};
	}

	public static CompositeExpression[] getBinaryTypes() {
		return new CompositeExpression[]{PLUS, MINUS, MULTIPLY, DIVIDE, POWER};
	}

}
