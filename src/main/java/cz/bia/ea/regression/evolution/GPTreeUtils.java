package cz.bia.ea.regression.evolution;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.ExpressionWrapper;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class GPTreeUtils {

	private GPTreeUtils() { }

	public static TraverseResult traverse(@NotNull ExpressionWrapper expression) {
		checkNotNull(expression);
		final int depth = expression.getDepth();
		final int maxNodes = 1 << depth; // 2^depth
		final Set<ExpressionWrapper> terminals = new LinkedHashSet<ExpressionWrapper>(maxNodes, 1.0f);
		final Set<ExpressionWrapper> nonTerminals = new LinkedHashSet<ExpressionWrapper>(maxNodes, 1.0f);

		recTraverse(expression, terminals, nonTerminals);
		return new TraverseResult(depth, terminals, nonTerminals);
	}

	private static void recTraverse(ExpressionWrapper expression, Set<ExpressionWrapper> terminals, Set<ExpressionWrapper> nonTerminals) {
		if (expression.isTerminal()) {
			terminals.add(expression);
		} else if (expression.isUnary()) {
			nonTerminals.add(expression);
			recTraverse((ExpressionWrapper) expression.getChild(), terminals, nonTerminals);
		} else if (expression.isBinary()) {
			nonTerminals.add(expression);
			recTraverse((ExpressionWrapper) expression.getLeftChild(), terminals, nonTerminals);
			recTraverse((ExpressionWrapper) expression.getRightChild(), terminals, nonTerminals);
		}
	}

	public static class TraverseResult {
		public final int depth;
		public final Set<ExpressionWrapper> terminals;
		public final Set<ExpressionWrapper> nonTerminals;

		public TraverseResult(int depth, Set<ExpressionWrapper> terminals, Set<ExpressionWrapper> nonTerminals) {
			this.depth = depth;
			this.terminals = terminals;
			this.nonTerminals = nonTerminals;
		}
	}

}
