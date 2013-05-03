package cz.bia.ea.regression.model;

public interface UnaryExpression extends Expression {

	public Expression getChild();

	public void setChild(Expression child);

	public Expression swapChild(Expression newChild);

}
