package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.BinaryExpression;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.NonTerminal;

import static com.google.common.base.Preconditions.checkNotNull;

@NonTerminal
public abstract class AbstractBinaryExpression extends AbstractExpression implements BinaryExpression {

	protected Expression leftChild;
	protected Expression rightChild;

	public AbstractBinaryExpression(@NotNull Expression leftChild, @NotNull Expression rightChild) {
		this.leftChild = checkNotNull(leftChild);
		this.rightChild = checkNotNull(rightChild);
		modified = true;
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
		modified = true;
	}

	@Override
	public void setRightChild(@NotNull Expression rightChild) {
		this.rightChild = checkNotNull(rightChild);
		modified = true;
	}

	@Override
	public Expression swapLeftChild(@NotNull Expression newLeftChild) {
		final Expression old = leftChild;
		leftChild = checkNotNull(newLeftChild);
		modified = true;
		return old;
	}

	@Override
	public Expression swapRightChild(@NotNull Expression newRightChild) {
		final Expression old = rightChild;
		rightChild = checkNotNull(newRightChild);
		modified = true;
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
