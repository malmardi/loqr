package edu.uncc.cs.loqr;

import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;

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
	
	public Optional<String> readName(String content) {
		return Optional.empty();
	}
	
	public Optional<Conjunct> read(String content) {
		return Optional.empty();
	}
}