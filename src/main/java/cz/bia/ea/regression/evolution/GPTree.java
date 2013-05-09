package cz.bia.ea.regression.evolution;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.evolution.measure.Objective;
import cz.bia.ea.regression.generate.Tuple;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.ExpressionWrapper;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class GPTree implements Individual {

	private static Objective OBJECTIVE = null;

	public static void setObjective(@NotNull Objective objective) {
		OBJECTIVE = checkNotNull(objective);
	}

	private final ExpressionWrapper root;

	private final Set<ExpressionWrapper> terminals;
	private final Set<ExpressionWrapper> nonTerminals;

	private int depth;
	private double fitness;

	public GPTree(@NotNull ExpressionWrapper root) {
		checkState(OBJECTIVE != null, "OBJECTIVE must be set");
		this.root = checkNotNull(root);
		final GPTreeUtils.TraverseResult result = GPTreeUtils.traverse(root);
		depth = result.depth;
		terminals = result.terminals;
		nonTerminals = result.nonTerminals;
		fitness = Double.NaN;
	}

	protected ExpressionWrapper getRoot() {
		return root;
	}

	protected Set<ExpressionWrapper> getTerminals() {
		return terminals;
	}

	protected Set<ExpressionWrapper> getNonTerminals() {
		return nonTerminals;
	}

	protected void setDepth(int depth) {
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
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
