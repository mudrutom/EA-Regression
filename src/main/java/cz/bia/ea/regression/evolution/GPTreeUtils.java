package cz.bia.ea.regression.evolution;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;
import cz.bia.ea.regression.model.ExpressionWrapper;
import cz.bia.ea.regression.model.factory.ExpressionFactory;
import cz.bia.ea.regression.util.RandomNumbers;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class GPTreeUtils {

	private GPTreeUtils() { }

	public static void pointMutation(@NotNull GPTree tree, @NotNull ExpressionFactory factory, @NotNull RandomNumbers rnd) {
		checkNotNull(tree); checkNotNull(factory); checkNotNull(rnd);
		// TODO
	}

	public static void subtreeMutation(@NotNull GPTree tree, @NotNull ExpressionFactory factory, @NotNull RandomNumbers rnd) {
		checkNotNull(tree); checkNotNull(factory); checkNotNull(rnd);
		// TODO
	}

	public static void subtreeCrossover(@NotNull GPTree treeOne, @NotNull GPTree treeTwo, @NotNull RandomNumbers rnd) {
		checkNotNull(treeOne); checkNotNull(treeTwo); checkNotNull(rnd);

		final boolean takeTerm1 = rnd.nextInt(10) == 0; // 10% chance
		final Set<ExpressionWrapper> oneSet = takeTerm1 ? treeOne.getTerminals() : treeOne.getNonTerminals();

		final boolean takeTerm2 = rnd.nextInt(10) == 0; // 10% chance
		final Set<ExpressionWrapper> twoSet = takeTerm2 ? treeOne.getTerminals() : treeOne.getNonTerminals();

		final ExpressionWrapper one = rnd.nextElement(oneSet);
		final ExpressionWrapper two = rnd.nextElement(twoSet);
		final Expression tmp = one.getExpression();
		one.setExpression(two.getExpression());
		two.setExpression(tmp);

		final TraverseResult resultOne = traverse(one);
		treeOne.getTerminals().removeAll(resultOne.terminals);
		treeOne.getNonTerminals().removeAll(resultOne.nonTerminals);

		final TraverseResult resultTwo = traverse(two);
		treeTwo.getTerminals().removeAll(resultTwo.terminals);
		treeTwo.getNonTerminals().removeAll(resultTwo.nonTerminals);

		treeOne.getTerminals().addAll(resultTwo.terminals);
		treeOne.getNonTerminals().addAll(resultTwo.nonTerminals);
		treeTwo.getTerminals().addAll(resultOne.terminals);
		treeTwo.getNonTerminals().addAll(resultOne.nonTerminals);

		treeOne.setDepth(one.getDepth());
		treeTwo.setDepth(two.getDepth());
	}

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
