package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.NonTerminal;
import cz.bia.ea.regression.model.UnaryExpression;

import static com.google.common.base.Preconditions.checkNotNull;

@NonTerminal
public abstract class AbstractUnaryExpression extends AbstractExpression implements UnaryExpression {

	protected Expression child;

	public AbstractUnaryExpression(@NotNull Expression child) {
		this.child = checkNotNull(child);
		depth = computeDepth();
		modified = false;
	}

	@Override
	public Expression getChild() {
		return child;
	}

	@Override
	public void setChild(@NotNull Expression child) {
		this.child = checkNotNull(child);
		depth = computeDepth();
	}

	@Override
	public Expression swapChild(@NotNull Expression newChild) {
		final Expression old = child;
		child = checkNotNull(newChild);
		depth = computeDepth();
		return old;
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	private int computeDepth() {
		return 1 + child.getDepth();
	}

}
