package cz.bia.ea.regression.model;

/**
 * Represents an expression that can be evaluated for
 * a variable <tt>x</tt>.
 */
public interface Expression {

	/**
	 * @param x double variable <tt>x</tt>
	 * @return result of the expression for <tt>x</tt>
	 */
	public double eval(double x);

	/**
	 * @return <tt>true</tt> IFF the expression is a terminal
	 */
	public boolean isTerminal();

	/**
	 * @return string representation of the expression
	 */
	public String toStringExpression();

	/**
	 * @return depth of the expression tree
	 */
	public int getDepth();

	/**
	 * @return exact duplicate of the expression
	 */
	public Expression duplicate();

}
