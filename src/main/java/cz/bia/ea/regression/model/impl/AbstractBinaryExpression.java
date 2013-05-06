package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.BinaryExpression;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.NonTerminal;

import java.lang.reflect.Constructor;

import static com.google.common.base.Preconditions.checkNotNull;

@NonTerminal
public abstract class AbstractBinaryExpression extends AbstractExpression implements BinaryExpression {

	private final Constructor<? extends BinaryExpression> constructor;

	protected Expression leftChild;
	protected Expression rightChild;

	public AbstractBinaryExpression(@NotNull Expression leftChild, @NotNull Expression rightChild) {
		this.leftChild = checkNotNull(leftChild);
		this.rightChild = checkNotNull(rightChild);

		try {
			constructor = this.getClass().getConstructor(Expression.class, Expression.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Expression duplicate() {
		try {
			return constructor.newInstance(leftChild.duplicate(), rightChild.duplicate());
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Expression getLeftChild() {
		return leftChild;
	}

	@Override
	public Expression getRightChild() {
		return rightChild;
	}

	@Override
	public void setLeftChild(@NotNull Expression leftChild) {
		this.leftChild = checkNotNull(leftChild);
	}

	@Override
	public void setRightChild(@NotNull Expression rightChild) {
		this.rightChild = checkNotNull(rightChild);
	}

	@Override
	public Expression swapLeftChild(@NotNull Expression newLeftChild) {
		final Expression old = leftChild;
		leftChild = checkNotNull(newLeftChild);
		return old;
	}

	@Override
	public Expression swapRightChild(@NotNull Expression newRightChild) {
		final Expression old = rightChild;
		rightChild = checkNotNull(newRightChild);
		return old;
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public int getDepth() {
		return 1 + Math.max(leftChild.getDepth(), rightChild.getDepth());
	}

}
