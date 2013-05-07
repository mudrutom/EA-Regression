package cz.bia.ea.regression.model.factory;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.BinaryExpression;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.ExpressionWrapper;
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

	public ExpressionFactory(@NotNull RandomNumbers randomNumbers) {
		this.randomNumbers = checkNotNull(randomNumbers);
		setTerminalExpressions(Variable.X, new Number(0.0), new Number(1.0), E.E, PI.PI);
		setUnaryExpressions(CompositeExpression.getUnaryTypes());
		setBinaryExpressions(CompositeExpression.getBinaryTypes());
	}

	public void setTerminalExpressions(@NotNull Expression... terminals) {
		terminalExpressions = checkNotNull(terminals);
		checkArgument(terminals.length > 0);
	}

	public void setUnaryExpressions(@NotNull CompositeExpression... types) {
		unaryExpressions = checkNotNull(types);
		checkArgument(types.length >= 0);
	}

	public void setBinaryExpressions(@NotNull CompositeExpression... types) {
		binaryExpressions = checkNotNull(types);
		checkArgument(types.length >= 0);
	}

	public List<ExpressionWrapper> generateExpressions(int size, int depth) {
		checkArgument(size > 0);
		final List<ExpressionWrapper> list = new ArrayList<ExpressionWrapper>(size);
		for (int i = 0; i < size; i++) {
			list.add(generateExpression(depth));
		}
		return list;
	}

	public ExpressionWrapper generateExpression(int depth) {
		if (depth < 1) {
			return createTerminalExpression();
		} else if (binaryExpressions.length > 0 && randomNumbers.nextBoolean()) {
			final ExpressionWrapper binary = createBinaryExpression();
			binary.setLeftChild(generateExpression(depth - 1));
			binary.setRightChild(generateExpression(depth - 1));
			return binary;
		} else if (unaryExpressions.length > 0 && randomNumbers.nextBoolean()) {
			final ExpressionWrapper unary = createUnaryExpression();
			unary.setChild(generateExpression(depth - 1));
			return unary;
		}

		return generateExpression(depth - 1);
	}

	public ExpressionWrapper createTerminalExpression() {
		return new ExpressionWrapper(randomNumbers.nextElement(terminalExpressions));
	}

	@SuppressWarnings("unchecked")
	public ExpressionWrapper createUnaryExpression() {
		final CompositeExpression type = randomNumbers.nextElement(unaryExpressions);
		try {
			final Constructor<? extends UnaryExpression> constructor = type.type.getConstructor(Expression.class);
			return new ExpressionWrapper(constructor.newInstance(leaf));
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public ExpressionWrapper createBinaryExpression() {
		final CompositeExpression type = randomNumbers.nextElement(binaryExpressions);
		try {
			final Constructor<? extends BinaryExpression> constructor = type.type.getConstructor(Expression.class, Expression.class);
			return new ExpressionWrapper(constructor.newInstance(leaf, leaf));
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

}
