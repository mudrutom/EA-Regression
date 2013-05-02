package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.NonTerminal;
import cz.bia.ea.regression.model.UnaryExpression;

@NonTerminal
public abstract class AbstractUnaryExpression implements UnaryExpression {

	protected Expression child;

	public AbstractUnaryExpression(@NotNull Expression child) {
		this.child = child;
	}

	@Override
	public Expression getChild() {
		return child;
	}

	@Override
	public void setChild(@NotNull Expression child) {
		this.child = child;
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

}
