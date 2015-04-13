package edu.uncc.cs.loqr;

/**
 * A one dimensional query conjunct. It's fields are public and immutable.
 */
public final class Conjunct {
	public final String attr;
	public final int value;
	public Conjunct(String attr, int value) {
		this.attr = attr;
		this.value = value;
	}
}