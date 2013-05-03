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
		modified = true;
	}

	@Override
	public Expression getChild() {
		return child;
	}

	@Override
	public void setChild(@NotNull Expression child) {
		this.child = checkNotNull(child);
		modified = true;
	}

	@Override
	public Expression swapChild(@NotNull Expression newChild) {
		final Expression old = child;
		child = checkNotNull(newChild);
		modified = true;
		return old;
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public int getDepth() {
		return 1 + child.getDepth();
	}

}
