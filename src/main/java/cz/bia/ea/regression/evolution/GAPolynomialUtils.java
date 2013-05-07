package cz.bia.ea.regression.evolution;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.ExpressionWrapper;
import cz.bia.ea.regression.model.impl.Number;
import cz.bia.ea.regression.util.RandomNumbers;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class GAPolynomialUtils {

	private GAPolynomialUtils() { }

	public static void mutation(@NotNull PolyMutationType type, GAPolynomial poly, double rangeForm, double rangeTo, RandomNumbers rnd) {
		switch (checkNotNull(type)) {
		// TODO
		}
	}

	public static void mutationA(@NotNull GAPolynomial poly, double rangeForm, double rangeTo, @NotNull RandomNumbers rnd) {
		checkNotNull(poly); checkArgument(rangeForm < rangeTo); checkNotNull(rnd);
		// TODO
	}

	public static void crossover(@NotNull PolyCrossoverType type, GAPolynomial polyOne, GAPolynomial polyTwo, RandomNumbers rnd) {
		switch (checkNotNull(type)) {
		// TODO
		}
	}

	public static void crossoverA(@NotNull GAPolynomial polyOne, @NotNull GAPolynomial polyTwo, @NotNull RandomNumbers rnd) {
		checkNotNull(polyOne); checkNotNull(polyTwo); checkNotNull(rnd);
		// TODO
	}

	public static Set<Number> traverse(@NotNull ExpressionWrapper expression) {
		checkNotNull(expression);
		final int depth = expression.getDepth();
		final Set<Number> parameters = new LinkedHashSet<Number>(depth + 1, 1.0f);

		recTraverse(expression, parameters);
		return parameters;
	}

	private static void recTraverse(ExpressionWrapper expression, Set<Number> parameters) {
		if (expression.isTerminal() && expression.getExpression() instanceof Number) {
			parameters.add((Number) expression.getExpression());
		} else if (expression.isUnary()) {
			recTraverse((ExpressionWrapper) expression.getChild(), parameters);
		} else if (expression.isBinary()) {
			recTraverse((ExpressionWrapper) expression.getLeftChild(), parameters);
			recTraverse((ExpressionWrapper) expression.getRightChild(), parameters);
		}
	}

	public static enum PolyMutationType {
		A // TODO
	}

	public static enum PolyCrossoverType {
		A // TODO
	}

}
