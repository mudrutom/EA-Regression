package cz.bia.ea.regression.generate;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class DataGenerator {

	private DataGenerator() { }

	public static List<Tuple> generateDataTuples(@NotNull Function function, double from, double to, int size) {
		checkNotNull(function);
		checkArgument(to - from > 0.0 && size > 0);

		final List<Tuple> data = new ArrayList<Tuple>(size);
		final double delta = (to - from) / size;
		for (double x = from; x <= to; x += delta) {
			data.add(new Tuple(x, function.eval(x)));
		}
		return data;
	}

}
