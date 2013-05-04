package cz.bia.ea.regression.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class RandomNumbers {

	private final Random random;

	public RandomNumbers() {
		this(System.nanoTime());
	}

	public RandomNumbers(long seed) {
		random = new Random(seed);
	}

	public void nextBytes(byte[] bytes) {
		random.nextBytes(bytes);
	}

	public boolean nextBoolean() {
		return random.nextBoolean();
	}

	public int nextInt() {
		return random.nextInt();
	}

	public int nextInt(int upto) {
		checkArgument(upto > 0, "invalid bound");
		return random.nextInt(upto);
	}

	public int nextInt(int from, int to) {
		checkArgument(to - from > 0, "invalid bounds");
		return from + random.nextInt(to - from);
	}

	public long nextLong() {
		return random.nextLong();
	}

	public double nextDouble() {
		return random.nextDouble();
	}

	public double nextGaussian() {
		return random.nextGaussian();
	}

	public <E> E nextElement(List<E> from) {
		checkNotNull(from);
		return from.get(nextInt(from.size()));
	}

	public <E> E nextElement(Collection<E> from) {
		checkNotNull(from);
		final int n = nextInt(from.size());
		final Iterator<E> iterator = from.iterator();
		for (int i = 0; i < n - 1; i++) {
			iterator.next();
		}
		return iterator.next();
	}

	public <E> E nextElement(E[] from) {
		checkNotNull(from);
		return from[nextInt(from.length)];
	}

	public <E extends Enum<E>> E nextEnum(Class<E> enumClass) {
		final E[] values = enumClass.getEnumConstants();
		return nextElement(values);
	}

}
