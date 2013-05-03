package cz.bia.ea.regression.generate;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

	private DataGenerator() { }

	public static List<Tuple> generateDataTuples(Function function, double from, double to, int size) {
		// TODO use more threads
		final List<Tuple> data = new ArrayList<Tuple>(size);
		final double delta = (to - from) / size;
		for (double x = from; x <= to; x += delta) {
			data.add(new Tuple(x, function.eval(x)));
		}
		return data;
	}

}
