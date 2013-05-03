package cz.bia.ea.regression.model.impl;

import cz.bia.ea.regression.model.Expression;

public abstract class AbstractExpression implements Expression {

	protected int depth;
	protected boolean modified;

	public AbstractExpression() {
		depth = 0;
		modified = false;
	}

	@Override
	public int getDepth() {
		return depth;
	}

}
