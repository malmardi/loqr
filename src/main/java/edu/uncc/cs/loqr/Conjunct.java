package edu.uncc.cs.loqr;

import weka.core.Attribute;

/**
 * A one dimensional query conjunct. It's fields are public and immutable.
 */
public final class Conjunct {
	public final Attribute attr;
	public final int value;
	public Conjunct(Attribute attr, int value) {
		this.attr = attr;
		this.value = value;
	}
}