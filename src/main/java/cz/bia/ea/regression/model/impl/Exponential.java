package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.UnaryExpression;

public class Exponential extends Power implements UnaryExpression {

	public Exponential(@NotNull Expression child) {
		super(E.E, child);
	}

	@Override
	public Expression getChild() {
		return getRightChild();
	}

	@Override
	public void setChild(Expression child) {
		setRightChild(child);
	}

	@Override
	public Expression swapChild(Expression newChild) {
		return swapRightChild(newChild);
	}

	@Override
	public String toString() {
		return "exp()";
	}

}
