package cz.bia.ea.regression.evolution;

import cz.bia.ea.regression.generate.Tuple;
import cz.bia.ea.regression.model.Expression;

import java.util.List;

public interface Individual extends Comparable<Individual> {

	public Expression getExpression();

	public double getFitness();

	public double computeFitness(List<Tuple> dataTuples);

}
