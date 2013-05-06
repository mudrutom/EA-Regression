package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.NonTerminal;
import cz.bia.ea.regression.model.UnaryExpression;

import java.lang.reflect.Constructor;

import static com.google.common.base.Preconditions.checkNotNull;

@NonTerminal
public abstract class AbstractUnaryExpression extends AbstractExpression implements UnaryExpression {

	private final Constructor<? extends UnaryExpression> constructor;

	protected Expression child;

	public AbstractUnaryExpression(@NotNull Expression child) {
		this.child = checkNotNull(child);

		try {
			constructor = this.getClass().getConstructor(Expression.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Expression duplicate() {
		try {
			return constructor.newInstance(child.duplicate());
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Expression getChild() {
		return child;
	}

	@Override
	public void setChild(@NotNull Expression child) {
		this.child = checkNotNull(child);
	}

	@Override
	public Expression swapChild(@NotNull Expression newChild) {
		final Expression old = child;
		child = checkNotNull(newChild);
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
