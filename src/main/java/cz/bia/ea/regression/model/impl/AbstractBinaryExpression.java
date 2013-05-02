package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.BinaryExpression;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.NonTerminal;

@NonTerminal
public abstract class AbstractBinaryExpression implements BinaryExpression {

	protected Expression leftChild;
	protected Expression rightChild;

	public AbstractBinaryExpression(@NotNull Expression leftChild, @NotNull Expression rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
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
		this.leftChild = leftChild;
	}

	@Override
	public void setRightChild(@NotNull Expression rightChild) {
		this.rightChild = rightChild;
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

}
