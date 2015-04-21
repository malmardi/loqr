package edu.uncc.cs.loqr;

import weka.core.Attribute;

/**
 * A one dimensional query conjunct. It's fields are public and immutable.
 */

//mamoun sds;ldsd
public final class Conjunct {

	//Removed final for the following parameters
	public final Attribute attr;
	public final Op op;
	public final double value;
	public Conjunct(Attribute attr, Op op, double value) {
		this.attr = attr;
		this.op = op;
		this.value = value;
	}

	//copy constructor
	public Conjunct(Conjunct another) {
		this.attr=another.attr;
		this.op=another.op;
		this.value=another.value;
	}
	
	public String toString() {
		return String.format("Conjunct [attr=%s, op=%s, value=%s]", attr, op,
				value);
	}
	
	
	
}