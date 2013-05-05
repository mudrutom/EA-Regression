package cz.bia.ea.regression.model.impl;

import cz.bia.ea.regression.model.Expression;

public abstract class AbstractExpression implements Expression {

	@Override
	public String toString() {
		return this.toStringExpression();
	}

}
