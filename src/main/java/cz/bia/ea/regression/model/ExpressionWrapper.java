package cz.bia.ea.regression.model;

import com.sun.istack.internal.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExpressionWrapper implements Expression, UnaryExpression, BinaryExpression {

	private Expression expression;

	public ExpressionWrapper(@NotNull Expression expression) {
		this.expression = checkNotNull(expression);
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(@NotNull Expression expression) {
		this.expression = checkNotNull(expression);
	}

	public boolean isUnary() {
		return expression instanceof UnaryExpression;
	}

	public boolean isBinary() {
		return expression instanceof BinaryExpression;
	}

	@Override
	public Expression getLeftChild() {
		return ((BinaryExpression) expression).getLeftChild();
	}

	@Override
	public Expression getRightChild() {
		return ((BinaryExpression) expression).getRightChild();
	}

	@Override
	public void setLeftChild(Expression leftChild) {
		((BinaryExpression) expression).setLeftChild(leftChild);
	}

	@Override
	public void setRightChild(Expression rightChild) {
		((BinaryExpression) expression).setRightChild(rightChild);
	}

	@Override
	public Expression swapLeftChild(Expression newLeftChild) {
		return ((BinaryExpression) expression).swapRightChild(newLeftChild);
	}

	@Override
	public Expression swapRightChild(Expression newRightChild) {
		return ((BinaryExpression) expression).swapRightChild(newRightChild);
	}

	@Override
	public Expression getChild() {
		return ((UnaryExpression) expression).getChild();
	}

	@Override
	public void setChild(Expression child) {
		((UnaryExpression) expression).setChild(child);
	}

	@Override
	public Expression swapChild(Expression newChild) {
		return ((UnaryExpression) expression).swapChild(newChild);
	}

	@Override
	public double eval(double x) {
		return expression.eval(x);
	}

	@Override
	public boolean isTerminal() {
		return expression.isTerminal();
	}

	@Override
	public String toStringExpression() {
		return expression.toStringExpression();
	}

	@Override
	public int getDepth() {
		return expression.getDepth();
	}

}
