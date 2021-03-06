package cz.bia.ea.regression.model;

public interface BinaryExpression extends Expression {

	public Expression getLeftChild();

	public Expression getRightChild();

	public void setLeftChild(Expression leftChild);

	public void setRightChild(Expression rightChild);

	public Expression swapLeftChild(Expression newLeftChild);

	public Expression swapRightChild(Expression newRightChild);

}
