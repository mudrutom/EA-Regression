package cz.bia.ea.regression.evolution;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.ExpressionWrapper;
import cz.bia.ea.regression.model.impl.Number;
import cz.bia.ea.regression.util.RandomNumbers;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class GAPolynomialUtils {

	private GAPolynomialUtils() { }

	public static void mutation(@NotNull PolyMutationType type, GAPolynomial poly, double rangeForm, double rangeTo, RandomNumbers rnd) {
		switch (checkNotNull(type)) {
			case RANDOM_POINT_MUTATION:
				randomPointMutation(poly, rangeForm, rangeTo, rnd);
				break;
			case GAUSSIAN_POINT_MUTATION:
				gaussianPointMutation(poly, rangeForm, rangeTo, rnd);
				break;
		}
	}

	public static void randomPointMutation(@NotNull GAPolynomial poly, double rangeForm, double rangeTo, @NotNull RandomNumbers rnd) {
		checkNotNull(poly); checkArgument(rangeForm < rangeTo); checkNotNull(rnd);

		// select one parameter to change
		final Number toChange = rnd.nextElement(poly.getParameters());
		// assign a random number
		toChange.setNumber(rnd.nextDouble(rangeForm, rangeTo));
	}

	public static void gaussianPointMutation(@NotNull GAPolynomial poly, double rangeForm, double rangeTo, @NotNull RandomNumbers rnd) {
		checkNotNull(poly); checkArgument(rangeForm < rangeTo); checkNotNull(rnd);

		// select one parameter to change
		final Number toChange = rnd.nextElement(poly.getParameters());
		final double delta = 0.1 * (rangeTo - rangeForm);
		// change number by 'normal' increment
		toChange.setNumber(toChange.getNumber() + rnd.nextGaussian() * delta);
	}

	public static void crossover(@NotNull PolyCrossoverType type, GAPolynomial polyOne, GAPolynomial polyTwo, RandomNumbers rnd) {
		switch (checkNotNull(type)) {
			case SIMPLE_CROSSOVER:
				simpleCrossover(polyOne, polyTwo, rnd);
				break;
			case ARITHMETICAL_CROSSOVER:
				arithmeticalCrossover(polyOne, polyTwo, rnd);
				break;
			case SIMULATED_BINARY_CROSSOVER:
				simulatedBinaryCrossover(polyOne, polyTwo, rnd);
				break;
		}
	}

	public static void simpleCrossover(@NotNull GAPolynomial polyOne, @NotNull GAPolynomial polyTwo, @NotNull RandomNumbers rnd) {
		checkNotNull(polyOne); checkNotNull(polyTwo); checkNotNull(rnd);
		checkArgument(polyOne.getOrder() == polyTwo.getOrder());

		final Iterator<Number> paramOne = polyOne.getParameters().iterator();
		final Iterator<Number> paramTwo = polyTwo.getParameters().iterator();
		final int n = polyOne.getParameters().size();
		if (n < 2) {
			// order 0 is not supported
			return;
		}

		// selection of crossover point
		final int point = rnd.nextInt(n);
		// swap parameters
		for (int i = 0; i <= point; i++) {
			Number one = paramOne.next();
			Number two = paramTwo.next();
			double tmp = one.getNumber();
			one.setNumber(two.getNumber());
			two.setNumber(tmp);
		}
	}

	public static void arithmeticalCrossover(@NotNull GAPolynomial polyOne, @NotNull GAPolynomial polyTwo, @NotNull RandomNumbers rnd) {
		checkNotNull(polyOne); checkNotNull(polyTwo); checkNotNull(rnd);
		checkArgument(polyOne.getOrder() == polyTwo.getOrder());

		final Iterator<Number> paramOne = polyOne.getParameters().iterator();
		final Iterator<Number> paramTwo = polyTwo.getParameters().iterator();

		// arithmetic recombination (affine combination)
		final double lambda = rnd.nextDouble();
		while (paramOne.hasNext()) {
			Number one = paramOne.next();
			Number two = paramTwo.next();
			double n1 = one.getNumber();
			double n2 = two.getNumber();
			one.setNumber(lambda * n1 + (1 - lambda) * n2);
			two.setNumber((1 - lambda) * n1 + lambda * n2);
		}
	}

	public static void simulatedBinaryCrossover(@NotNull GAPolynomial polyOne, @NotNull GAPolynomial polyTwo, @NotNull RandomNumbers rnd) {
		checkNotNull(polyOne); checkNotNull(polyTwo); checkNotNull(rnd);
		checkArgument(polyOne.getOrder() == polyTwo.getOrder());

		final Iterator<Number> paramOne = polyOne.getParameters().iterator();
		final Iterator<Number> paramTwo = polyTwo.getParameters().iterator();

		// simulated binary crossover
		final double beta = 1.0 + rnd.nextGaussian();
		while (paramOne.hasNext()) {
			Number one = paramOne.next();
			Number two = paramTwo.next();
			double n1 = one.getNumber();
			double n2 = two.getNumber();
			one.setNumber(0.5 * (n1 + n2 + beta * Math.abs(n2 - n1)));
			two.setNumber(0.5 * (n1 + n2 - beta * Math.abs(n2 - n1)));
		}
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
		RANDOM_POINT_MUTATION, GAUSSIAN_POINT_MUTATION,
	}

	public static enum PolyCrossoverType {
		SIMPLE_CROSSOVER, ARITHMETICAL_CROSSOVER, SIMULATED_BINARY_CROSSOVER
	}

}
