package cz.bia.ea.regression.model.impl;

import com.sun.istack.internal.NotNull;
import cz.bia.ea.regression.model.Expression;

public class Exponential extends Power {

	public Exponential(@NotNull Expression child) {
		super(E.val, child);
	}

}
