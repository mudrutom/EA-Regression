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

	public static void mutation(@NotNull TreeMutationType type, GPTree tree, ExpressionFactory factory, RandomNumbers rnd) {
		switch (checkNotNull(type)) {
			case POINT_MUTATION:
				pointMutation(tree, factory, rnd);
				break;
			case SUBTREE_MUTATION:
				subtreeMutation(tree, factory, rnd);
				break;
		}
	}

	public static void pointMutation(@NotNull GPTree tree, @NotNull ExpressionFactory factory, @NotNull RandomNumbers rnd) {
		checkNotNull(tree); checkNotNull(factory); checkNotNull(rnd);

		// select set of terminals or non-terminals
		final boolean takeTerm = tree.getNonTerminals().isEmpty() || rnd.nextBoolean(); // 50% chance
		final Set<ExpressionWrapper> set = takeTerm ? tree.getTerminals() : tree.getNonTerminals();

		// select random element
		final ExpressionWrapper node = rnd.nextElement(set);
		if (node.equals(tree.getRoot())) {
			// TODO try something else
			pointMutation(tree, factory, rnd);
			return;
		}

		// create new expression
		final ExpressionWrapper newNode;
		if (node.isUnary()) {
			newNode = factory.createUnaryExpression();
			newNode.setChild(node.getChild());
		} else if (node.isBinary()) {
			newNode = factory.createBinaryExpression();
			newNode.setLeftChild(node.getLeftChild());
			newNode.setRightChild(node.getRightChild());
		} else {
			newNode = factory.createTerminalExpression();
		}

		// swap the expression
		node.setExpression(newNode.getExpression());
	}

	public static void subtreeMutation(@NotNull GPTree tree, @NotNull ExpressionFactory factory, @NotNull RandomNumbers rnd) {
		checkNotNull(tree); checkNotNull(factory); checkNotNull(rnd);

		// select set of terminals or non-terminals
		final boolean takeTerm = tree.getNonTerminals().isEmpty() || rnd.nextBoolean(); // 50% chance
		final Set<ExpressionWrapper> set = takeTerm ? tree.getTerminals() : tree.getNonTerminals();

		// select random element
		final ExpressionWrapper node = rnd.nextElement(set);
		if (node.equals(tree.getRoot())) {
			// TODO try something else
			subtreeMutation(tree, factory, rnd);
			return;
		}

		// create new expression
		final ExpressionWrapper newNode = factory.generateExpression(2);

		final TraverseResult resultOld = traverse(node);
		final TraverseResult resultNew = traverse(newNode);

		// remove old subtrees
		tree.getTerminals().removeAll(resultOld.terminals);
		tree.getNonTerminals().removeAll(resultOld.nonTerminals);

		// add new subtrees
		tree.getTerminals().addAll(resultNew.terminals);
		tree.getNonTerminals().addAll(resultNew.nonTerminals);

		// swap the expression
		node.setExpression(newNode.getExpression());

		// re-compute depth
		tree.setDepth(tree.getRoot().getDepth());
	}

	public static void crossover(@NotNull TreeCrossoverType type, GPTree treeOne,  GPTree treeTwo, RandomNumbers rnd) {
		switch (checkNotNull(type)) {
			case SUBTREE_CROSSOVER:
				subtreeCrossover(treeOne, treeTwo, rnd);
				break;
		}
	}

	public static void subtreeCrossover(@NotNull GPTree treeOne, @NotNull GPTree treeTwo, @NotNull RandomNumbers rnd) {
		checkNotNull(treeOne); checkNotNull(treeTwo); checkNotNull(rnd);

		// select set of terminals or non-terminals
		final boolean takeTerm1 = treeOne.getNonTerminals().isEmpty() || rnd.nextInt(10) == 0; // 10% chance
		final Set<ExpressionWrapper> oneSet = takeTerm1 ? treeOne.getTerminals() : treeOne.getNonTerminals();
		final boolean takeTerm2 = treeTwo.getNonTerminals().isEmpty() || rnd.nextInt(10) == 0; // 10% chance
		final Set<ExpressionWrapper> twoSet = takeTerm2 ? treeTwo.getTerminals() : treeTwo.getNonTerminals();

		// select random elements
		final ExpressionWrapper one = rnd.nextElement(oneSet);
		final ExpressionWrapper two = rnd.nextElement(twoSet);
		if (one.equals(treeOne.getRoot()) || two.equals(treeTwo.getRoot())) {
			// TODO try something else
			subtreeCrossover(treeOne, treeTwo, rnd);
			return;
		}

		final TraverseResult resultOne = traverse(one);
		final TraverseResult resultTwo = traverse(two);

		// remove old subtrees
		treeOne.getTerminals().removeAll(resultOne.terminals);
		treeOne.getNonTerminals().removeAll(resultOne.nonTerminals);
		treeTwo.getTerminals().removeAll(resultTwo.terminals);
		treeTwo.getNonTerminals().removeAll(resultTwo.nonTerminals);

		// add new subtrees
		treeOne.getTerminals().addAll(resultTwo.terminals);
		treeOne.getNonTerminals().addAll(resultTwo.nonTerminals);
		treeTwo.getTerminals().addAll(resultOne.terminals);
		treeTwo.getNonTerminals().addAll(resultOne.nonTerminals);

		// swap the expressions
		final Expression tmp = one.getExpression();
		one.setExpression(two.getExpression());
		two.setExpression(tmp);

		// re-compute depths
		treeOne.setDepth(treeOne.getRoot().getDepth());
		treeTwo.setDepth(treeTwo.getRoot().getDepth());
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

	public static enum TreeMutationType {
		POINT_MUTATION, SUBTREE_MUTATION
	}

	public static enum TreeCrossoverType {
		SUBTREE_CROSSOVER
	}

}
