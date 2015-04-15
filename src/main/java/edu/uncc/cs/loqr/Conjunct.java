package edu.uncc.cs.loqr;

import weka.core.Attribute;

/**
 * A one dimensional query conjunct. It's fields are public and immutable.
 */
public final class Conjunct {
	public final Attribute attr;
	public final Op op;
	public final int value;
	public Conjunct(Attribute attr, Op op, int value) {
		this.attr = attr;
		this.op = op;
		this.value = value;
	}
	@Override
	public String toString() {
		return String.format("Conjunct [attr=%s, op=%s, value=%s]", attr, op,
				value);
	}
}