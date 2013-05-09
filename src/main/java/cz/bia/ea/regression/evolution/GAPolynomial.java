package cz.bia.ea.regression.evolution;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.evolution.measure.Objective;
import cz.bia.ea.regression.generate.Tuple;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.ExpressionWrapper;
import cz.bia.ea.regression.model.impl.Number;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class GAPolynomial implements Individual {

	private static Objective OBJECTIVE = null;

	public static void setObjective(@NotNull Objective objective) {
		OBJECTIVE = checkNotNull(objective);
	}

	private final ExpressionWrapper root;

	private final Set<Number> parameters;

	private int order;
	private double fitness;

	public GAPolynomial(@NotNull ExpressionWrapper root) {
		checkState(OBJECTIVE != null, "OBJECTIVE must be set");
		this.root = checkNotNull(root);
		parameters = GAPolynomialUtils.traverse(root);
		order = parameters.size() - 1;
		fitness = Double.NaN;
	}

	protected ExpressionWrapper getRoot() {
		return root;
	}

	protected Set<Number> getParameters() {
		return parameters;
	}

	protected void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return order;
	}

	@Override
	public Expression getExpression() {
		return root.getExpression();
	}

	@Override
	public double getFitness() {
		return fitness;
	}

	@Override
	public double computeFitness(@NotNull List<Tuple> dataTuples) {
		checkNotNull(dataTuples);
		final double[] errors = new double[dataTuples.size()];
		final Iterator<Tuple> iterator = dataTuples.iterator();
		for (int i = 0; iterator.hasNext(); i++) {
			Tuple tuple = iterator.next();
			double y = root.eval(tuple.x);
			errors[i] = OBJECTIVE.getError(tuple.y, y);
		}
		fitness = OBJECTIVE.getOverallError(errors);
		fitness = Double.isNaN(fitness) ? Double.POSITIVE_INFINITY : fitness;
		return fitness;
	}

	@Override
	public int compareTo(Individual other) {
		if (other == null || getFitness() < other.getFitness()) {
			return -1;
		} else if (getFitness() > other.getFitness()) {
			return +1;
		} else {
			return 0;
		}
	}

}
