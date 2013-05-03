package cz.bia.ea.regression.model.factory;

import cz.bia.ea.regression.model.BinaryExpression;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.UnaryExpression;
import cz.bia.ea.regression.model.impl.E;
import cz.bia.ea.regression.model.impl.Number;
import cz.bia.ea.regression.model.impl.PI;
import cz.bia.ea.regression.model.impl.Variable;
import cz.bia.ea.regression.util.RandomNumbers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ExpressionFactory {

	private final RandomNumbers randomNumbers;
	private final Expression leaf = Variable.X;

	private Expression[] terminalExpressions;
	private CompositeExpression[] unaryExpressions, binaryExpressions;

	public ExpressionFactory() {
		this(new RandomNumbers());
	}

	public ExpressionFactory(RandomNumbers randomNumbers) {
		this.randomNumbers = checkNotNull(randomNumbers);
		setTerminalExpressions(new Expression[]{Variable.X, new Number(1.0), E.E, PI.PI});
		setUnaryExpressions(CompositeExpression.getUnaryTypes());
		setBinaryExpressions(CompositeExpression.getBinaryTypes());
	}

	public void setTerminalExpressions(Expression[] terminals) {
		terminalExpressions = checkNotNull(terminals);
	}

	public void setUnaryExpressions(CompositeExpression[] types) {
		unaryExpressions = checkNotNull(types);
	}

	public void setBinaryExpressions(CompositeExpression[] types) {
		binaryExpressions = checkNotNull(types);
	}

	public List<Expression> generateExpressions(int size, int depth) {
		checkArgument(size > 0);
		final List<Expression> list = new ArrayList<Expression>(size);
		for (int i = 0; i < size; i++) {
			list.add(generateExpression(depth));
		}
		return list;
	}

	public Expression generateExpression(int depth) {
		if (depth < 1) {
			return createTerminalExpression();
		} else if (randomNumbers.nextBoolean()) {
			final BinaryExpression binary = createBinaryExpression();
			binary.setLeftChild(generateExpression(depth - 1));
			binary.setRightChild(generateExpression(depth - 1));
			return binary;
		} else if (randomNumbers.nextBoolean()) {
			final UnaryExpression unary = createUnaryExpression();
			unary.setChild(generateExpression(depth - 1));
			return unary;
		}

		return generateExpression(depth - 1);
	}

	public Expression createTerminalExpression() {
		return randomNumbers.nextElement(terminalExpressions);
	}

	@SuppressWarnings("unchecked")
	public UnaryExpression createUnaryExpression() {
		final CompositeExpression type = randomNumbers.nextElement(unaryExpressions);
		try {
			final Constructor<UnaryExpression> constructor = type.type.getConstructor(Expression.class);
			return constructor.newInstance(leaf);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public BinaryExpression createBinaryExpression() {
		final CompositeExpression type = randomNumbers.nextElement(binaryExpressions);
		try {
			final Constructor<BinaryExpression> constructor = type.type.getConstructor(Expression.class, Expression.class);
			return constructor.newInstance(leaf, leaf);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

}
