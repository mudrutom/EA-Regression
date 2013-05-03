package cz.bia.ea.regression.model.impl;

import cz.bia.ea.regression.model.Expression;

public abstract class AbstractExpression implements Expression {

	protected boolean modified;

	@Override
	public int getDepth() {
		return 0;
	}

}
